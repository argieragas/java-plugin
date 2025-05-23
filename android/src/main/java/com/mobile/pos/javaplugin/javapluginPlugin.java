package com.mobile.pos.javaplugin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.graphics.Paint;

import androidx.core.app.ActivityCompat;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.exceptions.EscPosConnectionException;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@SuppressLint("MissingPermission")
@CapacitorPlugin(name = "javaplugin")
public class javapluginPlugin extends Plugin {

    private javaplugin implementation = new javaplugin();

    private static final int REQUEST_CODE = 101;

    @PluginMethod
    public void connectToDevice(PluginCall call) {
        String address = call.getString("address");
        JSObject ret = implementation.connectToDevice(address);
        try {
            printer = new EscPosPrinter(implementation.deviceConnection, 203, 48f, 32);
        } catch (EscPosConnectionException e) {
            ret.put("error", e.getMessage());
        }
        call.resolve(ret);
    }

    @PluginMethod
    public void connectBluetooth(PluginCall call) {
        if (implementation.bluetoothAdapter == null) {
            call.reject("Bluetooth not supported on this device");
            return;
        }

        // Check permission
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_CODE);
            call.reject("Permission not granted");
            return;
        }

        if (!implementation.bluetoothAdapter.isEnabled()) {
            call.reject("Bluetooth is not enabled");
            return;
        }

        String macAddress = call.getString("macAddress");
        if (macAddress == null) {
            call.reject("No MAC address provided");
            return;
        }

        BluetoothDevice device = implementation.bluetoothAdapter.getRemoteDevice(macAddress);

        new Thread(() -> {
            try {
                BluetoothSocket socket = device.createRfcommSocketToServiceRecord(implementation.uuid);
                socket.connect();
                if (socket.isConnected()) {
                    call.resolve();
                } else {
                    call.reject("Failed to connect");
                }
            } catch (IOException e) {
                call.reject("Connection failed: " + e.getMessage());
            }
        }).start();
    }

    EscPosPrinter printer;
    @PluginMethod
    public void printTicket (PluginCall call) {
        JSObject ret = new JSObject();
        try {
            String firstName = call.getString("firstName");
            String drawDate = call.getString("drawDate");
            String datePrinted = call.getString("datePrinted");
            String drawTime = call.getString("drawTime");
            String qrcode = call.getString("qrcode");
            String games = call.getString("games");
            String total = call.getString("total");
            String agentCode = call.getString("agentCode");
            String betTime = call.getString("betTime");
            String betDate = call.getString("betDate");
            String area = call.getString("area");
            String gameType = call.getString("gameType");
            String referenceNumber = call.getString("referenceNumber");
            String combinations = call.getString("combinations");
            int max = call.getInt("maxSize");
            total = total.replace(".00", "");
            Bitmap logo = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.antiguas);
            logo = Bitmap.createScaledBitmap(logo, 384, 130, true);

            printer.printFormattedText("[C]<img>"+ PrinterTextParserImg.bitmapToHexadecimalString(printer, logo)+"</img>\n\n"+
                    "[C]Small Town Lottery - ADN\n"+
                    "[L]BET DATE   : "+betDate+"\n"+
                    "[L]BET TIME   : "+betTime+"\n"+
                    "[L]DRAW DATE  : "+drawDate+"\n"+
                    "[L]TX CODE    : "+qrcode+"\n"+
                    "[L]AREA       : "+area+"\n"+
                    "[L]ADN-170"+" ".repeat(25-referenceNumber.length())+""+referenceNumber+"\n"+
                    "[L]--------------------------------\n"+
                    "[L]Bet   Draw       Amt         Win\n"+
                    "[L]--------------------------------\n"+
                    "[L]GAME: "+gameType+"\n"+
                    "[L]"+games+"\n"+
                    "[L]     "+combinations+"\n"+
                    "[L]--------------------------------\n"+
                    "[L]TOTAL: "+" ".repeat(25-total.length())+""+total+"\n"+
                    "[C]Winning tickets should be\n" +
                    "[C]claimed within 1 year after\n" +
                    "[C]bet date, otherwise winning\n" +
                    "[C]prize shall be forfeited.\n"+
                    "[C]NO TICKET, NO PAYOUT.\n"+
                    "[C]<img>"+ PrinterTextParserImg.bitmapToHexadecimalString(
                    printer,
                    generateQrCodeWithLogo(
                            qrcode,
                            Bitmap.createScaledBitmap(
                                    BitmapFactory.decodeResource(
                                            getContext().getResources(),
                                            R.drawable.antiguas_logo
                                    ),
                                    300,
                                    300,
                                    true
                            )
                    )
            ) +"</img>\n" +
                    "[C]Thank you and Play Again!\n"+
                    "[C]<img>"+PrinterTextParserImg.bitmapToHexadecimalString(printer, Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.adnad), 400, 80, true))+"</img>",0);

            ret.put("success","");
        } catch (Exception e) {
            ret.put("error", e.getMessage());
        }
        call.resolve(ret);
    }

    @PluginMethod
    public void testPrint(PluginCall call) {
        JSObject ret = new JSObject();
        try {
            Bitmap logo = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.antiguas_monochrome);
            logo = Bitmap.createScaledBitmap(logo, 384, 136, true);

            String hexImage = PrinterTextParserImg.bitmapToHexadecimalString(printer, logo);
            printer.printFormattedText("[C]<img>"+PrinterTextParserImg.bitmapToHexadecimalString(printer, logo)+"</img>\n"+
                    "[C]Small Town Lottery - ADN\n"+
                    "[L]BET DATE   : Apr 13, 2025\n"+
                    "[L]BET TIME   : 04:43 PM\n"+
                    "[L]DRAW DATE  : Apr 13, 2025\n"+
                    "[L]TX CODE    : CMEW-RPIA-MFBP-DQ2F\n"+
                    "[L]AREA       : D-3, PUNTA NASIPIT, ADN; NEW AREA\n"+
                    "[L]ADN-170      UT2Q-NQ1F-GWN1-R5SB\n"+
                    "[L]--------------------------------\n"+
                    "[L]Bet   Draw       Amt            Win\n"+
                    "[L]--------------------------------\n"+
                    "[L]GAME: 3D\n"+
                    "[L]"+
                    "[C]<img>"+ PrinterTextParserImg.bitmapToHexadecimalString(
                    printer,
                    generateQrCodeWithLogo(
                            "ngi2m2yXmMyXndu5os1Iz1HJovq=",
                            Bitmap.createScaledBitmap(
                                    BitmapFactory.decodeResource(
                                            getContext().getResources(),
                                            R.drawable.antiguas_logo
                                    ),
                                    300,
                                    300,
                                    true
                            )
                    )
            ) +"</img>\n" +
                    "[C]Thank you and Play Again!\n"+
                    "[C]<img>"+PrinterTextParserImg.bitmapToHexadecimalString(printer, Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.adnad), 400, 80, true))+"</img>");

            /*printer.printFormattedText(
                    "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.header), 384, 176, true))+"</img>\n" +
                            "[L]\n" +
                            "[C]<u><font size='big'>ORDER N°045</font></u>\n" +
                            "[L]\n" +
                            "[C]================================\n" +
                            "[L]\n" +
                            "[L]<b>BEAUTIFUL SHIRT</b>[R]9.99e\n" +
                            "[L]<font size='small'></font>\n"+
                            "[L]  + Size : S\n" +
                            "[L]\n" +
                            "[L]\n" +
                            "[C]--------------------------------\n" +
                            "[R]TOTAL PRICE :[R]34.98e\n" +
                            "[R]TAX :[R]4.23e\n" +
                            "[L]\n" +
                            "[C]================================\n" +
                            "[L]\n" +
                            "[L]<font size='tall'>Customer :</font>\n" +
                            "[L]Raymond DUPONT\n" +
                            "[L]5 rue des girafes\n" +
                            "[L]31547 PERPETES\n" +
                            "[L]Tel : +33801201456\n" +
                            "[L]\n" +
                            "[C]<barcode type='ean13' height='10'>831254784551</barcode>\n" +
                            "[C]<qrcode size='20'>https://dantsu.com/</qrcode>", 40);*/
        } catch (Exception e) {
            ret.put("value", e.getMessage());
        }
        call.resolve(ret);
    }
    public static Bitmap generateQrCodeWithLogo(String text, Bitmap logoBitmap) throws Exception {
        int size = 400; // width and height

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.MARGIN, 6);
        hints.put(EncodeHintType.ERROR_CORRECTION, com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.H);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, size, size, hints);
        Bitmap qrBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                qrBitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        // Now draw the logo on the center
        Canvas canvas = new Canvas(qrBitmap);

        float scaleFactor = (size * 0.15f) / logoBitmap.getWidth();

        Matrix matrix = new Matrix();
        matrix.postScale(scaleFactor, scaleFactor);
        matrix.postTranslate(
                (size - logoBitmap.getWidth() * scaleFactor) / 2,
                (size - logoBitmap.getHeight() * scaleFactor) / 2
        );

        canvas.drawBitmap(logoBitmap, matrix, null);

        return qrBitmap;
    }

    public String padStart(String text) {
        String str = "";
        for(int i = 0; i < 16 - text.length(); i++) {
            str += " ";
        }
        str += text;
        return str;
    }
    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", implementation.echo(value));
        call.resolve(ret);
    }
}

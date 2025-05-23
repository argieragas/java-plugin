export interface javapluginPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  testPrint(): Promise<{ value: string }>;
  connectToDevice(options: { address: string }): Promise<{ value: string, success: string, error: string}>;
  printTicket(options: {
    firstName: string;
    drawDate: string;
    datePrinted: string;
    qrcode: string;
    games: string;
    total: string;
    drawTime: string;
    agentCode: string;
    maxSize: number;
    betTime: string;
    betDate: string;
    area: string;
    gameType: string;
    referenceNumber: string;
    combinations: string;
  }): Promise<{success: string, error: string}>;
  connectBluetooth(options: { macAddress: string }): Promise<void>;
}

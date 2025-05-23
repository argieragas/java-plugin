import { WebPlugin } from '@capacitor/core';

import type { javapluginPlugin } from './definitions';

export class javapluginWeb extends WebPlugin implements javapluginPlugin {
  printTicket(options: {
    firstName: string;
    drawDate: string;
    datePrinted: string;
    qrcode: string;
    games: string;
    total: string;
    agentCode: string;
    drawTime: string;
    maxSize: number;
    betTime: string;
    betDate: string;
    area: string;
    gameType: string;
    referenceNumber: string;
    combinations: string;
  }): Promise<{ success: string; error: string; }> {
    throw new Error(`Method not implemented. ${options}`);
  }
  connectToDevice(options: { address: string; }): Promise<{ value: string; success: string; error: string; }> {
    throw new Error(`Method not implemented. ${options}`);
  }
  testPrint(): Promise<{ value: string; }> {
    throw new Error('Method not implemented.');
  }
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
  async connectBluetooth(options: { macAddress: string }): Promise<void> {
    console.warn(`Bluetooth connection is not supported on web. \n${options}`);
    throw this.unavailable('Bluetooth is not supported on web.');
  }
}

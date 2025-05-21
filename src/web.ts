import { WebPlugin } from '@capacitor/core';

import type { javapluginPlugin } from './definitions';

export class javapluginWeb extends WebPlugin implements javapluginPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}

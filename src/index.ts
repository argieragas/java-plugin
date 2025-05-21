import { registerPlugin } from '@capacitor/core';

import type { javapluginPlugin } from './definitions';

const javaplugin = registerPlugin<javapluginPlugin>('javaplugin', {
  web: () => import('./web').then((m) => new m.javapluginWeb()),
});

export * from './definitions';
export { javaplugin };

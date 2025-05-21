export interface javapluginPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}

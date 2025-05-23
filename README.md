# java-plugin

bluetooth connection and can print using bluetooth

## Install

```bash
npm install java-plugin
npx cap sync
```

## API

<docgen-index>

* [`echo(...)`](#echo)
* [`testPrint()`](#testprint)
* [`connectToDevice(...)`](#connecttodevice)
* [`printTicket(...)`](#printticket)
* [`connectBluetooth(...)`](#connectbluetooth)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### echo(...)

```typescript
echo(options: { value: string; }) => Promise<{ value: string; }>
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ value: string; }</code> |

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------


### testPrint()

```typescript
testPrint() => Promise<{ value: string; }>
```

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------


### connectToDevice(...)

```typescript
connectToDevice(options: { address: string; }) => Promise<{ value: string; success: string; error: string; }>
```

| Param         | Type                              |
| ------------- | --------------------------------- |
| **`options`** | <code>{ address: string; }</code> |

**Returns:** <code>Promise&lt;{ value: string; success: string; error: string; }&gt;</code>

--------------------


### printTicket(...)

```typescript
printTicket(options: { firstName: string; drawDate: string; datePrinted: string; qrcode: string; games: string; total: string; drawTime: string; agentCode: string; maxSize: number; betTime: string; betDate: string; area: string; gameType: string; referenceNumber: string; combinations: string; }) => Promise<{ success: string; error: string; }>
```

| Param         | Type                                                                                                                                                                                                                                                                                            |
| ------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **`options`** | <code>{ firstName: string; drawDate: string; datePrinted: string; qrcode: string; games: string; total: string; drawTime: string; agentCode: string; maxSize: number; betTime: string; betDate: string; area: string; gameType: string; referenceNumber: string; combinations: string; }</code> |

**Returns:** <code>Promise&lt;{ success: string; error: string; }&gt;</code>

--------------------


### connectBluetooth(...)

```typescript
connectBluetooth(options: { macAddress: string; }) => Promise<void>
```

| Param         | Type                                 |
| ------------- | ------------------------------------ |
| **`options`** | <code>{ macAddress: string; }</code> |

--------------------

</docgen-api>

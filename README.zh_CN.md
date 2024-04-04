# SerialPortToolFX

## 模拟回复

### json配置格式:

```json
{
  "encode": "HEX",
  "packSize": "13",
  "delimiter": "",
    ...(以下由自己定义 K 表示收到的串口消息  V表示需要回复的消息)
}
```

### json文件内容要求:

1. json只能有一层,不可以有多层
2. json的键值对都是字符串类型(都用 " " 包裹)
3. json中的encode用于指示json文件中编码格式  
   - 目前可供选择的参数(不区分大小写)有 **HEX  ASCII**
   - 如果encode 指定为  HEX  则下文中的键值对都以16进制进行解码
   - 如果encode 指定为  ASCII则下文中的键值对都以ASCII进行解码
4. json文件中的packSize和delimiter用于指定串口消息的完整度判断方式
   - packSize表示用数据包的长度来判断消息完整度
   - delimiter表示用特定符号来判断消息完整度
   - **如果同时指定了packSize 和 delimiter  那么delimiter将会被忽略 除非 packSize的内容不正确(非数字)**
   - **packSize 和 delimiter 不能都为空**

### json配置示例:

使用ASCII并且使用数据包大小来断帧

```json
{
  "encode": "ASCII",
  "packSize": "5",
  "delimiter": "",
  "abcde":"ABCDE",
   "12345":"12345"
}
```

使用ASCII并且使用结束符来断帧

```json
{
  "encode": "ASCII",
  "packSize": "",
  "delimiter": "\r\n",
  "A\r\n":"B\r\n",
   "AAAA\r\n":"BAAA\r\n"
}
```





使用HEX并且使用数据包大小来断帧

```json
{
  "encode": "HEX",
  "packSize": "5",
  "delimiter": "",
  "F1 F2 F3 F4 F5":"01 02 03 04 05",
  "F0 F2 F3 F4 F5":"01 02 03 04 05"
}
```

使用HEX并且使用结束符来断帧

```json
{
  "encode": "HEX",
  "packSize": "",
  "delimiter": "0D 0A",
  "01 02 0D 0A":"01 02 0D 0A",
  "01 03 0D 0A":"01 03 05 0D 0A"
}
```


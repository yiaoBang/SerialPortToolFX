# SerialPortToolFX

## 模拟回复

json配置:

key 和 value  都是字符串

```json
{
  "encode": "HEX",
  "packSize": "13",
  "delimiter": "",
    ...(以下由自己定义 K 表示收到的串口消息  V表示需要回复的消息)
}
```

encode: 用于指示json中的编码格式   可选参数有    HEX     ASCII

packSize:用于指定数据包大小

 delimiter:指定消息分隔符

注意:如果指定了packSize那么delimiter的结果将被忽略

配置示例:

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


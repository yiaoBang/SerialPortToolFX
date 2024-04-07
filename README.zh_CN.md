# SerialPortToolFX

跨平台开源串口调试工具,使用java编写

## 特性

- 跨平台
- 支持串口参数设置
- 串口开关显示(绿:开  红:关)
- 实时更新串口列表,新增设备无需关闭软件
- 支持ASCII与HEX收发数据
- 支持新建窗口(无法打开多份软件,窗口关闭自动释放串口资源)
  - 打开多个窗口后只有所有的窗口都关闭后软件才会彻底退出
- **支持串口数据的模拟回复(模拟设备)**
- 支持数据的循环发送,且可以设置时间间隔
- 支持数据收发计数使用long类型)和重置计数(
- 如果使用ASCII发送则支持转义字符(例如 \r \n \t...)
- 支持数据显示开关(勾选接收显示则接收到数据后会在软件中进行显示,否则不会)
  - 接收到的数据最多显示12800个字节,放置缓存过多的数据导致软件卡死
- 支持数据收发的持久化(勾选 记录发送数据/记录接收数据)
  - 文件名会使用串口号和开始时间+read/write 进行保存

## 使用



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


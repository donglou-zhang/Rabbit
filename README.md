# Rabbit

从头搭建一个RPC框架，初步打算使用Netty进行数据通信，后期可能会增加BIO和NIO的方式

因为是小白入门级别，所以项目代号Rabbit

1、首先，先解释下RPC：

RPC = Remote Procedure Call ，远程过程调用，它能够通过网络从远程计算机上请求服务，且满足底层网络传输对用户透明化，让用户如同调用本地服务一般去调用远程服务。

2、RPC与HTTP请求的区别：

利用应用层的HTTP协议，也可以满足客户/服务器模式的信息交换，那为何还需要更为复杂的RPC框架呢？

我们知道，使用HTTP接口，一般有四个过程：（3次握手）建立（TCP协议）连接，发送请求信息，返回响应信息，（4次握手）关闭连接。之所以与RPC混为一谈，就是因为HTTP请求也包括了发送请求和返回消息这两个过程。


但是，这二者不是一种类型的事物：

RPC不是某一种专门的协议，而可以认为是一种编程模式，把对服务器的调用抽象为过程调用，其关键在于请求、响应消息体的封装、编码协议等，因为在RPC中也存在着网络传输，所以HTTP甚至可以用于RPC的传输协议。但是HTTP传输，本身更多的是关注底层数据的传输，而RPC不仅可以传输数据，也可以做到服务发现、函数代理调用等，可以看做是面向服务的封装。相对于HTTP，RPC做了更多的封装，使得用户开发更加轻松简单。

另一方面，RPC一般用于企业内网服务间的调用，HTTP一般用于外部，可以做到跨语言的传输。成熟的RPC框架一般使用二进制传输，而一般HTTP使用json，性能相对于RPC而言较低。HTTP自身的编码协议不够精简，往往包含太多的无用报文元数据。RPC可以自定义编码协议，能精简传输内容。

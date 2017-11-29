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

3、Rpc与动态代理
Rpc中，动态代理是非常重要的一个内容，下面我将以Jdk动态代理的方式陈述一下Client端的调用过程。待本项目完成后，客户端使用流程如下：

    (1) RpcClient client = new RpcClient();

如果我们希望调用服务端的DemoService类的helloWord()方法，则

>     getProxy(Class<?> rpcInterface)使用了Jdk的动态代理，内部调用了Proxy.newProxyInstance方法


    (2) DemoService service = client.getProxy(DemoService.class);

>     newProxyInstance这个方法有三个参数
>     @ loader, 定义了一个ClassLoader对象，用于加载生成的代理对象
>     @ interfaces，给需要代理的对象提供一组接口，可认为该对象实现了这些接口，因此可调用这些接口中的方法
>     @ h，一个InvocationHandler对象，表示当这个动态代理对象在调用方法时，会关联到哪一个InvocationHandler上

    (3) Proxy.newProxyInstance(Classloader, Class<?>[] interfaces, InvocationHandler h);

>     当调用helloWord方法时，实则会根据(2)中关联的InvocationHandler，调用它的invoke()方法

    (4) service.helloWorld("Hello, World");

>     InvocationHandler的invoke方法会调用RpcInvoker的invoke方法，这是因为客户端和服务端是不同的逻辑：
>     @ proxy, 所代理的真实对象(服务端调用时的ServiceBeanImpl，而不是接口)
>     @ method，所要调用代理的某个方法
>     @ args，调用方法时传入的参数

    (5) Class RpcInvocationHandler: Object invoke(Object proxy, Method method, Object[] args) {
    	...
    	rpcInvoker.invoker(request);
    	...
    }

>     客户端和服务端的逻辑不一样：
>     客户端，需要通过connector将request消息发送给服务端
>     服务端，接收到客户端传入的信息，通过method.invoke()调用真实对象，执行方法，获得结果并返回给服务端

    (6) Class RpcInvoker: RpcMessage invoke();


## RabbitMq
### 1.RabbitMq 简介
> ActiveMQ是用erlang写的，这里简单介绍一下这个家伙是什么，Erlang 是由爱立信公司开发的一种平台式语言，可以说是一种自带了操作系统平台的编程语言，
  而且在这个平台上实现了并发机制\进程调度、内存管理、分布式计算、网络通讯等功能，
  这些功能都是完全独立于用户的操作系统的，它采用的是类似于Java一样的虚拟机的方式
  来实现对操作系统的独立性的。
  
### 2.基本概念
- 虚拟主机（virtual host）
    ：虚拟主机，一个broker里可以开设多个vhost，用作不同用户的权限分离。
- 交换机（exchange）
    ：消息交换机，它指定消息按什么规则，路由到哪个队列，可以理解为具有路由表的路由程序，交换机中有一系列的binding。
- 队列（queue）
    ：消息队列载体，每个消息都会被投入到一个或多个队列,可以理解为装消息的容器。
- 绑定（binding）  
    ：交换和队列之间的关系称为绑定。
- 路由键（Routing Key）
    ：每个消息都有一个称为路由键（routing key）的属性
- 消息生产者（producer）
    ：消息生产者，就是投递消息的程序。
- 消息消费者（consumer）
    ：消息消费者，就是接受消息的程序。
- 消息通道（channel）
    ：消息通道，在客户端的每个连接里，可建立多个channel，每个channel代表一个会话任务。
- 临时队列
    ：每当我们连接到RabbitMq，我们需要一个新的空的队列。为此，我们可以创建一个具有随机名称的队列，或者甚至更好让服务器为我们选择一个随机队列名称。
     其次，一旦我们断开消费者，队列应该被自动删除。
     queueDeclare（）没有提供参数时可以生产一个临时队列 
  
### 3.RabbitMq安装  

- 安装erlang环境
```
    下载安装包
    wget http://erlang.org/download/otp_src_R13B04.tar.gz
    
    error: /bin/sh '/home/www/software/otp_src_R13B04/erts/configure' failed for erts
    yum -y install ncurses-devel
    再然后./configure
    
    make && make install
    
    测试安装成功与否 erl     
``` 
- 安装rabbitmq-server包
``` 
    启动服务
    rabbitmq-server -detached 守护进程
    
    详细说明 --> https://www.rabbitmq.com/configure.html#customise-general-unix-environment
    $RABBITMQ_HOME/etc/rabbitmq/rabbitmq-env.conf
    
    详细说明 --> https://www.rabbitmq.com/configure.html#configuration-file
    $RABBITMQ_HOME/etc/rabbitmq/rabbitmq.config
```
- 安装rabbitmq 插件
```
    rabbitmq-plugins enable rabbitmq_management
    默认账号和密码都是guest
```

- 端口
```
    4369：epmd，RabbitMQ节点和CLI工具使用的对等发现服务
    5672,5671：由AMQP 0-9-1和1.0客户端使用，不带TLS和TLS
    25672：Erlang分发用于节点间和CLI工具通信，并从动态范围分配（默认情况下限制为单个端口，计算为AMQP端口+ 20000）。有关详细信息，请参阅网络指南。
    15672：HTTP API客户端和rabbitmqadmin（仅当启用管理插件时）
    61613，61614：没有和使用TLS的STOMP客户端（只有启用了STOMP插件）
    1883,8883 :( 没有和带有TLS的MQTT客户端，如果启用了MQTT插件
    15674：STOMP-over-WebSockets客户端（只有启用了Web STOMP插件）
    15675：MQTT-over-WebSockets客户端（仅当启用了Web MQTT插件时
```
注意 erlang的版本与abbitmq-server的版本

### 4.client 方法
Java Api : https://www.rabbitmq.com/api-guide.html
- ConnectionFactory
    
- Connection

- Channel

- channel.exchangeDeclare(...)

- channel.queueDeclare(...)

- channel.basicPublish(...)
    第一个参数是交换的名称
    第二个是 routing key
    第三个配置信息
    第四个是 消息内容
    
- channel.queueBind(...)
    第一个参数是 队列名称
    第二个是 交换的名称

### 资料文档
``` 
    http://www.linuxidc.com/Linux/2011-12/49610p2.htm
    rabbitmq 使用文档
    https://www.rabbitmq.com/getstarted.html
```
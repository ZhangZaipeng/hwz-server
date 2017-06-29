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
    ：其实就是一个基于交换机的路由键与队列连接起来的路由规则。
- 路由键（Routing Key）
    ：每个消息都有一个称为路由键（routing key）的属性
- 消息生产者（producer）
    ：消息生产者，就是投递消息的程序。
- 消息消费者（consumer）
    ：消息消费者，就是接受消息的程序。
- 消息通道（channel）
    ：消息通道，在客户端的每个连接里，可建立多个channel，每个channel代表一个会话任务。
``` 
    http://www.linuxidc.com/Linux/2011-12/49610p2.htm
    rabbitmq 使用文档
    https://www.rabbitmq.com/getstarted.html
```    
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
 
    ![an](http://blog.csdn.net/hzhsan/article/details/49427283)
- 安装abbitmq-server包
``` 
```
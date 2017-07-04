### docker命令
    docker安装 --> yum -y install docker
    docker停止 --> systemctl start docker.service
    docker启动 --> systemctl stop docker.service
    docker开机启动 --> systemctl enable docker.service
    
### docker镜像概念
#### 一、镜像的特点
- 1 是针对Docker的特殊的只读文件
- 2 包含了应用程序及其运行环境的集合
- 3 加载Docker中，重现一切
- 4 可根据我们的设计生成新的镜像
- 5 是Docker核心概念之一
- 6 Docker容器的运行必须以镜像的成功加载为前提
 
#### 二、镜像的表示方法
- 1 仓库名称：[标签]。 如 centos:7, zhangsan/centos:7, dockerhub:5000/zhangsan/ubuntu:14.05
- 2 镜像ID号： 5506de43b 
- 3 查看镜像命令：docker images | less 
    REPOSITORY   -->  zzp/nginx   那个账号下的那个仓库    
    TAG --> centos7                
    IMAGE ID  --> 5506de43b          
    CREATED --> 创建时间            
    SIZE  --> 397M
- 4 镜像 搜索 获取 删除
    docker search 关键字
    eg: docker search lamp
    NAME：镜像所在仓库， DESCRIPTION描述 ， STARS星级欢迎程度 ， OFFICIAL：是否是官方生产 AUTOMATED
    
    docker pull  NAME
    eg: docker pull tutum/lamp
    

    
- 5 镜像存出、载入、新增名称
    docker save -o 文件名 用户名/仓库名：标签=REPOSITORY：TAG
    eg: docker save -o lamp tutum/lamp:latest
    
    docker --input centos_7.tar
    docker load < lamp
    
    docker tag 用户名/仓库名：标签 用户名/新名词:[新标签]
    eg: docker tag tutum/lamp:latest daoke/lamp:centos7
    
- 6 镜像上传、删除
    docker push zhangsan/centos:[7]
    eg: docker push daoke/lamp:centos7
    
    docker rmi name：tag
    docker rmi 镜像ID
    eg: docker rmi lamp
    
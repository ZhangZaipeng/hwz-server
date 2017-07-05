#### docker命令

    docker安装 --> yum -y install docker
    docker停止 --> systemctl start docker.service
    docker启动 --> systemctl stop docker.service
    docker开机启动 --> systemctl enable docker.service
    
#### docker镜像概念

- 一、镜像的特点
    - 1 是针对Docker的特殊的只读文件
    - 2 包含了应用程序及其运行环境的集合
    - 3 加载Docker中，重现一切
    - 4 可根据我们的设计生成新的镜像
    - 5 是Docker核心概念之一
    - 6 Docker容器的运行必须以镜像的成功加载为前提
 
- 二、镜像的表示方法
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
        
#### docker 容器

- 一、容器的概念
  - 1.是镜像加载到Docker运行的一个实例
  - 2.是镜像里的应用程序及其运行环境的再现
  - 3.带有一个可写的文件层
- 二、容器的创建，启动
    - 1.创建
        docker create [选项] 镜像 运行的程序
        选项：
            -i 让容器的输入保持打开
            -t 让Docker分配一个伪终端
        eg: docker create -it centos:7 /bin/bash 
        通过docker create 创建的容器有一个ID和名称，并默认是处于停止状态的
    - 2.启动
        docker start 容器的ID/名称
    - 3 创建并启动
        docker run centos:7 /bin/bash c ls /
        使用docker run 来运行一个容器时，只要后面的命令运行结束，容器就停止
        镜像不存在时会从共有仓库下载
- 三、容器的运行，终止
    
    - 1.守护态运行
        docker run -d centos:7 /bin/bash c "while true; do echo hello world; done"
    - 2.查看容器的运行状态
        docker ps -a  
        
        CONTAINER ID  容器的ID      
        IMAGE  加载的镜像             
        COMMAND  运行的程序           
        CREATED   创建的时间          
        STATUS   目前处的状态           
        PORTS   端口映射            
        NAMES   运行容器的名称
    - 3.终止
        docker stop 容器的ID/名称
- 四、Docker容器的进入 
    - 1 docker exec -it 容器的ID/名称 /bin/bash
        [root@容器ID /]#
    - 2 退出 exit
- 五、Docker容器的导出与导入
    - 1.导出一个已经创建好的容器到文件
        docker export 容器的ID/名称 > centostar
    - 2.将一个导出的文件导入成为镜像 
        cat centostar | docker import - centos7:test
- 六、Docker容器的删除
    - 1.删除停止容器
        docker rm 容器的ID/名称
    - 2.删除运行的容器
        docker rm -f 容器的ID/名称
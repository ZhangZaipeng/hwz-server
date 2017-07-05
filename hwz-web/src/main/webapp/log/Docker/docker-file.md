#### docker创建镜像方式
- 基于已有的Docker容器的创建镜像
    docker commit [选项] 容器的ID/名称 仓库名称:[标签]
    选项
        -m 说明信息
        -a 作者信息
        -p 生产过程中停止容器的运行
    不指定标签时默认为latest标签

- 基于本地模板创建
    可以导入操作系统模板文件生成一个镜像
    模板可以从OPENVZ开源下载
    cat centos-7-x86_64.tar.gz | docker import - centos
    
#### Dockerfile 是什么？
```
    Dockerfile是由一系列命令和参数构成的脚本，这些命令应用于基础镜像并最终创建一个新的镜像。
    它们简化了从头到尾的流程并极大的简化了部署工作。Dockerfile从FROM命令开始，紧接着跟随者各种方法，命令和参数。
    其产出为一个新的可以用于创建容器的镜像。
``` 
   
#### Dockerfile基本结构
  - 1.文件的名称一定要为   Dockerfile
#### Dockerfile指令介绍   
  - 1.FROM 镜像 :
    指定新的镜像所基于的镜像
  - 2.MAINTAINER 名称 :
    说明新镜像的维护人
  - 3.RUN 命令 :
    在所基于的镜像上执行命令，并提交到新的镜像中
  - 4.CMD ["要运行的程序", "参数1", "参数2"]:
    指定启动容器时要运行命令或者脚本，只能有一条CMD命令
  - 5.EXPOSE 端口号 :
    指定新镜像加载到Docker时开启的端口
  - 6.EVN 环境变量 环境变量值：
    设置一个环境变量的值，会被后面的RUN使用
  - 7.ADD 源文件/目录 目标文件/目录:
    将源文件复制到目标文件，源文件要与Dockerfiel位于相同的目录中，或者是一个URL
  - 8.COPY 源文件/目录 目标文件/目录:
    将本地主机上的文件/目录复制到目标地点，源文件/目录要与Dockerfile在相同目录中
  - 9.VOLUME ["目录"]:
    在容器中创建一个挂载点
  - 10.USER 用户名/uid:
    指定运行容器时的用户
  - 11.WORKDIR
    为后续的RUN\CMD 指定工作目录
  - 12.ONBUILD 命令:
    指定所生成的镜像作为一个基础镜像时所要运行的命令
    
    
    
#### 基于Dockerfile创建镜像
  - 1.建立目录 : Dockerfile文件
  - 2.创建并编辑Dockerfile文件及需要运行的脚本文件、要复制到容器里的文件
  - 3.运行 docker build 生成镜像
    docker build -t 名称 .

  命令： docker build

eg:

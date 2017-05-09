#!/bin/bash

. `dirname $0`/functions.sh

## root启动判断
exit_root

##初始化
prepare_env
## 启动应用容器
if [ $# -eq 0 -o "$1" = "jetty" ];then
start_jetty
fi
## 启动apache
if [ $# -eq 0 -o "$1" = "apache" ];then
start_httpd
fi

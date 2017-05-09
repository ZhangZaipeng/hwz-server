#!/bin/bash

. `dirname $0`/functions.sh

##初始化
prepare_env
## 关闭apache
stop_httpd

stop_jetty

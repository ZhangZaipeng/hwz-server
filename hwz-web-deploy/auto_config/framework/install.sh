#!/bin/bash

DEPLOY_HOME=${deployhome}

if [ -f $DEPLOY_HOME/bin/killws.sh ]; then
	$DEPLOY_HOME/bin/killws.sh
fi
rm -rf $DEPLOY_HOME
unzip -d $DEPLOY_HOME web-deploy.zip 
bash -i -c "cd $DEPLOY_HOME"

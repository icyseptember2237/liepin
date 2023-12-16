#!/bin/bash

appname=$1

# 获取正在运行的 jar 包的 pid
pid=`ps -ef | grep web | grep 'java -jar' | grep -v 'nohup' | awk '{printf $2}'`

#空值判断
if [ -z $pid ];
	then
		echo "$appname not find"
	else
		# 结束进程 -9 无条件结束
		kill -9 $pid
		check=`ps -ef | grep -w $pid | grep java`
		if [ -z $check ];
			then
				echo "Failed to end $appname pid:$pid"
			else
				echo "$appname is killed"
		fi
fi
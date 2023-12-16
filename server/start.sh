BUILD_ID=DONTKILLME
#!/bin/bash

mv /root/.jenkins/workspace/liepin/web/target/web-1.0.0.jar /root/project
cd /root/project || return
source /etc/profile
nohup java -jar -Xmx1024M -Xms256M  web-1.0.0.jar --server.port=8080 >/root/project/log.log 2>&1 &
sleep 15s
pid=`ps -ef | grep web | grep 'java -jar' | grep -v 'nohup' | awk '{printf $2}'`
if [ -z $pid ];
then
    echo "project start successfully"
else
    echo "project start failed"
fi
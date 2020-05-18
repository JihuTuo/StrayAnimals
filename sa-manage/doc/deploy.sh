echo "Stopping beam-admin-0.0.1-SNAPSHOT.jar"
pid=`ps -ef | grep beam-admin-0.0.1-SNAPSHOT.jar | grep -v grep | awk '{print $2}'`
if [ -n "$pid" ]
then
   echo "kill -9 的pid:" $pid
   kill -9 $pid
fi
echo "stop finish"
nohup java -jar /usr/local/beam/beam-admin-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod >/dev/null 2>&1  &
echo "start run"
sleep 1
tail -f beam-admin-logs/log_total.log

#!/bin/sh
#
#Author: guodalu, Date: 2016/8/8
#
###################################
# chkconfig read settings
# chkconfig: - 99 50
# description: Java programs start script
# processname: test
# config: 
###################################
#
###################################
#java home
JAVA_HOME="/opt/programs/jdk1.8.0_111"

#app run user
RUNNING_USER=apprun

#app home
APP_HOME=$(cd `dirname $0`; cd ../ ; pwd)

#app name
APP_NAME='rigai-rigeye-monitor'

#app log dir
APP_LOG=$APP_HOME/logs/${APP_NAME}.log

#java main function
APP_MAINCLASS=com.em.App
APP_JAR=$APP_HOME/apps/$APP_NAME.jar

#java jvm set
#JAVA_OPTS=" -Xmx1G -Xms1G -server -XX:+UseG1GC -XX:MaxGCPauseMillis=20 -XX:InitiatingHeapOccupancyPercent=35 -XX:+DisableExplicitGC -Djava.awt.headless=true -XX:G1HeapRegionSize=2M -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+UnlockExperimentalVMOptions -XX:G1NewSizePercent=10 -XX:G1MaxNewSizePercent=75 -Dapollo.cluster=local -Dapp.name=$APP_NAME -cp $APP_HOME/config/:$APP_HOME/lib/* -Denv=dev -Ddev_meta=http://172.31.217.46:8080 "
JAVA_OPTS=" -Xmx1G -Xms1G -server -XX:+UseG1GC -XX:MaxGCPauseMillis=20 -XX:InitiatingHeapOccupancyPercent=35 -XX:+DisableExplicitGC -Djava.awt.headless=true -XX:G1HeapRegionSize=2M -verbose:gc   -XX:+UnlockExperimentalVMOptions -XX:G1NewSizePercent=10 -XX:G1MaxNewSizePercent=75 -Dapollo.cluster=local -Dapp.name=$APP_NAME -cp $APP_HOME/config/:$APP_HOME/lib/* -Denv=dev -Ddev_meta=http://172.31.217.46:8080 "


###################################
#check java app is running
###################################
psid=0

checkpid(){
    #javaps=`$JAVA_HOME/bin/jps -l | grep $APP_MAINCLASS`
	javaps=`$JAVA_HOME/bin/jps -l | grep $APP_JAR`
    if [ -n "$javaps" ];then
	psid=`echo $javaps | awk '{print $1}'`
    else
	psid=0
    fi
}

###################################
# start java app 
##################################

start(){
    checkpid
    if [ $psid -ne 0 ];then
	echo "================================"
	echo "warn: $APP_NAME already started! (pid=$psid)"
	echo "================================"
    else
	echo -n "Starting $APP_NAME ..."
        if [ `id -u` -eq 0 ]; then
	    JAVA_CMD="nohup $JAVA_HOME/bin/java $JAVA_OPTS -jar $APP_JAR >> $APP_LOG 2>&1 &"
	    su - $RUNNING_USER -c "$JAVA_CMD"
        elif [ `id -u` -eq 500 ]; then
            nohup $JAVA_HOME/bin/java $JAVA_OPTS -jar $APP_JAR >> $APP_LOG 2>&1 &
        else
            echo "unkonw running user!"
        fi

	checkpid
	if [ $psid -ne 0 ];then
	    echo "(pid=$psid) [OK]"
	else
	    echo "[Failed]"
	fi
    fi
}


##################################
# stop java app
##################################

stop(){
    checkpid
    if [ $psid -ne 0 ];then
	echo -n "Stopping $APP_NAME ...(pid=$psid) "
        if [ `id -u` -eq 0 ]; then
	    su - $RUNNING_USER -c "kill $psid"
        elif [ `id -u` -eq 3000 ]; then
            kill $psid
        else
            echo "unkonw running user!"
        fi

	if [ $? -eq 0 ];then
	    echo "[OK]"
	else
            if [ `id -u` -eq 0 ]; then
	        su - $RUNNING_USER -c "kill -9 $psid"
            elif [ `id -u` -eq 500 ]; then
                kill -9 $psid 
            else
                echo "unkonw running user!"
            fi

            sleep 5
            checkpid
	    if [ $psid -ne 0 ];then
		echo -n "[Failed]"	   
	    fi
	fi 
    else
	echo "================================"
	echo "warn: $APP_NAME is not running"
	echo "================================"
    fi
}

case "$1" in
    'start')
	start
	;;
    'stop')
	stop
	;;
    'restart')
	stop
	start
	;;
    *)
	echo "Usage: $0 {start|stop|restart}"
	exit 1
esac

exit 0


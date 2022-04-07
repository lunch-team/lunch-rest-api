# lunch-rest-api
"점심 뭐 먹지" 서비스의 Rest Api Repository

# Wiki
[Wiki](https://github.com/lunch-team/lunch-rest-api/wiki)

# Execute Shell
```shell
 HOME=[home]

 DEVPROP=application-lunch-dev.yml
 LOCPROP=application-lunch-local.yml
 DEVPID=$(pgrep -f $DEVPROP)
 LOCPID=$(pgrep -f $LOCPROP)

 if [ -z $DEVPID ]; then
     echo "> Dev Not Running"
 else
     echo "kill -15 $DEVPID"
     exit 0
 fi

 if [ -z $LOCPID  ]; then
     echo "> Local Not Running"
 else
     echo "kill -15 $LOCPID"
     exit 0
 fi

 echo "> Start Spring Server"
 JAR=[jar file.jar]

 nohup java -Dfile.encoding=UTF-8 -Dfile.client.encoding=UTF-8 -Dclient.encoding.override=UTF-8 -jar $HOME/$JAR --spring.config.location=file:$HOME/$DEVPROP > $HOME/logs/lunch-rest-dev.log 2>&1 &
 echo "> DEV RUN"

 sleep 3

 nohup java -Dfile.encoding=UTF-8 -Dfile.client.encoding=UTF-8 -Dclient.encoding.override=UTF-8 -jar $HOME/$JAR --spring.config.location=file:$HOME/$LOCPROP > $HOME/logs/lunch-rest-loc.log 2>&1 &
 echo "> LOC RUN"
```
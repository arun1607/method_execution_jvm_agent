#!/bin/sh

 # Determine location of this script
 script_dir=`dirname "$0"`
 if [ "$script_dir" = "." ] ; then
   script_dir=`pwd`
 fi

 app_home=`cd "$script_dir" ; pwd`

 # Setting the classpath - verified to work with spaces in path on Linux, OS X
 # class_path="$app_home/build/libs/application-all-1.0.0.jar"
 class_path=""
 for file in $app_home/lib/*.jar
 do
    class_path=$class_path:$file
 done

echo $class_path
java -noverify -javaagent:../agent/build/libs/agent-all-1.0.0.jar=graphite.host:localhost,graphite.port:2003,graphite.prefix:idfgateway -classpath $class_path com.learning.app.RunExample

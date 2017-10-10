#!/bin/bash
base_path=$(cd `dirname $0`; pwd);
pid_file="pid";
pid_total_path=${base_path}/${pid_file};
target_jar_path="wxsk-vr-mine-game-web/target/mine-game.jar";
target_log_path="mine-game.log";

environment="dev";
is_no_hup=true;

echo "startup pid: $$";

#判断环境
if [ ! -z $1 ];
    then
        if [ $1 == "local" ];then environment="local";
        elif [ $1 == "dev" ];then environment="dev";
        elif [ $1 == "qa" ];then environment="qa";
        elif [ $1 == "prod" ];then environment="prod";
        fi
fi

echo "using environment: ${environment}";

#mavn 打包
echo "application is going to start up: mvn clean install....";
mvn clean install -P ${environment}>${base_path}/${target_log_path} 2>&1;

#启动项目
nohup java -jar ${base_path}/${target_jar_path}>${base_path}/${target_log_path} 2>&1 &

#存储pid
if [ ! -f ${pid_total_path} ]; then
	echo "pid文件不存在,准备创建新pid文件: ${pid_total_path}";
	touch ${pid_total_path};
fi
echo $!>${pid_total_path};
echo "application has ben started up successfully!";

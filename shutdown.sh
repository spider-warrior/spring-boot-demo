#!/bin/bash
base_path=$(cd `dirname $0`; pwd);
pid_file="pid";
pid_total_path=${base_path}/${pid_file};
#查找pid
if [[ ! -f "$pid_total_path" ]]; then
	echo "pid文件不存在: $pid_total_path";
	exit 0;
fi
line_count=`cat ${pid_total_path} |wc -l`;
if [[ ${line_count} -lt 1 ]]; then
    echo "pid文件中未找到pid";
    exit 0;
fi;
pid=`head -1 ${pid_total_path}`;
if [[ ! -z pid ]];then
    echo "find pid: ${pid}, killing it";
    kill ${pid};
else
    echo "pid文件中不存在pid";
fi


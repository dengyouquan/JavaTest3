# 运行流程

## 编译docker

sh build.sh

## 运行

sh start.sh

不知道docker-compose哪里有问题，mysql能正常启动，成功导入数据，不能启动java，只能启动mysql，java报一个com.mysql.jdbc.exceptions.jdbc4.CommunicationsException: Communications link failure

对于日志，sh start.sh不能完全打印，只能打印一部分(启动时间问题)

## 清理

sh clear.sh

清理所有环境


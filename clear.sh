docker-compose down
#docker stop javatest3_mysql_1
#docker stop javatest3_jdk_1
#docker rm javatest3_mysql_1
#docker rm javatest3_jdk_1
docker rmi mysql
docker rmi java
docker rmi $(docker images -q -f dangling=true)
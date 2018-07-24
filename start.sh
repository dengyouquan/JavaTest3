docker-compose up -d;
echo ---------------------mysqllog------------------------------
echo please wait 20s,we are importing data to mysql,we will print complete mysql log after 20s.
sleep 20
docker logs javatest3_mysql_1;
echo --------------------jdklog------------------------------------
#echo please wait 30s,because we are importing data to mysql,jdk will don,t connect to data server.
#sleep 30
docker logs javatest3_jdk_1;
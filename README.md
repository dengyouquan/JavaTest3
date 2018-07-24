# 运行流程

## 编译docker

sh build.sh

## 运行

sh start.sh

## 清理

sh clear.sh

清理所有环境

# 解析

对于昨天的考试，需要

1. 项目根目录下需要docker/mysql/Dockerfile 文件用于构建mysql镜像
2. 项目根目录下需要docker/java/Dockerfile 文件用于构建项目镜像
3. 根目录下需要有docker-compose.yml文件，用于启动mysql镜像和项目镜像
4. 根目录下需要有build.sh 用于编译maven项目和构建镜像。
5. 根目录下需要有start.sh启动docker-compose 容器。需要控制台运行，打印出结果。
6. 数据库连接信息需要从java 的环境变量中读取

## build.sh

首先我们应该有一个可运行的实际的项目（项目名为JavaTest3，子项目为Exam1），然后我们将在根目录下建一个build.sh文件，用于项目的编译和将jar包cp到/docker/mysql下,我们将用assembly将第三方依赖库打入一个独立jar包，下面有docker build指令，所以我们接下来将会去看一下这些Dockerfile文件将是怎么构建的。

```shell
cd Exam1;
mvn clean;
mvn assembly:assembly;
cp target/app-jar-with-dependencies.jar ../docker/java/app.jar
cd ../docker/mysql;
docker build -t mysql .;
cd ../java;
docker build -t java .;
```

docker/mysql/用于mysql的构建目录，里面有Dockerfile用于构建镜像，sakila-schema.sql建表sql，sakila-data.sql导入数据sql，setup.sh用于整个项目的运行，privileges.sql设置权限sql

首先看Dockerfile，可以看出我们用可一个mysql5.6基础镜像，然后copy一些文件到容器中，因为在Docker镜像构建中，每多一个COPY会多一层镜像，会占用空间，我们优化了一下代码，只用一个COPY，然后用sh /mysql/setup.sh进行启动，我将会在setup.sh中进行一些sql数据导入，权限设置等操作。

```shell
FROM registry.saas.hand-china.com/tools/mysql:5.6

#设置免密登录
ENV MYSQL_ALLOW_EMPTY_PASSWORD yes

#将所需文件放到容器中
#COPY setup.sh /mysql/setup.sh
#COPY sakila-schema.sql /mysql/sakila-schema.sql
#COPY sakila-data.sql /mysql/sakila-data.sql
#COPY privileges.sql /mysql/privileges.sql
COPY . /mysql/

#设置容器启动时执行的命令
CMD ["sh", "/mysql/setup.sh"]
```

接下来看一下setup.sh我到底做了什么.首先在里面启动mysql服务，导入数据，修改权限，最后用tail -f /dev/null接受输入信息但是什么都不做,不加这个mysql会停止运行，这样我们可以让mysql一直运行，模拟后台运行的效果。

```shell
#!/bin/bash
#告诉bash如果任何语句的执行结果不是true则应该退出防止错误像滚雪球般变大导致一个致命的错误
set -e

#查看mysql服务的状态，方便调试，这条语句可以删除
echo `service mysql status`

echo '1.start mysql....'
#启动mysql
service mysql start
sleep 3
echo `service mysql status`

echo '2.start import data....'
#导入数据
mysql < /mysql/sakila-schema.sql
mysql < /mysql/sakila-data.sql
echo '3.import data complete....'

sleep 3
echo `service mysql status`

#重新设置mysql密码
echo '4.start modify password....'
mysql < /mysql/privileges.sql
echo '5.modify password complete....'

#sleep 3
echo `service mysql status`
echo 'mysql is running'

#可以接受输入信息但是什么都不做,不加这个mysql会停止运行
tail -f /dev/null
```

sakila-schema.sql sakila-data.sql主要是sql数据，我们不再分析。

接下来是privileges.sql设置权限，因为我们在setup.sh设置了无密码登录，这是不对的，我们在这里面将为mysql设置权限,新建一个用户为用户授权，记得root@'%'是除localhost之外的所有网授权，*.*是对任意数据库的任意表搜权。

```sql
use mysql;
select host, user from user;
-- 因为mysql版本是5.7，因此新建用户为如下命令：
create user root identified by '123456';
-- 将docker_mysql数据库的权限授权给创建的docker用户，密码为123456：
grant all on *.* to root@'%' identified by '123456' with grant option;
-- 这一条命令一定要有：
flush privileges;
```

接下来，我们去看一下/docker/java中我们项目镜像怎么构建的，这个目录下有Dockerfile用于构建镜像，app.jar是我们在build.sh中cp过来的，我们将在docker中运行这个jar包,我们不会通过ENTRYPOINT ["java","-jar","app.jar"]直接启动项目，因为我们的mysql导入数据需要时间，怎么办呢，我们将在run.sh中延迟20s运行jar包这样就没有问题了。

```shell
FROM registry.saas.hand-china.com/hap-cloud/base:latest

#COPY app-jar-with-dependencies.jar app.jar
#COPY run.sh run.sh

COPY . .

#ENTRYPOINT ["java","-jar","app.jar"]
CMD ["sh","run.sh"]
```

接下来我们看一下run.sh,睡眠15秒再执行jar包

```shell
#!/bin/bash
set -e

sleep 15

echo 'start run app.jar'
java -jar app.jar

#tail -f /dev/null
```

## start.sh

以上通过指令通过build.sh会自动执行构建操作，结果是两个可运行的docker镜像，接下来我们通过docker-compose.yml运行我们的项目，然后我们用sh start.sh就可以完整显示结果

在docker-compose.yml文件中，我们配置了mysql的属性，项目镜像的属性和环境变量

```shell
version: '2'
services:
  mysql:
    image: mysql
    ports:
      - "3306:3306"
    environment:
      - MYSQL_ROOT_PASSWORD=123456
    container_name: javatest3_mysql_1

  jdk:
    image: java
    depends_on:
      - mysql
    environment:
      - name=root
      - pwd=123456
      - ip=192.168.99.100
      - port=3306
      - dbname=sakila
      - country_id=2
      - customer_id=1
    container_name: javatest3_jdk_1
```

在start.sh中首相我们以后台方式启动docker-compose，不然命令行会被占用，不能敲其他命令，因为我们数据有延迟导入，我们的日志要等20s才打印，这样的日志才是完全的。

```shell
docker-compose up -d;
echo ---------------------mysqllog------------------------------
echo please wait 20s,we are importing data to mysql,we will print complete mysql log after 20s.
sleep 20
docker logs javatest3_mysql_1;
echo --------------------jdklog------------------------------------
#echo please wait 30s,because we are importing data to mysql,jdk will don,t connect to data server.
#sleep 30
docker logs javatest3_jdk_1;
```

然后你可以看你的结果，如果想关闭这个项目，有clear.sh,清除不需要的容器和镜像

```shell
docker stop javatest3_mysql_1
docker stop javatest3_jdk_1
docker rm javatest3_mysql_1
docker rm javatest3_jdk_1
docker rmi mysql
docker rmi java
docker rmi $(docker images -q -f dangling=true)
```

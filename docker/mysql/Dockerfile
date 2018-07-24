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
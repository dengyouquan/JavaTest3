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
# backend-lender

## COMPILE FOR UAT
>>>
```shell
mvn clean install
```
>>>

## COMPILE FOR PRODUCTION
>>>
```shell
mvn clean install -Pprod
```
>>>

## DEPLOY TO UAT
>>>
```shell
aliyun oss cp ms-p2p-order/target/output/lib/ms-*.jar oss://doit-build-5263863254564512/p2p-order/lib/
aliyun oss cp ms-p2p-order/target/output/ms-p2p-order-0.0.2-SNAPSHOT.jar oss://doit-build-5263863254564512/p2p-order/

aliyun oss cp ms-p2p-system/target/output/lib/ms-*.jar oss://doit-build-5263863254564512/p2p-system/lib/
aliyun oss cp ms-p2p-system/target/output/ms-p2p-system-0.0.2-SNAPSHOT.jar oss://doit-build-5263863254564512/p2p-system/

aliyun oss cp ms-p2p-user/target/output/lib/ms-*.jar oss://doit-build-5263863254564512/p2p-user/lib
aliyun oss cp ms-p2p-user/target/output/ms-p2p-user-0.0.2-SNAPSHOT.jar oss://doit-build-5263863254564512/p2p-user/

aliyun oss cp ms-p2p-upload/target/output/lib/ms-*.jar* oss://doit-build-5263863254564512/p2p-upload/lib/
aliyun oss cp ms-p2p-upload/target/output/ms-p2p-upload-0.0.2-SNAPSHOT.jar oss://doit-build-5263863254564512/p2p-upload/

aliyun oss cp ms-p2p-pay/target/output/lib/ms-*.jar oss://doit-build-5263863254564512/p2p-pay/lib/
aliyun oss cp msaliyun oss cp ms-p2p-pay/target/output/ms-p2p-pay-0.0.2-SNAPSHOT.jar oss://doit-build-5263863254564512/p2p-pay/

aliyun oss cp ms-p2p-task/target/output/lib/ms-*.jar oss://doit-build-5263863254564512/p2p-task/lib
aliyun oss cp ms-p2p-task/target/output/ms-p2p-task-0.0.2-SNAPSHOT.jar oss://doit-build-5263863254564512/p2p-task/
```
>>>

## DEPLOY THIRD PARTY DEPENDENCY LIBRARIES
>>>
1. Need to manual deploy to server
```shell
scp ms-p2p-order/target/output/lib/xxx.jar test_java04:/home/p2p-order/lib/
scp ms-p2p-system/target/output/lib/xxx.jar test_java04:/home/p2p-system/lib/
scp ms-p2p-user/target/output/lib/xxx.jar test_java04:/home/p2p-user/lib/
scp ms-p2p-upload/target/output/lib/xxx.jar test_java04:/home/p2p-upload/lib/
scp ms-p2p-pay/target/output/lib/xxx.jar test_java04:/home/p2p-pay/lib/
scp ms-p2p-task/target/output/lib/xxx.jar test_java04:/home/p2p-task/lib/
```
>>>

## RUN MANUALLY IN LOCAL
>>>
```shell
java -Xmx1024m -Xms1024m -Xmn512m -Xss1m -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+PrintGCDetails -Xloggc:gc.log -jar -Dserver.port=9010 -Dspring.profiles.active=test2 ms-p2p-user/target/output/ms-p2p-user-0.0.2-SNAPSHOT.jar

java -Xmx1024m -Xms1024m -Xmn512m -Xss1m -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+PrintGCDetails -Xloggc:gc.log -jar -Dserver.port=9020 -Dspring.profiles.active=test2 ms-p2p-order/target/output/ms-p2p-order-0.0.2-SNAPSHOT.jar

java -Xmx1024m -Xms1024m -Xmn512m -Xss1m -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+PrintGCDetails -Xloggc:gc.log -jar -Dserver.port=9030 -Dspring.profiles.active=test2 ms-p2p-system/target/output/ms-p2p-system-0.0.2-SNAPSHOT.jar

java -Xmx1024m -Xms1024m -Xmn512m -Xss1m -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+PrintGCDetails -Xloggc:gc.log -jar -Dserver.port=9031 -Dspring.profiles.active=test2 ms-p2p-upload/target/output/ms-p2p-upload-0.0.2-SNAPSHOT.jar

java -Xmx1024m -Xms1024m -Xmn512m -Xss1m -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+PrintGCDetails -Xloggc:gc.log -jar -Dserver.port=9040 -Dspring.profiles.active=test2 ms-p2p-pay/target/output/ms-p2p-pay-0.0.2-SNAPSHOT.jar

java -Xmx1024m -Xms1024m -Xmn512m -Xss1m -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+PrintGCDetails -Xloggc:gc.log -jar -Dserver.port=9090 -Dspring.profiles.active=test2 ms-p2p-task/target/output/ms-p2p-task-0.0.2-SNAPSHOT.jar
```
>>>
# backend-lender

## DEPLOY CODE ONLY
>>>
* aliyun oss cp ms-p2p-order/target/output/ms-p2p-order-0.0.2-SNAPSHOT.jar oss://doit-build-5263863254564512/p2p-order/ms-p2p-order-0.0.2-SNAPSHOT.jar
* aliyun oss cp ms-p2p-system/target/output/ms-p2p-system-0.0.2-SNAPSHOT.jar oss://doit-build-5263863254564512/p2p-system/ms-p2p-system-0.0.2-SNAPSHOT.jar
* aliyun oss cp ms-p2p-user/target/output/ms-p2p-user-0.0.2-SNAPSHOT.jar oss://doit-build-5263863254564512/p2p-user/ms-p2p-user-0.0.2-SNAPSHOT.jar
* aliyun oss cp ms-p2p-upload/target/output/ms-p2p-upload-0.0.2-SNAPSHOT.jar oss://doit-build-5263863254564512/p2p-upload/ms-p2p-upload-0.0.2-SNAPSHOT.jar
* aliyun oss cp ms-p2p-pay/target/output/ms-p2p-pay-0.0.2-SNAPSHOT.jar oss://doit-build-5263863254564512/p2p-pay/ms-p2p-pay-0.0.2-SNAPSHOT.jar
* aliyun oss cp ms-p2p-task/target/output/ms-p2p-task-0.0.2-SNAPSHOT.jar oss://doit-build-5263863254564512/p2p-task/ms-p2p-task-0.0.2-SNAPSHOT.jar
>>>

## DEPLOY DEPENDENCY LIBRARIES
>>>
scp -rf ms-p2p-order/target/output/lib/ test_java04:/home/p2p-order/lib/
scp -rf ms-p2p-system/target/output/lib/ test_java04:/home/p2p-system/lib/
scp -rf ms-p2p-user/target/output/lib/ test_java04:/home/p2p-user/lib/
scp -rf ms-p2p-upload/target/output/lib/ test_java04:/home/p2p-upload/lib/
scp -rf ms-p2p-pay/target/output/lib/ test_java04:/home/p2p-pay/lib/
scp -rf ms-p2p-task/target/output/lib/ test_java04:/home/p2p-task/lib/
>>>

## RUN MANUALLY IN LOCAL
>>>
* java -Xmx1024m -Xms1024m -Xmn512m -Xss1m -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+PrintGCDetails -Xloggc:gc.log -jar -Dserver.port=9010 -Dspring.profiles.active=test2 ms-p2p-user/target/output/ms-p2p-user-0.0.2-SNAPSHOT.jar
* java -Xmx1024m -Xms1024m -Xmn512m -Xss1m -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+PrintGCDetails -Xloggc:gc.log -jar -Dserver.port=9020 -Dspring.profiles.active=test2 ms-p2p-order/target/output/ms-p2p-order-0.0.2-SNAPSHOT.jar
* java -Xmx1024m -Xms1024m -Xmn512m -Xss1m -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+PrintGCDetails -Xloggc:gc.log -jar -Dserver.port=9030 -Dspring.profiles.active=test2 ms-p2p-system/target/output/ms-p2p-system-0.0.2-SNAPSHOT.jar
* java -Xmx1024m -Xms1024m -Xmn512m -Xss1m -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+PrintGCDetails -Xloggc:gc.log -jar -Dserver.port=9031 -Dspring.profiles.active=test2 ms-p2p-upload/target/output/ms-p2p-upload-0.0.2-SNAPSHOT.jar
* java -Xmx1024m -Xms1024m -Xmn512m -Xss1m -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+PrintGCDetails -Xloggc:gc.log -jar -Dserver.port=9040 -Dspring.profiles.active=test2 ms-p2p-pay/target/output/ms-p2p-pay-0.0.2-SNAPSHOT.jar
* java -Xmx1024m -Xms1024m -Xmn512m -Xss1m -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+PrintGCDetails -Xloggc:gc.log -jar -Dserver.port=9090 -Dspring.profiles.active=test2 ms-p2p-task/target/output/ms-p2p-task-0.0.2-SNAPSHOT.jar
>>>
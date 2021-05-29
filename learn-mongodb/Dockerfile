# 该镜像需要依赖的基础镜像
FROM openjdk:8
#作者
MAINTAINER gaoh <1662143230@qq.com>

VOLUME /tmp
#用于设置环境变量，格式：
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8
# 将当前目录下的jar包复制到docker容器的/目录下
ADD target/learn-mongodb-1.0.1.jar /learn-mongodb.jar
# 声明服务运行在8080端口
EXPOSE 8081
# 指定docker容器启动时运行jar包
ENTRYPOINT ["java","-jar","/learn-mongodb.jar"]
# 指定维护者的名字
MAINTAINER gaoh

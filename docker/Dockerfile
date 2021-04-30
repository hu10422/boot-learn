FROM openjdk:8

#作者
MAINTAINER gaoh <1662143230@qq.com>

ENV LANG=C.UTF-8 LC_ALL=C.UTF-8

VOLUME /tmp

ADD ../target/boot-learn-0.0.1-SNAPSHOT.jar boot-learn.jar

ENTRYPOINT ["java","-jar","/boot-learn.jar"]

EXPOSE 8081
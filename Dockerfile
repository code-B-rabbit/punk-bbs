FROM java:8

COPY *.jar /app.jar

CMD ["--server.port=80"]

VOLUME /tmp

RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

EXPOSE 80

ENTRYPOINT ["java","-jar","/app.jar"]
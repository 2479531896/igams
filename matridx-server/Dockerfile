FROM java:8
VOLUME /tmp
ADD matridx-server.jar matridx-server.jar
ADD 2563135_service.matridx.com.pfx 2563135_service.matridx.com.pfx
ADD 2sEDHIe3j2.txt 2sEDHIe3j2.txt
RUN bash -c 'touch /matridx-server.jar'
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","-Denv=DEV","/matridx-server.jar","--spring.profiles.active=prod","-Djava.io.tmpdir=/matridx/Temp"]
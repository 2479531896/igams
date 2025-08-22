FROM java:8
VOLUME /tmp
ADD igams-web.jar igams.jar
ADD jacob-1.18-x64.dll /usr/lib/jvm/java-8-openjdk-amd64/bin/
RUN bash -c 'touch /igams.jar'
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","-Denv=DEV","/igams.jar","--spring.profiles.active=prod","-Djava.io.tmpdir=/matridx/Temp"]
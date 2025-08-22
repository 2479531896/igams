FROM java:8
VOLUME /tmp
ADD igams-exec.jar igams-production.jar
RUN bash -c 'touch /igams-production.jar'
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","-Denv=DEV","/igams-production.jar","--port=8090","--mqhost=172.17.60.192","--mqname=admin","--mqpass=matridx2019!","--mqok=production.mq.tran.matridx.ok","--name=igams-production","--profile=dev","uri:http://172.17.60.192:8860","--zone=http://matridx:matridx2019!@172.17.60.192:8851/eureka/","--ipaddr=172.17.60.193","-Djava.io.tmpdir=/matridx/Temp"]
FROM tomcat:alpine

ENV DOCKER="True"
ENV METADATA_STORAGE_SERVER_URL="http://192.168.1.192:8061"

RUN rm -rf /usr/local/tomcat/webapps/*
ADD target/deployment-manager.war /usr/local/tomcat/webapps/ROOT.war

CMD ["catalina.sh", "run"]

#FROM jboss/wildfly
#
#ADD target/deployment-manager.war /opt/jboss/wildfly/standalone/deployments/
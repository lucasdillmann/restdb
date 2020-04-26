FROM openjdk:11-jre-slim
ARG DIST_FILE
ARG APPLICATION_VERSION
ENV RESTDB_SERVER_HOST 0.0.0.0
ENV RESTDB_SERVER_PORT 80
EXPOSE 80
ENTRYPOINT ["/bin/bash", "/opt/restdb/bin/restdb"]
ENV RESTDB_APPLICATION_VERSION ${APPLICATION_VERSION}
ADD ${DIST_FILE} /opt/
RUN find /opt/ -depth -type d -name 'restdb-*' -exec ln -s {} /opt/restdb \;
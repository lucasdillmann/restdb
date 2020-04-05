FROM openjdk:11-jre-slim
ARG DIST_FILE
ADD ${DIST_FILE} /opt/
ENV RESTDB_SERVER_HOST 0.0.0.0
ENV RESTDB_SERVER_PORT 80
EXPOSE 80
ENTRYPOINT ["/bin/bash", "/opt/restdb/bin/restdb"]
RUN find /opt/ -depth -type d -name 'restdb-*' -exec ln -s {} /opt/restdb \;
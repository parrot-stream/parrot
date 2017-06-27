FROM mcapitanio/wildfly:10.1.0.Final

MAINTAINER Matteo Capitanio <matteo.capitanio@gmail.com>

USER root

ENV PARROT_HOME /opt/parrot
ENV PATH $PATH:$PARROT_HOME/docker

WORKDIR $PARROT_HOME

COPY docker/etc/ /etc/

COPY ./ ./
COPY docker/wildfly/standalone.xml $WILDFLY_HOME/standalone/configuration
COPY docker/wildfly/standalone.conf $WILDFLY_HOME/bin

# Add a user in administration realm
RUN $WILDFLY_HOME/bin/add-user.sh parrot parrot --silent

RUN find docker -name '*.sh' -exec chmod +x {} \;
RUN find docker -name '*.sh' -exec dos2unix {} \;

ENTRYPOINT ["supervisord", "-c", "/etc/supervisord.conf", "-n"]
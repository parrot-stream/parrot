FROM mcapitanio/kafka:0.10.2.0

MAINTAINER Matteo Capitanio <matteo.capitanio@gmail.com>

COPY docker/etc/ /etc/

RUN find docker -name '*.sh' -exec chmod +x {} \;
RUN find docker -name '*.sh' -exec dos2unix {} \;

ENTRYPOINT ["supervisord", "-c", "/etc/supervisord.conf", "-n"]
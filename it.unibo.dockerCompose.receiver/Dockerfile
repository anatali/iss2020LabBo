## ----------------------------------------------------------
## Dockerfile for dcreceiver
## ----------------------------------------------------------
## see https://hub.docker.com/r/amd64/openjdk/
FROM openjdk:12.0.2
EXPOSE 8037
## ADD extracts the tar
ADD ./build/distributions/*.tar /
WORKDIR /it.unibo.dockerCompose.receiver-1.0/bin
COPY ./*.pl ./
CMD ["bash", "it.unibo.dockerCompose.receiver"]

##  docker build -t dcreceiver . 
##  docker run -it --name dcr -p8037:8037/tcp -p8037:8037/udp dcreceiver  /bin/bash	(OLD)

##  docker run --name dcr -p8037:8037/tcp -p8037:8037/udp dcreceiver   
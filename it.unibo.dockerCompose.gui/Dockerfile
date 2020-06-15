## ----------------------------------------------------------
## Dockerfile for gui
## ----------------------------------------------------------
## see https://hub.docker.com/r/amd64/openjdk/
FROM openjdk:12.0.2
 
## ADD extracts the tar
ADD ./build/distributions/*.tar /
WORKDIR /it.unibo.dockerCompose.gui-1.0/bin

CMD ["bash", "it.unibo.dockerCompose.gui"]

##  docker build -t dcreceiver . 
##  docker run -it --name dcr -p8037:8037/tcp -p8037:8037/udp dcreceiver  /bin/bash	(OLD)

##  docker run --name dcr -p8037:8037/tcp -p8037:8037/udp dcreceiver   
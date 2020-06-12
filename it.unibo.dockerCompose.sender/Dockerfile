## ----------------------------------------------------------
## Dockerfile for dcsender
## ----------------------------------------------------------
## see https://hub.docker.com/r/amd64/openjdk/
FROM openjdk:12.0.2  
EXPOSE 8039
## ADD extracts the tar
ADD ./build/distributions/*.tar /
WORKDIR /it.unibo.dockerCompose.sender-1.0/bin
COPY ./*.pl ./
CMD ["bash", "it.unibo.dockerCompose.sender"]

##  docker build -t dcsender . 													CREATE IMAGE
##  docker run -it --name dcs -p8039:8039/tcp -p8039:8039/udp dcsender 	bash 	RUN CONTAINER (OLD)

##  docker run --name dcs -p8039:8039/tcp -p8039:8039/udp dcsender 				RUN CONTAINER 
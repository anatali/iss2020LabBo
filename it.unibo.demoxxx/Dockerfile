FROM openjdk:12.0.2
EXPOSE 8080
ADD ./build/distributions/*.tar /
ADD ./*.pl /
RUN ls /it.unibo.demoxxx-1.0/bin
ENTRYPOINT ["bash", "/it.unibo.demoxxx-1.0/bin/it.unibo.demoxxx"]

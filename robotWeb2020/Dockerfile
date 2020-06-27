## ------------------------------------------------------- 
## Dockerfile for frontend  
## ------------------------------------------------------- 
FROM openjdk:12.0.2

EXPOSE 8080

ADD ./build/distributions/robotWeb2020-1.0.tar / 
ADD ./build/distributions/robotWeb2020-boot-1.0.tar / 
WORKDIR robotWeb2020-boot-1.0/bin
COPY ./*.json ./
RUN pwd
RUN ls  
CMD ["bash","robotWeb2020"]

# ENTRYPOINT ["echo","docker exec -ti robotWeb2020-boot-1.0/bin/robotWeb2020 /bash"]

## docker build -t frontendqak20basicrobot .
## docker run -it --name brfe  -p8080:8080  -e "SPRING_PROFILES_ACTIVE=docker" frontendqak20basicrobot 

## docker run -it --name brfe  -p8080:8080   frontendqak20basicrobot  /bin/bash  ## MAGGIOR CONTROLLO
## docker run -it --name brfe  -p7001:7001   frontendqak20basicrobot  /bin/bash  ## MAGGIOR CONTROLLO
## cat pageConfig.json
## bash robotWeb2020



## docker cp C:\Didattica2018Work\iss2020LabBo\robotWeb2020\src\main\resources\connQak\mypageConfig.json brfe:/xxx.json
## 

FROM openjdk:12.0.2

EXPOSE 8080

ADD ./build/distributions/robotWeb2020-1.0.tar / 
ADD ./build/distributions/robotWeb2020-boot-1.0.tar / 
# ADD ./*.json /robotWeb2020-1.0/bin/
ADD ./*.json /robotWeb2020-boot-1.0/bin/
ADD ./*.json / 
RUN ls robotWeb2020-1.0/bin
RUN ls robotWeb2020-boot-1.0/bin
# RUN ls robotWeb2020-1.0/lib
CMD ["bash","robotWeb2020-boot-1.0/bin/robotWeb2020"]
# ENTRYPOINT ["echo","docker exec -ti robotWeb2020-boot-1.0/bin/robotWeb2020 /bash"]

## docker build -t frontendqak20basicrobot .
## docker run -it --name brfe  -p8080:8080  -e "SPRING_PROFILES_ACTIVE=docker" frontendqak20basicrobot 

## docker run -it --name brfe  -p8080:8080   frontendqak20basicrobot  /bin/bash

## docker run  --name brfe  -p8080:8080   frontendqak20basicrobot  


## docker cp C:\Didattica2018Work\iss2020LabBo\robotWeb2020\src\main\resources\connQak\mypageConfig.json brfe:/xxx.json
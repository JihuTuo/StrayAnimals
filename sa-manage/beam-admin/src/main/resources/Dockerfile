FROM java:8
EXPOSE 8080
VOLUME /tmp
ADD beam-admin.jar /beam-admin.jar
RUN bash -c 'touch /beam-admin.jar'
ENTRYPOINT ["java","-jar","/beam-admin.jar","--spring.profiles.active=prod"]

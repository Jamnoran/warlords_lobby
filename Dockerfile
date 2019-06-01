FROM anapsix/alpine-java

COPY build/libs/warlords_lobby.jar /home/warlords_lobby.jar

EXPOSE 2075

CMD java -jar /home/warlords_lobby.jar

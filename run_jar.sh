#! /bin/sh

mkdir -p ../lobby
cp -r build/* ../lobby

#cp main.properties ../lobby/classes/main/main.properties
echo "app.port=666" > ../lobby/classes/main/main.properties


echo "#! /bin/sh
java -jar artifacts/warlords_lobby_jar/warlords_lobby.jar" > ../lobby/run.sh

java -jar ../lobby/artifacts/warlords_lobby_jar/warlords_lobby.jar
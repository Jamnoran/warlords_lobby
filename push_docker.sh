#!/usr/bin/env bash

gradle clean

gradle jar

echo "Built Jar file"

docker build -t warlords_lobby .

docker tag warlords_lobby jamnoran/warlords_lobby

docker push jamnoran/warlords_lobby

echo "Pushed lobby"

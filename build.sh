#!/bin/bash
#
# This script will build the hole application and
# creates a directory for the ResourceHub.

# clear current build
rm -r build/
mvn clean install -fae
mkdir build/

# copy Desktop-Client
cp -r Desktop-Client/ClientAgent/target/ClientAgent-*-with-dependencies.jar build/
mv build/ClientAgent-*-with-dependencies.jar build/Desktop-Client.jar
chmod +x build/Desktop-Client.jar

# copy ResouceHub
mkdir build/ResourceHub/
mkdir build/ResourceHub/secrets/
cp -r ResourceHub/target/ResourceHub-*-with-dependencies.jar build/ResourceHub/
cp -r ResourceHub/secrets/* build/ResourceHub/secrets/
mv build/ResourceHub/ResourceHub-*-with-dependencies.jar build/ResourceHub/ResourceHub.jar
chmod +x build/ResourceHub/ResourceHub.jar

# copy DummyClient
cp -r DummyClient/target/DummyClient-*-with-dependencies.jar build/ResourceHub/
mv build/ResourceHub/DummyClient-*-with-dependencies.jar build/ResourceHub/DummyClient.jar
chmod +x build/ResourceHub/DummyClient.jar

# !/bin/sh

# Installation script for the resource hub with overwriting the /etc/network/interfaces.
# This script has to run with sudo.

echo 'start setup...'

echo 'installing JRE'
apt-add-repository ppa:webupd8team/java
apt-get update
apt-get install oracle-java8-installer

echo 'installing mysql-server'
apt-get install mysql-server

echo 'installing Maven'
apt-get purge maven maven2 maven3
add-apt-repository ppa:andrei-pozolotin/maven3
apt-get update
apt-get install maven3

echo 'setup finished'

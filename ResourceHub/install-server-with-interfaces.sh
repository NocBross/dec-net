# !/bin/sh

# Installation script for the resource hub with overwriting the /etc/network/interfaces.
# This script has to run with sudo and you have to give the last number for the
# address as parameter e.g. you can call
# 	sudo ./install-server-with-interfaces 1
# to insert the address 10.0.2.101 .

interfacesPath=/etc/network/interfaces

echo 'start setup...'

# overwriting the /etc/network/interfaces file
echo 'overwriting /etc/network/interfaces'
rm -r $interfacesPath
touch $interfacesPath
echo '# This file describes the network interfaces available on your system' >> $interfacesPath
echo '# and how to activate them. For more information, see interfaces(5).' >> $interfacesPath
echo '\n' >> $interfacesPath
echo '# The loopback network interface' >> $interfacesPath
echo 'auto lo' >> $interfacesPath
echo 'iface lo inet loopback' >> $interfacesPath
echo '\n' >> $interfacesPath
echo '# The primary network interface' >> $interfacesPath
echo 'auto eth0' >> $interfacesPath
echo 'iface eth0 inet static' >> $interfacesPath
echo 'address 10.0.2.10'$1 >> $interfacesPath
echo 'netmask 255.255.255.0' >> $interfacesPath
echo 'network 10.0.2.0' >> $interfacesPath
echo 'broadcast 10.0.2.255' >> $interfacesPath
echo 'gateway 10.0.2.254' >> $interfacesPath

echo 'installing JRE'
apt-add-repository ppa:webupd8team/java
apt-get update
apt-get install oracle-java8-installer

echo 'installing mysql-server'
apt-get install mysql-server

echo 'setup finished'

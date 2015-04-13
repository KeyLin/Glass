#bin/bash

myDir="/tmp/voiceRecognition"
if [ ! -d "$myDir" ]; then
	sudo mkdir "$myDir"
fi

cd "$myDir"

declare -a package
declare -a module

package[0]=bison
package[1]=swig
# package[2]=sphinxbase-utils

# package[3]=libpocketsphinx-dev
# package[4]=libpocketsphinx1

# package[5]=python-sphinxbase
# package[6]=python-pocketsphinx
# python-pocketsphinx-dbg 

# package[7]=pocketsphinx-lm-zh-hans-gigatdt 
module[0]=pocketsphinx

function PackageInstall()
{
	#echo $1
	dpkg -l | grep $1  > /dev/null
	if [ $? -eq 0 ]; then
		echo "$1 already exit"
	else
		sudo apt-get install -y $1 || { echo "$1 install failed"; exit 1; } 
		echo "$1 successfully installed"
	fi
	return 0;
}

function ModuleInstall()
{
	pip freeze | grep $1  > /dev/null
	if [ $? -eq 0 ]; then
		echo "$1 already exit"
	else
		sudo pip install $1  --allow-external $1 --allow-unverified $1 || { echo "$1 install failed"; exit 1; }
		echo "$1 successfully installed"
	fi
}

for ((i=0;i<${#package[@]};i++));
	do
		#echo ${package[i]}
		PackageInstall ${package[i]}
	done 

pkg-config --list-all | grep sphinxbase > /dev/null
if [ $? -eq 1 ]; then
    myFile0="./sphinxbase-5prealpha.tar.gz"
    myDir0="./sphinxbase-5prealpha"
    if [ ! -f "$myFile0" ]; then
    	wget http://jaist.dl.sourceforge.net/project/cmusphinx/sphinxbase/5prealpha/sphinxbase-5prealpha.tar.gz
    fi
    if [ ! -d "$myDir0" ]; then
    	tar -xzvf sphinxbase-5prealpha.tar.gz
    fi
    ./sphinxbase-5prealpha/configure --enable-fixed&&make&&sudo make install || { echo "sphinxbase install failed"; exit 1; }
    echo "sphinxbase successfully installed"
else echo "sphinxbase already exit "
fi

pkg-config --list-all | grep pocketsphinx > /dev/null
if [ $? -eq 1 ]; then
    myFile0="./pocketsphinx-5prealpha.tar.gz"
    myDir0="./pocketsphinx-5prealpha"
    if [ ! -f "$myFile0" ]; then
    	wget http://jaist.dl.sourceforge.net/project/cmusphinx/pocketsphinx/5prealpha/pocketsphinx-5prealpha.tar.gz
    fi
    if [ ! -d "$myDir0" ]; then
    	tar -xzvf pocketsphinx-5prealpha.tar.gz
    fi
    ./pocketsphinx-5prealpha/configure&&make&&sudo make install || { echo "pocketsphinx install failed"; exit 1; }
    echo export LD_LIBRARY_PATH=/usr/local/lib | sudo tee -a /etc/profile 
    echo export PKG_CONFIG_PATH=/usr/local/lib/pkgconfig | sudo tee -a /etc/profile
    source /etc/profile  
    echo "pocketsphinx successfully installed"
else echo "pocketsphinx already exit "
fi

for ((i=0;i<${#module[@]};i++));
	do
		#echo ${package[i]}
		ModuleInstall ${module[i]}
	done 

pocketsphinx_continuous -samprate 16000/8000/48000 
#rm -rf ../voiceRecognition
echo -e "\nSuccessfully deployed!\n"
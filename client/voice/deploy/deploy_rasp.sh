#set -e

myDir="../work-dir/"
if [ ! -d "$myDir" ]; then
	mkdir "$myDir"
fi

cd "$myDir"

dpkg -l | grep libogg-dev  > /dev/null
if [ $? -eq 0 ]; then
	echo "libogg-dev already exit"
else
	sudo apt-get install -y libogg-dev || { echo "libogg-dev install failed"; exit 1; } 
	echo "libogg-dev successfully installed"
fi

dpkg -l | grep python2.7-dev  > /dev/null
if [ $? -eq 0 ]; then
	echo "python2.7-dev already exit"
else
	sudo apt-get install -y python2.7-dev || { echo "python2.7-dev install failed"; exit 1; }
	echo "python2.7-dev successfully installed"
fi

whatis pip  > /dev/null
if [ $? -eq 0 ]; then
	echo "python-pip already exit"
else
	sudo apt-get install -y python-pip || { echo "python-pip install failed"; exit 1; }
	echo "python-pip successfully installed"
fi

dpkg -l | grep speex  > /dev/null
if [ $? -eq 0 ]; then
	echo "speex already exit"
else
	sudo apt-get install -y speex  || { echo "speex install failed"; exit 1; }
	echo "speex successfully installed"
fi

dpkg -l | grep libspeex-dev > /dev/null
if [ $? -eq 0 ]; then
	echo "libspeex already exit"
else
	sudo apt-get install libspeex-dev  || { echo "libspeex-dev install failed"; exit 1; }
	echo "libspeex successfully installed"
fi

pip freeze | grep Pyrex  > /dev/null
if [ $? -eq 0 ]; then
	echo "pyrex already exit"
else
	sudo pip install pyrex  --allow-external pyrex --allow-unverified pyrex || { echo "pyrex install failed"; exit 1; }
	echo "pyrex successfully installed"
fi

dpkg -l | grep pkg-config  > /dev/null
if [ $? -eq 0 ]; then
	echo "pkg-config already exit"
else
	sudo apt-get install -y pkg-config  || { echo "pkg-config install failed"; exit 1; }
	echo "pkg-config successfully installed"
fi

pkg-config --list-all | grep portaudio > /dev/null
if [ $? -eq 1 ]; then
    myFile0="./pa_stable_v19_20140130.tgz"
    if [ ! -f "$myFile0" ]; then
    	wget http://www.portaudio.com/archives/pa_stable_v19_20140130.tgz
    fi
    tar -xzvf pa_stable_v19_20140130.tgz
    ./portaudio/configure&&make clean&&make&&make install  || { echo "portaudio install failed"; exit 1; }
    echo "portaudio successfully installed"
else echo "portaudio already exit "
fi

pip freeze | grep PyAudio  > /dev/null
if [ $? -eq 0 ]; then
	echo "pyaudio already exit"
else
	sudo pip install pyaudio --allow-external pyaudio --allow-unverified pyaudio || { echo "pyaudio install failed"; exit 1; }
	echo "pyaudio successfully installed"
fi


#myFile1="./speex-1.2rc1.tar.gz"
#if [ ! -f "$myFile1" ]; then  
#	wget http://downloads.xiph.org/releases/speex/speex-1.2rc1.tar.gz
#	tar -xzvf speex-1.2rc1.tar.gz  
#fi

rm -rf ../work-dir

echo -e "\nSuccessfully deployed!\n"

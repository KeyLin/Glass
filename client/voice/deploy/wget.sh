#!/bin/bash

myDir="/tmp/voiceRecognition"
if [ ! -d "$myDir" ]; then
	mkdir "$myDir"
fi

cd "$myDir"

wget http://jaist.dl.sourceforge.net/project/cmusphinx/sphinxbase/5prealpha/sphinxbase-5prealpha.tar.gz
tar -xzvf sphinxbase-5prealpha.tar.gz

wget http://jaist.dl.sourceforge.net/project/cmusphinx/pocketsphinx/5prealpha/pocketsphinx-5prealpha.tar.gz
tar -xzvf pocketsphinx-5prealpha.tar.gz
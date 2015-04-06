#bin/bash

declare -a package
declare -a module

package[0]=libsphinxbase-dev
package[1]=libsphinxbase1
package[2]=sphinxbase-utils

package[3]=libpocketsphinx-dev
package[4]=libpocketsphinx1

package[5]=python-sphinxbase
package[6]=python-pocketsphinx
#python-pocketsphinx-dbg 

package[7]=pocketsphinx-lm-zh-hans-gigatdt 

function PackageInstall()
{
	#echo $1
	dpkg -l | grep $1  > /dev/null
	if [ $? -eq 0 ]; then
		echo "$1 already exit"
	else
		sudo apt-get install -y '$1' || { echo "$1 install failed"; exit 1; } 
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



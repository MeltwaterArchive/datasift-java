#!/bin/bash
#-v

#export BASE_DIR="$( cd "$( dirname $0 )/../../../.." && pwd )/"
export BASE_DIR="`pwd`/"

source ${BASE_DIR}sub/make-docs-util-defs.sh
export BASE_DIR="/tmp/$(basename $0).$$.tmp/"
initialise $*

### Java-specific parameters
parameters "java"

### installation of Java-specific tools
message "installing tools"
sudo apt-get install git
sudo apt-get install default-jdk

pre_build

### Java-specific build steps

#message "preparing to build documents"

### (build/copy docs steps commbined in this case)
(
	message "building documents"
	cd ${GH_PAGES_DIR} ; stop_on_error
	cp doc-tools/overview.html ${CODE_DIR}src/ ; stop_on_error
	javadoc \
        	-d . \
        	-use \
        	-splitIndex \
        	-overview ../code/src/overview.html \
        	-doctitle 'DataSift Java API' \
        	-header 'DataSift Java API' \
        	-footer 'Copyright &copy; 2010-'`date +%Y`' <a href="http://datasift.com">DataSift</a>' \
        	-bottom 'DataSift Java API' \
        	-sourcepath ../code/src/ \
        	-windowtitle 'DataSift Java API'  \
        	org.datasift org.json de.roderick ; stop_on_error
) || error "stopped parent"

(
	cd ${GH_PAGES_DIR} ; stop_on_error
	git add *
) || error "stopped parent"

exit
post_build

finalise


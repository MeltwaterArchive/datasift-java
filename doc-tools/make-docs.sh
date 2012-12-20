#!/bin/bash
#-v

export BASE_DIR="$( cd "$( dirname $0 )/../../../.." && pwd )/"

source ${BASE_DIR}ms-tools/doc-tools/docathon/sub/make-docs-util-defs.sh
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
	javadoc -sourcepath ../master/src/ -windowtitle 'DataSift Java API' org.datasift org.json de.roderick ; stop_on_error
) || error "stopped parent"

(
	cd ${GH_PAGES_DIR} ; stop_on_error
	git add *
) || error "stopped parent"

post_build

finalise

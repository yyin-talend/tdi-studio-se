#!/bin/sh
cd `dirname $0`
ROOT_PATH=`pwd`
java -Xms256M -Xmx1024M -cp ${talend.job.sh.classpath} ${talend.job.class} ${talend.job.sh.addition} "$@" 
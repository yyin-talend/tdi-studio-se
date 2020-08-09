#!/bin/sh
cd `dirname $0`
ROOT_PATH=`pwd`
java -Dtalend.component.manager.m2.repository=$ROOT_PATH/../MAVEN-INF/repository ${talend.job.jvmargs} -cp ${talend.job.sh.classpath} ${talend.job.class} ${talend.job.sh.addition} "$@"
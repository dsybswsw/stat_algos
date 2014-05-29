#! /bin/sh

HDP_PATH=../../hadoop-1.2.1

ant jar
cp proj.jar ${HDP_PATH}

rm -fr ${HDP_PATH}/lr.mdl*

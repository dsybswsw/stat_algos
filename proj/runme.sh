#! /bin/sh

FEAT_NUM=$1
CLASS_NUM=$2
TRAIN=$3
MDL_HEAD=$4

rm -fr ${MDL_HEAD}_*
rm -fr ${MDL_HEAD}.mdl

./bin/hadoop jar proj.jar paral_opt.MaxEntJob ${FEAT_NUM} ${CLASS_NUM} ${TRAIN} ${MDL_HEAD} 

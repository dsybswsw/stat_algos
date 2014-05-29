#! /bin/sh

FEAT_NUM=$1
TRAIN=$2
MDL_HEAD=$3

rm -fr ${MDL_HEAD}_*
rm -fr ${MDL_HEAD}.mdl

./bin/hadoop jar proj.jar paral_opt.LogisticJob ${FEAT_NUM} ${TRAIN} ${MDL_HEAD} 

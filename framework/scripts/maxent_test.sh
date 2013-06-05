#! /bin/sh

echo "test max entropy max_ent"

java -classpath ./build/classes/ \
    -Djava.ext.dirs=./lib   \
    max_ent.common.MaxEntTester
    
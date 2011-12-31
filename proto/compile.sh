#!/bin/bash
set +e
set -x

OUTPUTDIR="`readlink -ne ../src/main/java`"
for PROTO in *.proto
do
    protoc "$PROTO" --java_out="$OUTPUTDIR"
done


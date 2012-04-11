#!/bin/bash
set -e
set -x

cd "`dirname $0`"
PROTODIR="`readlink -ne .`"
OUTPUTDIR="`readlink -ne ../src/main/java`"

for PROTO in "$PROTODIR"/*.proto
do
    protoc -I/ -I"$PROTODIR" "$PROTO" --java_out="$OUTPUTDIR"
done


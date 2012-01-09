#!/bin/bash
set +e
set -x

PROTODIR="`dirname $0`"
PROTODIR=`readlink -ne "$PROTODIR"`
OUTPUTDIR=`readlink -ne "$PROTODIR/../src/main/java"`
for PROTO in "$PROTODIR"/*.proto
do
    protoc -I/ "$PROTO" --java_out="$OUTPUTDIR"
done


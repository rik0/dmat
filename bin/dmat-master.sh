#! /bin/sh

for arg in $@
do
  if [ $arg = "-h" -o $arg = "--help" ]; then
    echo "   dmat-master.sh";
    echo "Script to launch the master node";
    echo "Needs one argument: filename of the scala script to be executed on the master node"
    return 0;
  elif [ $arg = "-v" -o $arg = "--version" ]; then
    echo "dmat-master version 1.0";
    scalac -version
    return 0;
  fi
done

if [ $# -eq 0 ]; then
  echo "Provide at least one argument";
  echo "\t -h or --help for help";
  return 0;
elif [ $# -gt 1 ]; then
  echo "Too many arguments ($#, 1 expected)";
  echo "\t -h or --help for help";
  return 0;
fi

NAME=$1

BASEDIR=`dirname $0`/..
BASEDIR=`(cd "$BASEDIR"; pwd)`

scala -cp $BASEDIR/target/dmat-1.0-SNAPSHOT.jar -Djava.library.path=$BASEDIR/usr/lib $1


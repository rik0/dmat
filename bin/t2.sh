#!/bin/sh
# ----------------------------------------------------------------------------
#  Copyright 2001-2006 The Apache Software Foundation.
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
# ----------------------------------------------------------------------------
#
#   Copyright (c) 2001-2006 The Apache Software Foundation.  All rights
#   reserved.

BASEDIR=`dirname $0`/..
BASEDIR=`(cd "$BASEDIR"; pwd)`



# OS specific support.  $var _must_ be set to either true or false.
cygwin=false;
darwin=false;
case "`uname`" in
  CYGWIN*) cygwin=true ;;
  Darwin*) darwin=true
           if [ -z "$JAVA_VERSION" ] ; then
             JAVA_VERSION="CurrentJDK"
           else
             echo "Using Java version: $JAVA_VERSION"
           fi
           if [ -z "$JAVA_HOME" ] ; then
             JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Versions/${JAVA_VERSION}/Home
           fi
           ;;
esac

if [ -z "$JAVA_HOME" ] ; then
  if [ -r /etc/gentoo-release ] ; then
    JAVA_HOME=`java-config --jre-home`
  fi
fi

# For Cygwin, ensure paths are in UNIX format before anything is touched
if $cygwin ; then
  [ -n "$JAVA_HOME" ] && JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
  [ -n "$CLASSPATH" ] && CLASSPATH=`cygpath --path --unix "$CLASSPATH"`
fi

# If a specific java binary isn't specified search for the standard 'java' binary
if [ -z "$JAVACMD" ] ; then
  if [ -n "$JAVA_HOME"  ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
      # IBM's JDK on AIX uses strange locations for the executables
      JAVACMD="$JAVA_HOME/jre/sh/java"
    else
      JAVACMD="$JAVA_HOME/bin/java"
    fi
  else
    JAVACMD=`which java`
  fi
fi

if [ ! -x "$JAVACMD" ] ; then
  echo "Error: JAVA_HOME is not defined correctly."
  echo "  We cannot execute $JAVACMD"
  exit 1
fi

if [ -z "$REPO" ]
then
  REPO="$BASEDIR"/repo
fi

CLASSPATH=$CLASSPATH_PREFIX:"$BASEDIR"/etc:"$REPO"/org/zeromq/jzmq/1.1.0-SNAPSHOT/jzmq-1.1.0-SNAPSHOT.jar:"$REPO"/com/google/protobuf/protobuf-java/2.4.1/protobuf-java-2.4.1.jar:"$REPO"/org/codehaus/mojo/appassembler-maven-plugin/1.2/appassembler-maven-plugin-1.2.jar:"$REPO"/org/codehaus/mojo/appassembler/appassembler-model/1.2/appassembler-model-1.2.jar:"$REPO"/net/java/dev/stax-utils/stax-utils/20060502/stax-utils-20060502.jar:"$REPO"/stax/stax/1.1.1-dev/stax-1.1.1-dev.jar:"$REPO"/org/codehaus/plexus/plexus-utils/1.5.6/plexus-utils-1.5.6.jar:"$REPO"/org/apache/maven/maven-plugin-api/2.0/maven-plugin-api-2.0.jar:"$REPO"/org/apache/maven/maven-project/2.0.10/maven-project-2.0.10.jar:"$REPO"/org/apache/maven/maven-settings/2.0.10/maven-settings-2.0.10.jar:"$REPO"/org/apache/maven/maven-profile/2.0.10/maven-profile-2.0.10.jar:"$REPO"/org/apache/maven/maven-model/2.0.10/maven-model-2.0.10.jar:"$REPO"/org/apache/maven/maven-artifact-manager/2.0.10/maven-artifact-manager-2.0.10.jar:"$REPO"/org/apache/maven/maven-repository-metadata/2.0.10/maven-repository-metadata-2.0.10.jar:"$REPO"/org/apache/maven/wagon/wagon-provider-api/1.0-beta-2/wagon-provider-api-1.0-beta-2.jar:"$REPO"/org/apache/maven/maven-plugin-registry/2.0.10/maven-plugin-registry-2.0.10.jar:"$REPO"/org/codehaus/plexus/plexus-interpolation/1.1/plexus-interpolation-1.1.jar:"$REPO"/org/apache/maven/maven-artifact/2.0.10/maven-artifact-2.0.10.jar:"$REPO"/stax/stax-api/1.0.1/stax-api-1.0.1.jar:"$REPO"/org/codehaus/plexus/plexus-container-default/1.0-alpha-9-stable-1/plexus-container-default-1.0-alpha-9-stable-1.jar:"$REPO"/classworlds/classworlds/1.1-alpha-2/classworlds-1.1-alpha-2.jar:"$REPO"/gnu/getopt/java-getopt/1.0.13/java-getopt-1.0.13.jar:"$REPO"/org/scala-lang/scala-library/2.8.0/scala-library-2.8.0.jar:"$REPO"/it/unipr/aotlab/dmat/1.0-SNAPSHOT/dmat-1.0-SNAPSHOT.jar
EXTRA_JVM_ARGUMENTS="-Xms128m -Djava.library.path=$BASEDIR/usr/lib"

# For Cygwin, switch paths to Windows format before running java
if $cygwin; then
  [ -n "$CLASSPATH" ] && CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
  [ -n "$JAVA_HOME" ] && JAVA_HOME=`cygpath --path --windows "$JAVA_HOME"`
  [ -n "$HOME" ] && HOME=`cygpath --path --windows "$HOME"`
  [ -n "$BASEDIR" ] && BASEDIR=`cygpath --path --windows "$BASEDIR"`
  [ -n "$REPO" ] && REPO=`cygpath --path --windows "$REPO"`
fi

exec "$JAVACMD" $JAVA_OPTS \
  $EXTRA_JVM_ARGUMENTS \
  -classpath "$CLASSPATH" \
  -Dapp.name="t2" \
  -Dapp.pid="$$" \
  -Dapp.repo="$REPO" \
  -Dbasedir="$BASEDIR" \
  it.unipr.aotlab.dmat.mains.MulMatricesT2 \
  "$@"

#! /bin/bash

builddir=$1
shift

if [ -z "$builddir" ] ; then 
    echo Usage $0 build-dir
    exit 1
fi

if [ -z "$ANT_PATH" ] ; then
    TMP_ANT_PATH=$(dirname `which ant`)/..
    if [ -e $TMP_ANT_PATH/lib/ant.jar ] ; then
        ANT_PATH=$TMP_ANT_PATH
        export ANT_PATH
        echo autosetting ANT_PATH to $ANT_PATH
    fi
fi
if [ -z "$ANT_PATH" ] ; then
    echo ANT_PATH does not exist, set it
    print_usage
    exit
fi

ANT_JARS=$ANT_PATH/lib/ant.jar:$ANT_PATH/lib/ant-junit.jar:$ANT_PATH/lib/ant-launcher.jar

LOG=runtest.log
rm -f $LOG

#D_ARGS="-Djogamp.debug.ProcAddressHelper=true -Djogamp.debug.NativeLibrary=true"
#D_ARGS="-Djogamp.debug.TraceLock"
#D_ARGS="-Djogamp.debug.JarUtil"
#D_ARGS="-Djogamp.debug.TempFileCache"
#D_ARGS="-Djogamp.debug.JNILibLoader -Djogamp.debug.TempFileCache -Djogamp.debug.JARUtil"
#D_ARGS="-Djogamp.debug.JNILibLoader -Djogamp.gluegen.UseTempJarCache=false"
#D_ARGS="-Djogamp.debug.JNILibLoader"
#D_ARGS="-Djogamp.debug.Lock"
#D_ARGS="-Djogamp.debug.Lock -Djogamp.debug.Lock.TraceLock"
#D_ARGS="-Djogamp.debug.Lock.TraceLock"

function onetest() {
    clazz=$1
    shift
    #libspath=$builddir/obj:$builddir/test/build/natives:
    libspath=$builddir/test/build/natives:
    LD_LIBRARY_PATH=$libspath:$LD_LIBRARY_PATH
    CLASSPATH=lib/junit.jar:$ANT_JARS:$builddir/../make/lib/TestJarsInJar.jar:$builddir/gluegen-rt.jar:$builddir/gluegen.jar:$builddir/test/build/gluegen-test.jar
    echo LD_LIBRARY_PATH $LD_LIBRARY_PATH
    echo CLASSPATH $CLASSPATH
    java $D_ARGS -Djava.library.path=$libspath $clazz
    echo
}

#onetest com.jogamp.common.GlueGenVersion 2>&1 | tee -a $LOG
#onetest com.jogamp.common.util.TestVersionInfo 2>&1 | tee -a $LOG
#onetest com.jogamp.common.util.TestIteratorIndexCORE 2>&1 | tee -a $LOG
#onetest com.jogamp.common.util.locks.TestRecursiveLock01 2>&1 | tee -a $LOG
#onetest com.jogamp.common.util.TestArrayHashSet01 2>&1 | tee -a $LOG
#onetest com.jogamp.common.util.IntIntHashMapTest 2>&1 | tee -a $LOG
#onetest com.jogamp.common.util.IntObjectHashMapTest 2>&1 | tee -a $LOG
#onetest com.jogamp.common.util.LongIntHashMapTest 2>&1 | tee -a $LOG
#onetest com.jogamp.common.nio.TestBuffersFloatDoubleConversion 2>&1 | tee -a $LOG
#onetest com.jogamp.gluegen.PCPPTest 2>&1 | tee -a $LOG
#onetest com.jogamp.common.nio.TestPointerBufferEndian 2>&1 | tee -a $LOG
#onetest com.jogamp.common.nio.TestStructAccessorEndian 2>&1 | tee -a $LOG
#onetest com.jogamp.gluegen.test.junit.generation.Test1p1JavaEmitter 2>&1 | tee -a $LOG
#onetest com.jogamp.gluegen.test.junit.generation.Test1p2ProcAddressEmitter 2>&1 | tee -a $LOG
#onetest com.jogamp.common.util.TestPlatform01 2>&1 | tee -a $LOG
#onetest com.jogamp.common.util.TestRunnableTask01 2>&1 | tee -a $LOG
#onetest com.jogamp.common.util.TestIOUtil01 2>&1 | tee -a $LOG
onetest com.jogamp.common.util.TestTempJarCache 2>&1 | tee -a $LOG
#onetest com.jogamp.common.util.TestJarUtil 2>&1 | tee -a $LOG

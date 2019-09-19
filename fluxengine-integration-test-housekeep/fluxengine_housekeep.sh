#!/bin/sh

if [ $# -lt 1 ];then
    echo "Usage: $ ./fluxengine_housekeep.sh path[full path of fluxengine housekeep job config file]"
    exit 1
else
    if [ ! -e $1 ]; then
        echo "Usage: $ ./fluxengine_housekeep.sh path[full path of fluxengine housekeep job config file]"
        exit 1
    fi

    if [ $# -eq 2 ]; then
	if [ $2 != "debug" ]; then
            echo "Usage: $ ./fluxengine_housekeep.sh path[full path of fluxengine housekeep job config file] defaultWorkerLogLevel[debug]"
            exit 1
	fi
    fi
fi

. $1

if [ -z ${GOOGLE_APPLICATION_CREDENTIALS} -o  -z ${PROJECT} ]; then
    echo "GOOGLE_APPLICATION_CREDENTIALS and PROJECT are required."
    exit 1
fi

OPTIONS=" --runner=DataflowRunner --project=${PROJECT}"


if [ -z $HOUSEKEEP_JOB_STAGING_LOCATION -o  -z ${TEMPLATE_LOCATION} ]; then
    echo "HOUSEKEEP_JOB_STAGING_LOCATION and TEMPLATE_LOCATION are required."
    exit 1
fi

OPTIONS=${OPTIONS}" --stagingLocation=${HOUSEKEEP_JOB_STAGING_LOCATION} --templateLocation=${TEMPLATE_LOCATION} --streaming=false"

if [ $# -eq 2 ]; then
   WORKER_LOG_LEVEL=$(echo $3 | tr '[a-z]' '[A-Z]')
   OPTIONS=${OPTIONS}" --defaultWorkerLogLevel=${WORKER_LOG_LEVEL}"
else
   OPTIONS=${OPTIONS}" --defaultWorkerLogLevel=WARN"
fi

OPTIONS=${OPTIONS}" --region=${REGION}"
OPTIONS=${OPTIONS}" --timezone=${TIMEZONE}"

BASE_DIR=$(pwd)
CONF="${BASE_DIR}/conf/"
export CONF=${CONF}
LIB="${BASE_DIR}/lib"

HOUSEKEEP_JOB_CLASS="jp.co.fluxengine.gcp.dataflow.housekeep.HouseKeepProcessor"

for libfile in ${LIB}/*.jar ; do
if [ -f $libfile ] ; then
    CLASSPATH=$libfile:${CLASSPATH}
#    echo $libfile
fi
done

CLASSPATH=${CONF}:${CLASSPATH}

java -server -cp ${CLASSPATH} ${HOUSEKEEP_JOB_CLASS} ${OPTIONS}

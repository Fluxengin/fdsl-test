#!/bin/sh

if [ $# -lt 2 ];then
    echo "Usage: $ ./dataflow_job_publisher.sh kind[batch or stream] path[full path of fluxengine dataflow job publish config file]"
    exit 1
else
    if [ -z `echo $1 | egrep '(batch|stream)'` ] ; then
        echo "Usage: $ ./dataflow_job_publisher.sh kind[batch or stream] path[full path of fluxengine dataflow job publish config file]"
        exit 1
    fi

    if [ ! -e $2 ]; then
        echo "Usage: $ ./dataflow_job_publisher.sh kind[batch or stream] path[full path of fluxengine dataflow job publish config file]"
        exit 1
    fi

    if [ $# -eq 3 ]; then
	  if [ $3 != "debug" -a $3 != "info" -a $3 != "warn" ]; then
            echo "Usage: $ ./dataflow_job_publisher.sh kind[batch or stream] path[full path of fluxengine dataflow job publish config file] defaultWorkerLogLevel[debug|info|warn]"
            exit 1
	  fi
    fi
fi

. $2

if [ -z ${GOOGLE_APPLICATION_CREDENTIALS} -o  -z ${PROJECT} ]; then
    echo "GOOGLE_APPLICATION_CREDENTIALS and PROJECT are required."
    exit 1
fi

OPTIONS=" --runner=DataflowRunner --project=${PROJECT}"

if [ $1 = "batch" ]; then
    if [ -z $BATCH_JOB_STAGING_LOCATION -o  -z ${TEMPLATE_LOCATION} ]; then
        echo "BATCH_JOB_STAGING_LOCATION and TEMPLATE_LOCATION are required."
        exit 1
    fi

    OPTIONS=${OPTIONS}" --stagingLocation=${BATCH_JOB_STAGING_LOCATION} --templateLocation=${TEMPLATE_LOCATION} --streaming=false"
fi

if [ $1 = "stream" ]; then
    if [ -z ${STREAM_JOB_STAGING_LOCATION} -o  -z ${FROM_SUBSCRIPTION} ]; then
        echo "BATCH_JOB_STAGING_LOCATION and TEMPLATE_LOCATION are required."
        exit 1
    fi

    OPTIONS=${OPTIONS}" --stagingLocation=${STREAM_JOB_STAGING_LOCATION} --fromSubscription=${FROM_SUBSCRIPTION} --streaming=true"
fi

if [ ${AUTO_SCALING} = "THROUGHPUT_BASED" ]; then
  OPTIONS=${OPTIONS}" --autoscalingAlgorithm=${AUTO_SCALING} --maxNumWorkers=${MAX_NUM_WORKERS}"
fi

if [ $# -eq 3 ]; then
   WORKER_LOG_LEVEL=$(echo $3 | tr '[a-z]' '[A-Z]')
   OPTIONS=${OPTIONS}" --defaultWorkerLogLevel=${WORKER_LOG_LEVEL}"
else
   OPTIONS=${OPTIONS}" --defaultWorkerLogLevel=WARN"
fi

#init workers
OPTIONS=${OPTIONS}" --numWorkers=${NUM_WORKERS}"
OPTIONS=${OPTIONS}" --region=${REGION}"
OPTIONS=${OPTIONS}" --tempLocation=${TEMP_LOCATION}"
OPTIONS=${OPTIONS}" --timezone=${TIMEZONE}"
OPTIONS=${OPTIONS}" --reshuffleBound=100"

BASE_DIR=$(pwd)
CONF="${BASE_DIR}/conf/"
export CONF=${CONF}
LIB="${BASE_DIR}/lib"


DATAFLOW_JOB_CLASS="jp.co.fluxengine.gcp.dataflow.FluxengineDataflowProcessor"

for libfile in ${LIB}/*.jar ; do
if [ -f $libfile ] ; then
    CLASSPATH=$libfile:${CLASSPATH}
#    echo $libfile
fi
done

CLASSPATH=${CONF}:${CLASSPATH}

java -server -cp ${CLASSPATH} ${DATAFLOW_JOB_CLASS} ${OPTIONS}

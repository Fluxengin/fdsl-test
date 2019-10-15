#!/bin/bash

{
  pushd ~/fdsl-test/datastore-cleaner
  chmod +x gradlew
  ALL_NAMESPACES=`seq --format="INTEGRATION_TEST%g_${CIRCLE_WORKFLOW_ID}" --separator=' ' 1 4`
  env NAMESPACE="${ALL_NAMESPACES}" ./gradlew run
  popd
} &
pidDatastore=$!

{
  PUBSUBS=`seq --format="integration-test%g_${CIRCLE_WORKFLOW_ID}" --separator=' ' 1 4`
  gcloud pubsub subscriptions delete ${PUBSUBS}
  gcloud pubsub topics delete ${PUBSUBS}
} &
pidPubsub=$!

{
  for i in `seq 1 4`; do
    STAGING_DIRECTORY=gs://${BUCKET}/staging${i}_${CIRCLE_WORKFLOW_ID}
    TEMPLATE_FILE=gs://${BUCKET}/templates/MyTemplate${i}_${CIRCLE_WORKFLOW_ID}
    gsutil -m rm -r ${STAGING_DIRECTORY} ${TEMPLATE_FILE}
  done
  HOUSEKEEP_STAGING_DIRECTORY=gs://${BUCKET}/stagingh_${CIRCLE_WORKFLOW_ID}
  HOUSEKEEP_TEMPLATE_FILE=gs://${BUCKET}/templates/housekeepJob_${CIRCLE_WORKFLOW_ID}
  gsutil -m rm -r ${HOUSEKEEP_STAGING_DIRECTORY} ${HOUSEKEEP_TEMPLATE_FILE}
} &
pidStorage=$!

{
  SERVICES=`seq --format="%g-${CIRCLE_WORKFLOW_ID}" --separator=' ' 1 2`
  HOUSEKEEP_SERVICE_NAME=h-${CIRCLE_WORKFLOW_ID}
  gcloud --quiet app services delete ${SERVICES} ${HOUSEKEEP_SERVICE_NAME}
} &
pidService=$!

{
  # Stop SQL instance only when no other test is runnung
  FLAG_FILE_LOCATION=gs://${BUCKET}/running/${CIRCLE_WORKFLOW_ID}
  gsutil rm ${FLAG_FILE_LOCATION}
  gsutil ls gs://${BUCKET}/running/* || gcloud sql instances patch integration-test --activation-policy=NEVER --async
} &
pidSql=$!

REDIS_INSTANCE_NAME=i-${CIRCLE_WORKFLOW_ID}
gcloud --quiet redis instances delete ${REDIS_INSTANCE_NAME} --region=asia-northeast1 --async

wait $pidDatastore $pidPubsub $pidStorage $pidService $pidSql

#!/bin/bash

{
  pushd ~/fdsl-test/datastore-cleaner
  chmod +x gradlew
  env NAMESPACE="INTEGRATION_TEST1_${CIRCLE_WORKFLOW_ID} INTEGRATION_TEST2_${CIRCLE_WORKFLOW_ID} INTEGRATION_TEST3_${CIRCLE_WORKFLOW_ID}" ./gradlew run
  popd
} &
pidDatastore=$!

{
  for i in `seq 1 4`; do
    PUBSUB_NAME=integration-test${i}_${CIRCLE_WORKFLOW_ID}
    gcloud pubsub subscriptions delete ${PUBSUB_NAME}
    gcloud pubsub topics delete ${PUBSUB_NAME}
  done
} &
pidPubsub=$!

{
  for i in `seq 1 4`; do
    STAGING_DIRECTORY=gs://fluxengine-integration-test/staging${i}_${CIRCLE_WORKFLOW_ID}
    TEMPLATE_FILE=gs://fluxengine-integration-test/templates/MyTemplate${i}_${CIRCLE_WORKFLOW_ID}
    gsutil -m rm -r ${STAGING_DIRECTORY} ${TEMPLATE_FILE}
  done
  HOUSEKEEP_TEMPLATE_FILE=gs://fluxengine-integration-test/templates/housekeepJob_${CIRCLE_WORKFLOW_ID}
  gsutil -m rm -r ${HOUSEKEEP_TEMPLATE_FILE}
} &
pidStorage=$!

{
  for i in `seq 1 2`; do
    SERVICE_NAME=${i}-${CIRCLE_WORKFLOW_ID}
    gcloud --quiet app services delete ${SERVICE_NAME}
  done
  HOUSEKEEP_SERVICE_NAME=h-${CIRCLE_WORKFLOW_ID}
  gcloud --quiet app services delete ${HOUSEKEEP_SERVICE_NAME}
}
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

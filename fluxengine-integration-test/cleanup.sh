#!/bin/bash

{
  pushd ~/fdsl-test/datastore-cleaner
  chmod +x gradlew
  env NAMESPACE=INTEGRATION_TEST_${CIRCLE_WORKFLOW_ID} ./gradlew run
  popd
} &
pidDatastore=$!

{
  PUBSUB_NAME=integration-test_${CIRCLE_WORKFLOW_ID}
  gcloud pubsub subscriptions delete ${PUBSUB_NAME}
  gcloud pubsub topics delete ${PUBSUB_NAME}
} &
pidPubsub=$!

{
  # Stop SQL instance only when no other test is runnung
  FLAG_FILE_LOCATION=gs://${BUCKET}/running/${CIRCLE_WORKFLOW_ID}
  gsutil rm ${FLAG_FILE_LOCATION}
  gsutil ls gs://${BUCKET}/running/* || gcloud sql instances patch integration-test --activation-policy=NEVER --async
} &
pidStopSql=$!

{
  STAGING_DIRECTORY=gs://fluxengine-integration-test/staging_${CIRCLE_WORKFLOW_ID}
  TEMPLATE_FILE=gs://fluxengine-integration-test/templates/MyTemplate_${CIRCLE_WORKFLOW_ID}
  HOUSEKEEP_TEMPLATE_FILE=gs://fluxengine-integration-test/templates/housekeepJob_${CIRCLE_WORKFLOW_ID}
  gsutil -m rm -r ${STAGING_DIRECTORY} ${TEMPLATE_FILE} ${HOUSEKEEP_TEMPLATE_FILE}
} &
pidStorage=$!

REDIS_INSTANCE_NAME=i-${CIRCLE_WORKFLOW_ID}
gcloud --quiet redis instances delete ${REDIS_INSTANCE_NAME} --region=asia-northeast1 --async

SERVICE_NAME=${CIRCLE_WORKFLOW_ID}
gcloud --quiet app services delete ${SERVICE_NAME}

wait $pidDatastore $pidPubsub $pidStopSql $pidStorage

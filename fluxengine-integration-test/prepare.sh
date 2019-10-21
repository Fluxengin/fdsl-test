#!/bin/bash

# Create running flag
{
  touch dummy.txt
  FLAG_FILE_LOCATION=gs://${BUCKET}/running/${CIRCLE_WORKFLOW_ID}
  gsutil cp dummy.txt ${FLAG_FILE_LOCATION}
} &
pidFlag=$!

{
  for i in `seq 1 5`; do
    gcloud pubsub topics create integration-test${i}_${CIRCLE_WORKFLOW_ID}
    gcloud pubsub subscriptions create integration-test${i}_${CIRCLE_WORKFLOW_ID} --topic=integration-test${i}_${CIRCLE_WORKFLOW_ID}
  done
} &
pidPubsub=$!

gcloud sql instances patch integration-test --activation-policy=ALWAYS --async || echo "SQL instance is in operation, but continues this test"
REDIS_INSTANCE_NAME=i-${CIRCLE_WORKFLOW_ID}
gcloud redis instances create ${REDIS_INSTANCE_NAME} --size=1 --region=asia-northeast1 --zone=asia-northeast1-b --redis-version=redis_4_0 --async

wait $pidFlag $pidPubsub

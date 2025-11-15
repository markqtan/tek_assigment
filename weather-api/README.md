# S3 -> SQS Notification
~~~
awslocal sqs create-queue --queue-name s3-event-notification-queue

awslocal s3 mb s3://my-local-bucket

//get sqs arn
awslocal sqs get-queue-attributes --attribute-names QueueArn --queue-url http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/s3-event-notification-queue 

->  "QueueArn": "arn:aws:sqs:us-east-1:000000000000:s3-event-notification-queue"

QUEUE_ARN=$(awslocal sqs get-queue-attributes \
  --queue-url http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/s3-event-notification-queue \
  --attribute-names QueueArn \
  --query 'Attributes.QueueArn' --output text)

echo "Queue ARN: $QUEUE_ARN"

cat > notification.json <<EOF
{
  "QueueConfigurations": [
    {
      "QueueArn": "$QUEUE_ARN",
      "Events": ["s3:ObjectCreated:*"]
    }
  ]
}
EOF

awslocal s3api  put-bucket-notification-configuration \
  --bucket my-local-bucket \
  --notification-configuration file://notification.json


echo "Hello LocalStack" > test.txt
awslocal s3 cp test.txt s3://my-local-bucket/

awslocal sqs receive-message --queue-url http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/s3-event-notification-queue 
~~~

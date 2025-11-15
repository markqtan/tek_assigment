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

https://docs.localstack.cloud/aws/getting-started/installation/#docker-compose
#MSI
C:\Programs\localstack\docker-compose.yml
C:\Programs\bin\localstack-start
  Ø docker compose -f C:\Programs\localstack\docker-compose.yml up
C:\Programs\bin\localstack-stop
  Ø docker compose -f C:\Programs\localstack\docker-compose.yml stop

install aws cli (https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html)
config aws cli
  Ø ~/.aws/config
  [profile localstack]
  region = us-east-1
  output = json
  endpoint_url = http://localhost:4566
  Ø ~/.aws/credentials
  [localstack]
  aws_access_key_id = test
  aws_secret_access_key = test

  Ø aws s3 mb s3://my-local-bucket --profile localstack
  Ø aws s3 ls --profile localstack
  Ø pip install awscli-local[ver1] (https://docs.localstack.cloud/aws/integrations/aws-native-tools/aws-cli/)
  Ø awslocal s3 mb s3://my-local-bucket
  Ø awslocal s3 ls
  Ø awslocal sns list-topics
  Ø awslocal sqs create-queue --queue-name my-fifo-queue.fifo --attributes "FifoQueue=true"
  Ø awslocal sqs create-queue --queue-name my-sqs-queue
  Ø awslocal sqs list-queues
  Ø awslocal sqs delete-queue --queue-url http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/my-fifo-queue.fifo
  Ø awslocal sqs receive-message --queue-url http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/my-sqs-queue
  Ø  awslocal sqs delete-message --queue-url http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/my-sqs-queue --receipt-handle NmM2Y2JjZjItMTI1Mi00ZGJmLWFhZGItYzEyMmY0ODNiNzFhIGFybjphd3M6c3FzOnVzLWVhc3QtMTowMDAwMDAwMDAwMDA6bXktc3FzLXF1ZXVlIDNhNjBkZTQ4LWZhYzQtNDU5Zi04MWUzLWM3NTAxMWU2ZDA0ZSAxNzYzMTU4NzAyLjE1MDU0NTQ=
  
  
#spring s3 example https://github.com/adpe/springboot-amazon-aws-s3-example-with-localstack/tree/main
https://github.com/markqtan/tek_assigment

#localstack - aws
https://docs.localstack.cloud/aws/tutorials/java-notification-app/
https://hashnode.localstack.cloud/smooth-transition-from-aws-to-localstack-for-your-dev-environment


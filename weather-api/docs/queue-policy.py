import json
policy_dict = {  "Version": "2012-10-17",
  "Id": "example-ID",
  "Statement": [
    {
      "Sid": "example-statement-ID",
      "Effect": "Allow",
      "Principal": {
        "Service": "s3.amazonaws.com"
      },
      "Action": "SQS:SendMessage",
      "Resource": "arn:aws:sqs:us-east-1:000000000000:s3-event-notification-queue",
      "Condition": {
        "StringEquals": {
          "aws:SourceAccount": "*"
        },
        "ArnLike": {
          "aws:SourceArn": "arn:aws:s3:*:*:my-local-bucket"
        }
      }
    }
  ]
}
policy_string = json.dumps(policy_dict)
print(policy_string)

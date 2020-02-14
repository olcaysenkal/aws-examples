package com.test;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.event.S3EventNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AWS Lambda function with S3 trigger.
 *
 */
public class App implements RequestHandler<S3EventNotification, String> {

    static final Logger log = LoggerFactory.getLogger(App.class);

    @Override
    public String handleRequest(S3EventNotification s3EventNotification, Context context) {
        log.info("Lambda function is invoked:" + s3EventNotification.toJson());

        return null;
    }

}

/**
 {
 "Records": [
 {
 "awsRegion": "us-east-1",
 "eventName": "ObjectCreated:Put",
 "eventSource": "aws:s3",
 "eventTime": "2018-09-01T15:35:42.355Z",
 "eventVersion": "2.0",
 "requestParameters": {
 "sourceIPAddress": "139.5.48.135"
 },
 "responseElements": {
 "x-amz-id-2": "ouuqLMI4XGqHtZSbtylmHjj1djmCmkXhNnRp8XMyKPNT9xx5x+8bVWtz7PWzgLaz4U6CIEhCWQo=",
 "x-amz-request-id": "FCB3F1B77DF4FF2B"
 },
 "s3": {
 "configurationId": "74eab48d-723f-43bb-b03c-a70432beff3c",
 "bucket": {
 "name": "trinity-test-lambda",
 "ownerIdentity": {
 "principalId": "AWG8WXZYQB42H"
 },
 "arn": "arn:aws:s3:::trinity-test-lambda"
 },
 "object": {
 "key": "test.txt",
 "size": 13,
 "eTag": "a5decd745d43af4aa8cf62eef5be43ac",
 "versionId": "",
 "sequencer": "005B8AB1CE50E37A6D",
 "urlDecodedKey": "test.txt"
 },
 "s3SchemaVersion": "1.0"
 },
 "userIdentity": {
 "principalId": "AWG8WXZYQB42H"
 }
 }
 ]
 }
 */
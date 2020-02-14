package com.test;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App implements RequestHandler<Request, Object> {

    static final Logger log = LoggerFactory.getLogger(App.class);

    public  Object handleRequest(Request request, Context context) {

        AmazonDynamoDB client = AmazonDynamoDBAsyncClientBuilder.defaultClient();
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        Student student = null;

        if ("GET".equals(request.getHttpMethod())) {
            student = mapper.load(Student.class,request.getId());
            if (student == null){
                throw new RuntimeException("Resource not found" + request.getId());
            }
            return student;
        }
        else if ("POST".equals(request.getHttpMethod())){
            student = request.getStudent();
            mapper.save(student);
            return student;
        }


        return null;
    }
}

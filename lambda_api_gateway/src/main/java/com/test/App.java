package com.test;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersRequest;
import com.amazonaws.services.simplesystemsmanagement.model.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App implements RequestHandler<Request, Object> {

    static final Logger log = LoggerFactory.getLogger(App.class);

    public  Object handleRequest(Request request, Context context) {

        String stageName = System.getenv("STAGE_NAME");
        log.info("Stage name: " + stageName) ;

        Parameter dbPassword = getParameterFromSSMByName("/myDBs/Mysql/Prod-Inst-001-Pass");

        log.info("DB password " + dbPassword.getValue());

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

    protected Parameter getParameterFromSSMByName(String parameterKey)
    {
        AWSCredentialsProvider credentials = InstanceProfileCredentialsProvider.getInstance();
        AWSSimpleSystemsManagement simpleSystemsManagementClient = AWSSimpleSystemsManagementClientBuilder.defaultClient();
        GetParameterRequest parameterRequest = new GetParameterRequest();
        parameterRequest.withName(parameterKey).setWithDecryption(Boolean.valueOf(true));
        GetParameterResult parameterResult = simpleSystemsManagementClient.getParameter(parameterRequest);
        return parameterResult.getParameter();
    }
}

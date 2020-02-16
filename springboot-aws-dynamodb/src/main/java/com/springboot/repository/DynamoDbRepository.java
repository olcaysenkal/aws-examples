package com.springboot.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.springboot.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DynamoDbRepository {

    private static final Logger logger = LoggerFactory.getLogger(DynamoDbRepository.class);

    @Autowired
    DynamoDBMapper mapper;

    public void insertIntoDynamoDB(Student student) {
        mapper.save(student);
    }

    public Student getOneStudentDetails(String studentId, String lastName) {
        return mapper.load(Student.class, studentId, lastName);
    }

    public void updateStudentDetails(Student student) {
        try {
            mapper.save(student, buildDynamoDBSaveExpression(student));
        } catch (ConditionalCheckFailedException e) {
            logger.error("invalid data - " + e.getMessage());
        }
    }

    public void deleteStudent(Student student) {
        mapper.delete(student);
    }

    public DynamoDBSaveExpression buildDynamoDBSaveExpression(Student student) {
        DynamoDBSaveExpression saveExpression = new DynamoDBSaveExpression();

        Map<String, ExpectedAttributeValue> expected = new HashMap<>();
        expected.put("studentId", new ExpectedAttributeValue(new AttributeValue(student.getStudentId()))
                .withComparisonOperator(ComparisonOperator.EQ));

        saveExpression.setExpected(expected);
        return saveExpression;
    }

    public List<Student> getStudentsByAge(String age) {

        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":age", new AttributeValue().withS(age));

        DynamoDBQueryExpression<Student> queryExpression = new DynamoDBQueryExpression<Student>()
                .withKeyConditionExpression("age = :age").withExpressionAttributeValues(eav);

        queryExpression.setIndexName("age-index");
        queryExpression.setConsistentRead(false);
        queryExpression.setProjectionExpression("firstName,lastName");

        return mapper.query(Student.class, queryExpression);

    }
}

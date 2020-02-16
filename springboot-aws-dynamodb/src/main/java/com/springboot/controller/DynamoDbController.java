package com.springboot.controller;

import com.springboot.model.Student;
import com.springboot.repository.DynamoDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dynamoDb")
public class DynamoDbController {

    @Autowired
    private DynamoDbRepository repository;

    @PostMapping
    public String insertIntoDynamoDB(@RequestBody Student student) {
        repository.insertIntoDynamoDB(student);
        return "Successfully inserted into DynamoDb table";
    }


    @RequestMapping(method = RequestMethod.GET, params = {"age"})
    public ResponseEntity<List<Student>> getStudentsByAge(@RequestParam String age) {
        List<Student> studentList = repository.getStudentsByAge(age);
        return new ResponseEntity<List<Student>>(studentList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, params = {"studentId","lastName"})
    public ResponseEntity<Student> getOneStudentDetails(@RequestParam String studentId, @RequestParam String lastName) {
        Student student = repository.getOneStudentDetails(studentId, lastName);
        return new ResponseEntity<Student>(student, HttpStatus.OK);
    }


    @PutMapping
    public void updateStudentDetails(@RequestBody Student student) {
        repository.updateStudentDetails(student);
    }

    @DeleteMapping(value = "{studentId}/{lastName}")
    public void deleteStudent(@PathVariable String studentId, @PathVariable String lastName) {
        Student student = new Student();
        student.setStudentId(studentId);
        student.setLastName(lastName);
        repository.deleteStudent(student);
    }

}

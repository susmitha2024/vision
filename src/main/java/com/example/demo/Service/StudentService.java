package com.example.demo.Service;

import com.example.demo.exception.IdentityException;
import com.example.demo.model.Payload.StudentCreatePayload;
import org.springframework.http.ResponseEntity;

public interface StudentService {
    ResponseEntity<?> addStudent(StudentCreatePayload createPayload) throws IdentityException;

    ResponseEntity<?> getAllStudents() throws IdentityException;

    ResponseEntity<?> getStudent(String id) throws IdentityException;
}

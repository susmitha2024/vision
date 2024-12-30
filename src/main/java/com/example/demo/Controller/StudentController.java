package com.example.demo.Controller;

import com.example.demo.Service.StudentService;
import com.example.demo.exception.IdentityException;
import com.example.demo.model.Payload.StudentCreatePayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/student")
    public ResponseEntity<?> addStudent(@RequestBody StudentCreatePayload request) throws IdentityException {
        return studentService.addStudent(request);
    }
    @GetMapping("/student")
    public ResponseEntity<?> getAllStudents() throws IdentityException {
        return studentService.getAllStudents();
    }
    @GetMapping("/student/{id}")
    public ResponseEntity<?> getStudent(@PathVariable  String id) throws IdentityException {
        return studentService.getStudent(id);
    }

}
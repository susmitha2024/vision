package com.example.demo.Controller;

import com.example.demo.Service.InstituteService;
import com.example.demo.exception.IdentityException;
import com.example.demo.model.Payload.InstituteCreatePayload;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/")
public class Controller {

    @Autowired
    private InstituteService instituteService;

    @PostMapping("/institute")
    public ResponseEntity<?> addInstitute(@RequestBody InstituteCreatePayload request) throws IdentityException {
        return instituteService.addInstitute(request);
    }
    @GetMapping("/institute")
    public ResponseEntity<?> getAllInstitute() throws IdentityException {
        return instituteService.getAllInstitutes();
    }
    @GetMapping("/institute/{id}")
    public ResponseEntity<?> getInstitute(@PathVariable  String id) throws IdentityException {
        return instituteService.getInstitutes(id);
    }


}

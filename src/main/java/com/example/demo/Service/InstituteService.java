package com.example.demo.Service;

import com.example.demo.exception.IdentityException;
import com.example.demo.model.Payload.InstituteCreatePayload;
import org.springframework.http.ResponseEntity;

public interface InstituteService {
    ResponseEntity<?> addInstitute(InstituteCreatePayload createPayload) throws IdentityException;

    ResponseEntity<?> getAllInstitutes() throws IdentityException;

    ResponseEntity<?> getInstitutes(String id) throws IdentityException;
}

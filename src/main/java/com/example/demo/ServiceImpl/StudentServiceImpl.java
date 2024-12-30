package com.example.demo.ServiceImpl;

import com.example.demo.Service.StudentService;
import com.example.demo.common.ResponseBuilder;
import com.example.demo.elasticsearch.ElasticSearchOperations;
import com.example.demo.exception.ErrorMessageHandler;
import com.example.demo.exception.IdentityErrorMessageKey;
import com.example.demo.exception.IdentityException;
import com.example.demo.model.Payload.StudentCreatePayload;
import com.example.demo.model.ResourceType;
import com.example.demo.model.StudentEntity;
import com.example.demo.util.Constant;
import com.example.demo.util.ResourceIdUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StudentServiceImpl implements StudentService {

    @Autowired
    ElasticSearchOperations elasticSearchOperations;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public ResponseEntity<?> addStudent(StudentCreatePayload createPayload) throws IdentityException {
        String resourceId = ResourceIdUtils.generateStudentResourceId(createPayload.getFirstName(), System.currentTimeMillis());

        try {
            StudentEntity studentEntity = objectMapper.convertValue(createPayload, StudentEntity.class);
            studentEntity.setId(resourceId);
            studentEntity.setType(ResourceType.STUDENT.value());
            elasticSearchOperations.saveEntity(studentEntity, resourceId, Constant.INDEX_VISION);
            log.info("student registered ");
        } catch (Exception exception) {
            log.error("Unable to save the student details {} {}", createPayload.getInstituteName(),exception.getMessage());
            throw new IdentityException(ErrorMessageHandler.getMessage(IdentityErrorMessageKey.UNABLE_SAVE_INSTITUTE),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(
                ResponseBuilder.builder().build().createSuccessResponse(Constant.SUCCESS), HttpStatus.CREATED);
    }



    @Override
    public ResponseEntity<?> getAllStudents() throws IdentityException {
        List<StudentEntity> studentEntities ;
        try {
            studentEntities = elasticSearchOperations.getAllStudents();
        }catch (Exception exception) {
            log.error("Unable to get regions Details");
            throw new IdentityException(ErrorMessageHandler.getMessage(IdentityErrorMessageKey.valueOf("unable to get the Institutes")),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(
                ResponseBuilder.builder().build().createSuccessResponse(studentEntities), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getStudent(String id) throws IdentityException {
        StudentEntity studentEntity;
        try {
            studentEntity = elasticSearchOperations.getStudent(id);
        }catch (Exception exception) {
            log.error("Unable to get student Details");
            throw new IdentityException(ErrorMessageHandler.getMessage(IdentityErrorMessageKey.valueOf("unable to get the Institutes")),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(
                ResponseBuilder.builder().build().createSuccessResponse(studentEntity), HttpStatus.OK);
    }
}


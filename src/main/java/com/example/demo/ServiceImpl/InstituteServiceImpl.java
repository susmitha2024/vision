package com.example.demo.ServiceImpl;

import com.example.demo.Service.InstituteService;
import com.example.demo.common.ResponseBuilder;
import com.example.demo.elasticsearch.ElasticSearchOperations;
import com.example.demo.exception.ErrorMessageHandler;
import com.example.demo.exception.IdentityErrorMessageKey;
import com.example.demo.exception.IdentityException;
import com.example.demo.model.InstituteEntity;
import com.example.demo.model.Payload.InstituteCreatePayload;
import com.example.demo.model.ResourceType;
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
public class InstituteServiceImpl implements InstituteService {

    @Autowired
    ElasticSearchOperations elasticSearchOperations;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public ResponseEntity<?> addInstitute(InstituteCreatePayload createPayload) throws IdentityException {
        String resourceId = ResourceIdUtils.generateInstituteResourceId(createPayload.getInstituteName());

        try {
            InstituteEntity instituteEntity = objectMapper.convertValue(createPayload, InstituteEntity.class);
            instituteEntity.setId(resourceId);
            instituteEntity.setType(ResourceType.INSTITUTE.value());
            elasticSearchOperations.saveEntity(instituteEntity, resourceId, Constant.INDEX_VISION);
            log.info("institute registered ");
        } catch (Exception exception) {
            log.error("Unable to save the institute details {} {}", createPayload.getInstituteName(),exception.getMessage());
            throw new IdentityException(ErrorMessageHandler.getMessage(IdentityErrorMessageKey.UNABLE_SAVE_INSTITUTE),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(
                ResponseBuilder.builder().build().createSuccessResponse(Constant.SUCCESS), HttpStatus.CREATED);
    }



    @Override
    public ResponseEntity<?> getAllInstitutes() throws IdentityException {
        List<InstituteEntity> instituteEntities ;
        try {
            instituteEntities = elasticSearchOperations.getAllInstitution();
        }catch (Exception exception) {
            log.error("Unable to get regions Details");
            throw new IdentityException(ErrorMessageHandler.getMessage(IdentityErrorMessageKey.valueOf("unable to get the Institutes")),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(
                ResponseBuilder.builder().build().createSuccessResponse(instituteEntities), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getInstitutes(String id) throws IdentityException {
        InstituteEntity instituteEntity ;
        try {
            instituteEntity = elasticSearchOperations.getInstitution(id);
        }catch (Exception exception) {
            log.error("Unable to get institute Details");
            throw new IdentityException(ErrorMessageHandler.getMessage(IdentityErrorMessageKey.valueOf("unable to get the Institutes")),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(
                ResponseBuilder.builder().build().createSuccessResponse(instituteEntity), HttpStatus.OK);
    }
}

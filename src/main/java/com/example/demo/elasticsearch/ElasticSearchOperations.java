package com.example.demo.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.example.demo.exception.ErrorMessageHandler;
import com.example.demo.exception.IdentityErrorMessageKey;
import com.example.demo.exception.IdentityException;
import com.example.demo.model.InstituteEntity;
import com.example.demo.model.StudentEntity;
import com.example.demo.persistance.Entity;
import com.example.demo.util.Constant;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ElasticSearchOperations {

    private static final Integer SIZE_ELASTIC_SEARCH_MAX_VAL = 9999;
    Logger logger = LoggerFactory.getLogger(ElasticSearchOperations.class);

    @Autowired
    private ElasticsearchClient esClient;

    public Entity saveEntity(Entity entity, String Id, String index) throws IdentityException {
        IndexResponse indexResponse = null;
        try {
            synchronized (entity){
                indexResponse = esClient.index(builder -> builder.index(index)
                        .id(Id)
                        .document(entity));
            }
            logger.debug("Saved the entity. Response {}.Entity:{}",indexResponse, entity);
        } catch (IOException e) {
            logger.error("Exception ",e);
            throw new IdentityException(String.format("Unable to save the entity {} ",entity.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return entity;
    }
    public String deleteEntity(String Id, String index) throws IdentityException {
        logger.debug("Deleting the Entity {}", Id);
        co.elastic.clients.elasticsearch.core.DeleteResponse deleteResponse = null;
        try {
            synchronized (Id){
                deleteResponse = esClient.delete(b -> b.index(index)
                        .id(Id));

            }
            if(deleteResponse.result() == Result.NotFound) {
                throw new IdentityException(String.format("Entity Id not found",Id), HttpStatus.NOT_FOUND);
            }
            logger.debug("Deleted the Entity {}, Delete response {}", Id, deleteResponse);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IdentityException(String.format("Exception while deleting Entity {} .",Id), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return Id;
    }


    public List<InstituteEntity> getAllInstitution() throws IdentityException {
        logger.debug("Getting all the Institution ");
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();
        boolQueryBuilder = boolQueryBuilder
                .filter(q -> q.matchPhrase(t -> t.field(Constant.TYPE).query(Constant.INSTITUTE)));
        BoolQuery.Builder finalBoolQueryBuilder = boolQueryBuilder;
        SearchResponse<InstituteEntity> searchResponse = null;
        try {
            searchResponse = esClient.search(t -> t.index(Constant.INDEX_VISION).size(SIZE_ELASTIC_SEARCH_MAX_VAL)
                    .query(finalBoolQueryBuilder.build()._toQuery()), InstituteEntity.class);
        } catch (IOException e) {
            e.getStackTrace();
            logger.error(e.getMessage());
            throw new IdentityException(ErrorMessageHandler.getMessage(IdentityErrorMessageKey.UNABLE_TO_SEARCH), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        List<Hit<InstituteEntity>> hits = searchResponse.hits().hits();
        logger.info("Number of hits {}", hits.size());
        List<InstituteEntity> userEntities = new ArrayList<>();
        if(hits.size() > 0) {
            for(Hit<InstituteEntity> hit : hits){
                userEntities.add(hit.source());
            }
        }
        return userEntities;
    }

    public InstituteEntity getInstitution(String id) {
        SearchResponse<InstituteEntity> searchResponse = null;
        try {
            BoolQuery boolQuery = BoolQuery.of(b -> b
                    .filter(f -> f.matchPhrase(m -> m.field(Constant.RESOURCE_ID).query(id)))
                    .filter(f -> f.matchPhrase(m -> m.field(Constant.TYPE).query(Constant.INSTITUTE))));
            SearchRequest searchRequest = SearchRequest.of(s -> s
                    .index(Constant.INDEX_VISION)  // Specify the index
                    .query(Query.of(q -> q.bool(boolQuery)))
                    .size(1));
            searchResponse = esClient.search(searchRequest, InstituteEntity.class);

        } catch (IOException e) {
            logger.error("Unable to fetch institute id: {}", id, e);
        }
        List<Hit<InstituteEntity>> hits = searchResponse.hits().hits();
        if (hits != null && !hits.isEmpty()) {
            return hits.get(0).source();
        }
        return null;
    }

    public List<StudentEntity> getAllStudents() throws IdentityException {
        logger.debug("Getting all the Students ");
    BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();
    boolQueryBuilder = boolQueryBuilder
            .filter(q -> q.matchPhrase(t -> t.field(Constant.TYPE).query(Constant.STUDENT)));
    BoolQuery.Builder finalBoolQueryBuilder = boolQueryBuilder;
    SearchResponse<StudentEntity> searchResponse = null;
        try {
        searchResponse = esClient.search(t -> t.index(Constant.INDEX_VISION).size(SIZE_ELASTIC_SEARCH_MAX_VAL)
                .query(finalBoolQueryBuilder.build()._toQuery()), StudentEntity.class);
    } catch (IOException e) {
        e.getStackTrace();
        logger.error(e.getMessage());
        throw new IdentityException(ErrorMessageHandler.getMessage(IdentityErrorMessageKey.UNABLE_TO_SEARCH), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    List<Hit<StudentEntity>> hits = searchResponse.hits().hits();
        logger.info("Number of hits {}", hits.size());
    List<StudentEntity> userEntities = new ArrayList<>();
        if(hits.size() > 0) {
        for(Hit<StudentEntity> hit : hits){
            userEntities.add(hit.source());
        }
    }
        return userEntities;
}

public StudentEntity getStudent(String id) {
    SearchResponse<StudentEntity> searchResponse = null;
    try {
        BoolQuery boolQuery = BoolQuery.of(b -> b
                .filter(f -> f.matchPhrase(m -> m.field(Constant.RESOURCE_ID).query(id)))
                .filter(f -> f.matchPhrase(m -> m.field(Constant.TYPE).query(Constant.STUDENT))));
        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index(Constant.INDEX_VISION)  // Specify the index
                .query(Query.of(q -> q.bool(boolQuery)))
                .size(1));
        searchResponse = esClient.search(searchRequest, StudentEntity.class);

    } catch (IOException e) {
        logger.error("Unable to fetch institute id: {}", id, e);
    }
    List<Hit<StudentEntity>> hits = searchResponse.hits().hits();
    if (hits != null && !hits.isEmpty()) {
        return hits.get(0).source();
    }
    return null;
}
}
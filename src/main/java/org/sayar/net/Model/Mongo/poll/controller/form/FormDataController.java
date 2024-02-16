package org.sayar.net.Model.Mongo.poll.controller.form;

import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.Mongo.poll.model.form.Form;
import org.sayar.net.Model.Mongo.poll.model.form.FormData;
import org.sayar.net.Model.ResponseModel.ResponseContent;
import org.sayar.net.Security.TokenPrinciple;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;


/**
 * Created by sina on 1/10/17.
 */
@RestController
@RequestMapping("/formData")
public class FormDataController {
    private ResponseContent content;
    private final MongoOperations mongoOperations;
    private final GeneralDao<Form> formGeneralDao;
    @Autowired
    private TokenPrinciple tokenPrinciple;
//    private final Toolkit toolkit;
//    private final RequestService requestService;

    @Autowired
    public FormDataController(MongoOperations mongoOperations, GeneralDao<Form> formGeneralDao) {
        this.mongoOperations = mongoOperations;
        this.formGeneralDao = formGeneralDao;
//        this.toolkit = toolkit;
//        this.requestService = requestService;
    }

    public FormData getFormDataOfSampleActivity(String formDataId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(formDataId));
        return mongoOperations.findOne(query, FormData.class);
    }

    public FormData createFormData(FormData formData) {
        return mongoOperations.save(formData);
    }

    public FormData createFormDataOfActivitySample(FormData formData) {
        formData.setSystemCreationDate(new Date());
        return mongoOperations.save(formData);

    }

    public FormData getFormDataForFormAndFormDataDTO(String formDataId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(formDataId));
        return mongoOperations.findOne(query, FormData.class);
    }

    public FormData getFormDataOfRepository(String formDataId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(formDataId));
        return mongoOperations.findOne(query, FormData.class);
    }

    public List<FormData> getFormDataList(List<String> formDataIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(formDataIdList));
        return mongoOperations.find(query, FormData.class);
    }
}

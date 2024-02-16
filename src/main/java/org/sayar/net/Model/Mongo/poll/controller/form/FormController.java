package org.sayar.net.Model.Mongo.poll.controller.form;


import org.sayar.net.Controller.Mongo.activityController.activity.ActivityController;
import org.sayar.net.Model.Mongo.MyModel.Activity;
import org.sayar.net.Model.Mongo.poll.DTO.*;
import org.sayar.net.Model.Mongo.poll.model.form.Form;
import org.sayar.net.Model.Mongo.poll.model.form.FormCategory;
import org.sayar.net.Model.ResponseModel.ResponseContent;
import org.sayar.net.Security.TokenPrinciple;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;


/**
 * Created by sina on 1/10/17.
 */
@RestController
@RequestMapping("/form")
public class FormController {
    private ResponseContent content;
    private final MongoOperations mongoOperations;
    protected TokenPrinciple tokenPrinciple;
    @Autowired
    private ActivityController activityController;

    @Autowired
    private FormCategoryController formCategoryController;

    @Autowired
    public FormController(MongoOperations mongoOperations, TokenPrinciple tokenPrinciple) {
        this.mongoOperations = mongoOperations;
        this.tokenPrinciple = tokenPrinciple;
    }

    @GetMapping()
    public ResponseEntity<?> getAllFormTitle() {

        Criteria criteria = new Criteria();
        try {
            Aggregation aggregation = newAggregation(
                    match(criteria)
                    , lookup("formCategory", "formCategory._id", "_id", "formCategory")
                    , unwind("formCategory")
            );
            AggregationResults<FormDto> groupResults
                    = mongoOperations.aggregate(aggregation, "form", FormDto.class);
            Print.print(groupResults.getMappedResults());

            List<FormDto> result = groupResults.getMappedResults();
            content = new ResponseContent();
            content.setData(result);
            content.setFlag(true);
            return ResponseEntity.ok().body(content);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e);
        }
    }

    @GetMapping("get-all-limit")
    public ResponseEntity<?> getAllLimit() {
        Query query = new Query();
        query.fields()
                .include("id")
                .include("title")
                .include("description")
                .include("formCategoryId");
        List<Form> forms = mongoOperations.find(query, Form.class);
        List<String> formCategoryIdList = new ArrayList<>();
        forms.forEach(form -> formCategoryIdList.add(form.getFormCategoryId()));
        List<FormCategory> formCategoryList = formCategoryController.getFormCategoryList(formCategoryIdList);
        List<FormAndFormCategoryDTO> formAndFormCategoryDTOS = FormAndFormCategoryDTO.map(forms, formCategoryList);
        return ResponseEntity.ok().body(formAndFormCategoryDTOS);
    }

    @GetMapping("list-form-by-category/{formCategoryId}")
    public ResponseEntity<?> getAllFormTitleByCategory(@PathVariable String formCategoryId) {
        try {
            Query query = new Query();
            query.addCriteria(
                    Criteria.where("formCategoryId").is(formCategoryId)
            );
            query.fields().include("title");
            List formList = mongoOperations.find(query, Form.class);
            content.setData(formList);
            return ResponseEntity.ok().body(content);
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(500).body(content);
        }
    }

    @GetMapping("get-one")
    public ResponseEntity<?> getOneForm(@PathParam("formId") String formId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(formId));
        return ResponseEntity.ok().body(mongoOperations.findOne(query, Form.class));
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "one-light-form/{formId}")
    public ResponseEntity<?> getFormTitleAndCategory(@PathVariable String formId) {
        content = new ResponseContent();
        try {
            Query query = new Query();
            query.addCriteria(
                    Criteria.where("id").is(formId)
            );
            query.fields().include("title");
            query.fields().include("formCategoryId");
            Form form = mongoOperations.findOne(query, Form.class);
            content.setData(form);
            content.setFlag(true);
            return ResponseEntity.ok().body(content);
        } catch (Exception e) {
            e.printStackTrace();
            content.setFlag(false);
            return ResponseEntity.status(500).body(content);
        }
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody Form form) {
        if (form.getId() != null && activityController.ifFormExistsInActivity(form.getId())) {
            return ResponseEntity.ok().body("\"این فرم در فرآیند ها استفاده شده است و قابل تغییر نمباشد\"");
        } else {
            content = new ResponseContent();
            mongoOperations.save(form);
            content.setData(form);
            content.setFlag(true);
            return ResponseEntity.ok().body(content);
        }
    }

    @GetMapping("getAllLimit/{companyId}")
    public ResponseEntity<?> getAllLimit(@PathVariable("companyId") String companyId) {
        return ResponseEntity.ok().body(content);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteOne(@PathParam("id") String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("activityLevelList").elemMatch(Criteria.where("formIdCopy.formId").is(id)));

        if (mongoOperations.exists(query, Activity.class)) {
            return ResponseEntity.ok().body("\"برای حذف این فرم ابتدا آن را از قسمت فرایندها پاک کنید\"");
        } else {
            query = new Query();
            query.addCriteria(Criteria.where("id").is(id));
            mongoOperations.remove(query, Form.class);
            return ResponseEntity.ok(true);
        }
    }

    public Form getFirstActivityLevelForm(String formId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(formId));
        return mongoOperations.findOne(query, Form.class);
    }

    public Form getFormOfActivityByFormId(String formId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(formId));
        return mongoOperations.findOne(query, Form.class);
    }

    public Form getFormByActivitySampleFormId(String formId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(formId));
        return mongoOperations.findOne(query, Form.class);
    }

    public List<Form> getFormsOfActivitySamples(List<String> formIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(formIdList));
        query.fields()
                .include("title");
        return mongoOperations.find(query, Form.class);
    }

    public Form getFormForFormAndFormDataDTO(String formId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(formId));
        return mongoOperations.findOne(query, Form.class);
    }

    @GetMapping("if-form-used-in-activity")
    public ResponseEntity<?> ifFormUsedInActivity(@PathParam("formId") String formId) {
        return ResponseEntity.ok().body(activityController.ifFormUsedInActivity(formId));
    }

    @PostMapping("get-all-list-with-pagination")
    public ResponseEntity<?> getAllFormsWithPagination(@RequestBody FormSearchInputDTO formSearchInputDTO,
                                                       Pageable pageable) {

        long skip = pageable.getPageNumber() * pageable.getPageSize();
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        if (formSearchInputDTO.getTitle() != null && !formSearchInputDTO.getTitle().equals("")) {
            criteria.and("title").regex(formSearchInputDTO.getTitle());
        }
        if (formSearchInputDTO.getFormCategoryId() != null && !formSearchInputDTO.getFormCategoryId().equals("")) {
            criteria.and("formCategoryId").is(formSearchInputDTO.getFormCategoryId());
        }

        ProjectionOperation projectionOperation = project()
                .andExpression("id").as("_id")
                .andExpression("description").as("description")
                .andExpression("title").as("name")
                .andExpression("formCategoryId").as("formCategoryId");

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
                , projectionOperation
                , Aggregation.skip(skip)
                , Aggregation.limit(pageable.getPageSize())
        );
        List<FormSearchDTO> searchDTOList = mongoOperations.aggregate(aggregation, Form.class, FormSearchDTO.class).getMappedResults();
        Print.print("searchDTOList", searchDTOList);
        List<String> formCategoryIdList = new ArrayList<>();
        searchDTOList.forEach(searchDTO -> {
            if (searchDTO.getFormCategoryId() != null) {
                formCategoryIdList.add(searchDTO.getFormCategoryId());
            }
        });
        Print.print("formCategoryIdList", formCategoryIdList);
        List<FormCategory> formCategoryList = formCategoryController.getFormCategoryList(formCategoryIdList);
        Print.print("searchDTOList", searchDTOList);
        Print.print("formCategoryList", formCategoryList);
        long count = countNumberOfAllForms(formSearchInputDTO);
        return ResponseEntity.ok().body(new PageImpl<>(
                FormFieldsAndFormCategoryDTO.paginationMap(formCategoryList, searchDTOList),
                pageable,
                count
        ));
    }

    private long countNumberOfAllForms(FormSearchInputDTO formSearchInputDTO) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        if (formSearchInputDTO.getTitle() != null && !formSearchInputDTO.getTitle().equals("")) {
            criteria = Criteria.where("title").regex(formSearchInputDTO.getTitle());
        }
        if (formSearchInputDTO.getFormCategoryId() != null && !formSearchInputDTO.getFormCategoryId().equals("")) {
            criteria = Criteria.where("formCategoryId").is(formSearchInputDTO.getFormCategoryId());
        }
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
        );
        return mongoOperations.aggregate(aggregation, Form.class, FormSearchDTO.class).getMappedResults().size();
    }

    public boolean ifFormCategoryExistsInForm(String deleteId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("formCategoryId").is(deleteId));
        return mongoOperations.exists(query, Form.class);
    }

    @GetMapping("check-if-title-is-unique")
    public ResponseEntity<?> checkIfTitleIsUnique(@PathParam("title") String title) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("title").is(title));
        return ResponseEntity.ok().body(mongoOperations.exists(query, Form.class));
    }

    public List<Form> getFormList(List<String> formIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(formIdList));
        return mongoOperations.find(query, Form.class);
    }
}

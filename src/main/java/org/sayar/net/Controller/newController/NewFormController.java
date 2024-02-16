package org.sayar.net.Controller.newController;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.sayar.net.Controller.Mongo.activityController.activity.ActivityController;
import org.sayar.net.Model.FormGetPage;
import org.sayar.net.Model.Mongo.poll.DTO.FormSearchInputDTO;
import org.sayar.net.Model.NewForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("newForm")
public class NewFormController {

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private ActivityController activityController;

    @PostMapping("save")
    public ResponseEntity<?> createNewForm(@RequestBody NewForm newForm) {
        return ResponseEntity.ok().body(mongoOperations.save(newForm));
    }

    @GetMapping("get-one")
    public ResponseEntity<?> getOneNewForm(@PathParam("id") String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        return ResponseEntity.ok().body(mongoOperations.findOne(query, NewForm.class));
    }

    @GetMapping("get-all")
    public ResponseEntity<?> getAllNewForm() {
        Query query = new Query();
        query.fields()
                .include("id")
                .include("title");
        return ResponseEntity.ok().body(mongoOperations.find(query, NewForm.class));
    }

    @PutMapping("update")
    public ResponseEntity<?> updateNewForm(@RequestBody NewForm newForm) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(newForm.getId()));
        Update update = new Update();
        update.set("title", newForm.getTitle());
        update.set("newElementList", newForm.getNewElementList());
        update.set("description", newForm.getDescription());
        update.set("formCategoryId", newForm.getFormCategoryId());
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, NewForm.class);
        if (updateResult.getModifiedCount() > 0) {
            return ResponseEntity.ok().body(true);
        } else {
            return ResponseEntity.ok().body(false);
        }
    }

    @DeleteMapping("delete")
    public ResponseEntity<?> deleteNewForm(@PathParam("newFormId") String newFormId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(newFormId));
        DeleteResult deleteResult = mongoOperations.remove(query, NewForm.class);
        if (deleteResult.getDeletedCount() > 0) {
            return ResponseEntity.ok().body(true);
        } else {
            return ResponseEntity.ok().body(false);
        }
    }

    @PostMapping("get-all-list-with-pagination")
    public ResponseEntity<?> getAllWithPagination(@RequestBody FormSearchInputDTO formSearchInputDTO, Pageable pageable) {

        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Criteria criteria = new Criteria();
        if (formSearchInputDTO.getTitle() != null && !formSearchInputDTO.getTitle().equals("")) {
            criteria.and("title").regex(formSearchInputDTO.getTitle());
        }

        if (formSearchInputDTO.getFormCategoryId() != null && !formSearchInputDTO.getFormCategoryId().equals("")) {
            criteria.and("formCategoryId").is(formSearchInputDTO.getFormCategoryId());
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.project()
                        .and(ConvertOperators.ToObjectId.toObjectId("$formCategoryId")).as("formCategoryId")
                        .and("title").as("title")
                        .and("id").as("id"),
                Aggregation.lookup("formCategory", "formCategoryId", "_id", "formCategory"),
                Aggregation.project()
                        .and("id").as("id")
                        .and("title").as("title")
                        .and("formCategory.id").as("formCategoryId")
                        .and("formCategory.title").as("formCategoryTitle"),
                skipOperation,
                limitOperation
        );
        List<FormGetPage> formGetPageList = mongoOperations.aggregate(aggregation, NewForm.class, FormGetPage.class).getMappedResults();

        return ResponseEntity.ok().body(
                new PageImpl<>(
                        formGetPageList,
                        pageable,
                        this.countGetPage(formSearchInputDTO)
                )
        );

    }

    private long countGetPage(FormSearchInputDTO formSearchInputDTO) {

        Criteria criteria = new Criteria();

        if (formSearchInputDTO.getTitle() != null && !formSearchInputDTO.getTitle().equals("")) {
            criteria.and("title").regex(formSearchInputDTO.getTitle());
        }

        if (formSearchInputDTO.getFormCategoryId() != null && !formSearchInputDTO.getFormCategoryId().equals("")) {
            criteria.and("formCategoryId").is(formSearchInputDTO.getFormCategoryId());
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
        );

        return mongoOperations.aggregate(aggregation, NewForm.class, FormGetPage.class).getMappedResults().size();
    }

    @GetMapping("if-form-used-in-activity")
    public ResponseEntity<?> ifFormUsedInActivity(@PathParam("formId") String formId) {
        return ResponseEntity.ok().body(activityController.ifFormUsedInActivity(formId));
    }

    public List<NewForm> getFormList(List<String> formIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(formIdList));
        return mongoOperations.find(query, NewForm.class);
    }

    public NewForm getFormByActivitySampleFormId(String formId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(formId));
        return mongoOperations.findOne(query, NewForm.class);
    }

    public NewForm getFormForFormAndFormDataDTO(String formId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(formId));
        return mongoOperations.findOne(query, NewForm.class);
    }

    public NewForm getFormOfActivityByFormId(String formId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(formId));
        return mongoOperations.findOne(query, NewForm.class);
    }
}

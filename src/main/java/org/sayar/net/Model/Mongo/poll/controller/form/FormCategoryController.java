package org.sayar.net.Model.Mongo.poll.controller.form;


import org.sayar.net.Model.Mongo.poll.model.form.FormCategory;
import org.sayar.net.Model.ResponseModel.ResponseContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * Created by morteza on 11/16/17.
 */
@RestController()
@RequestMapping("/formCategory")
public class FormCategoryController {

    @Autowired
    private FormController formController;
    private ResponseContent content;
    private MongoOperations mongoOperations;

    @Autowired
    public FormCategoryController(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @RequestMapping(method = RequestMethod.POST, value = "create")
    public ResponseEntity<?> create(@RequestBody FormCategory formCategory) {
        content = new ResponseContent();
        content.setFlag(true);
        content.setData(mongoOperations.save(formCategory));
        return ResponseEntity.ok().body(content);
    }

    @RequestMapping(method = RequestMethod.GET, value = "get-all")
    public ResponseEntity<?> getAll() {
        content = new ResponseContent();
        content.setData(mongoOperations.findAll(FormCategory.class));
        content.setFlag(true);
        return ResponseEntity.ok().body(content);
    }

    @GetMapping("get-one")
    public ResponseEntity<?> getById(@RequestParam("formCategoryId") String formCategoryId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(formCategoryId));
        return ResponseEntity.ok().body(mongoOperations.findOne(query, FormCategory.class));
    }

    @DeleteMapping("delete")
    public ResponseEntity<?> delete(@RequestParam("deleteId") String deleteId) {
        System.out.println(deleteId + "  dsbhcxdwvcgwvdyc");
        if (formController.ifFormCategoryExistsInForm(deleteId)) {
            return ResponseEntity.ok().body("\" این دسته بندی در فرم ها استفاده شده و امکان حذف آن نمیباشد برای انجام حذف ابتدا این دسته را از قسمت فرم ها پاک کنید \"");
        } else {
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(deleteId));
            FormCategory formCategory = mongoOperations.findOne(query, FormCategory.class);
            if (formCategory != null) {
                mongoOperations.remove(query, FormCategory.class);
                return ResponseEntity.ok().body(true);
            } else
                return ResponseEntity.ok().body(false);
        }
    }

    @PutMapping("update")
    public ResponseEntity<?> update(@RequestBody FormCategory formCategory) {
        try {
            content = new ResponseContent();
            mongoOperations.save(formCategory);
            content.setData(formCategory);
            content.setFlag(true);
            return ResponseEntity.ok().body(content);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e);
        }
    }

    @GetMapping("check-if-title-is-unique")
    public ResponseEntity<?> checkIfTitleIsUnique(@PathParam("title") String title) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("title").is(title));
        return ResponseEntity.ok().body(mongoOperations.exists(query, FormCategory.class));
    }

    public List<FormCategory> getFormCategoryList(List<String> formCategoryIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(formCategoryIdList));
        return mongoOperations.find(query, FormCategory.class);
    }

    @GetMapping("get-all-form-category-with-pagination")
    public ResponseEntity<?> getAllFormCategoryWithPagination(@PathParam("term") String term, Pageable pageable, Integer element) {
        List<FormCategory> formCategoryList = this.getFormCategoryWithPagination(term, pageable, element);
        long count = this.countNumberOfCategory(term);
        Page<FormCategory> formCategoryPage = this.getPagedFormCategory(formCategoryList, pageable, count);
        return ResponseEntity.ok().body(formCategoryPage);
    }

    private Page<FormCategory> getPagedFormCategory(List<FormCategory> formCategoryList, Pageable pageable, long count) {
        return new PageImpl<>(
                formCategoryList,
                pageable,
                count
        );
    }

    private long countNumberOfCategory(String term) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        if (term != null && !term.equals("")) {
            criteria.and("title").regex(term);
        }
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
        );
        return mongoOperations.aggregate(aggregation, FormCategory.class, FormCategory.class).getMappedResults().size();
    }

    private List<FormCategory> getFormCategoryWithPagination(String term, Pageable pageable, Integer element) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        if (term != null && !term.equals("")) {
            criteria.and("title").regex(term);
        }
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                skipOperation,
                limitOperation
        );
        return mongoOperations.aggregate(aggregation, FormCategory.class, FormCategory.class).getMappedResults();
    }
}

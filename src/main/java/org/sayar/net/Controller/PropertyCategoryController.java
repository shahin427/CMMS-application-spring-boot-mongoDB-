package org.sayar.net.Controller;

import org.sayar.net.Model.PropertyCategory;
import org.sayar.net.Service.newService.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
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
@RequestMapping("property-category")
public class PropertyCategoryController {

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private PropertyService propertyService;

    @PostMapping("save")
    public ResponseEntity<?> savePropertyCategory(@RequestBody PropertyCategory propertyCategory) {
        return ResponseEntity.ok().body(mongoOperations.save(propertyCategory));
    }

    @GetMapping("get-all")
    public ResponseEntity<?> getAllPropertyCategory() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        return ResponseEntity.ok().body(mongoOperations.find(query, PropertyCategory.class));
    }

    @GetMapping("get-one")
    public ResponseEntity<?> getOnePropertyCategory(@PathParam("id") String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(id));
        return ResponseEntity.ok().body(mongoOperations.findOne(query, PropertyCategory.class));
    }

    @PutMapping("update")
    public ResponseEntity<?> updatePropertyCategory(@RequestBody PropertyCategory propertyCategory) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(propertyCategory.getId()));
        Update update = new Update();
        update.set("title", propertyCategory.getTitle());
        update.set("code", propertyCategory.getCode());
        return ResponseEntity.ok().body(mongoOperations.updateFirst(query, update, PropertyCategory.class));
    }

    @GetMapping("get-all-with-pagination")
    public ResponseEntity<?> getAllWithPagination(@PathParam("term") String term, @PathParam("code") String code, Pageable pageable) {

        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        if (term != null && !term.equals("")) {
            criteria.and("title").regex(term);
        }
        if (code != null && !code.equals("")) {
            criteria.and("code").regex(code);
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                skipOperation,
                limitOperation
        );

        List<PropertyCategory> propertyCategories = mongoOperations.aggregate(aggregation, PropertyCategory.class, PropertyCategory.class).getMappedResults();
        return ResponseEntity.ok().body(
                new PageImpl<>(
                        propertyCategories,
                        pageable,
                        this.countAllPropertyCategories(term, code)
                )
        );
    }

    private long countAllPropertyCategories(String term, String code) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        if (term != null && !term.equals("")) {
            criteria.and("title").regex(term);
        }
        if (code != null && !code.equals("")) {
            criteria.and("code").is(code);
        }
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
        );
        return mongoOperations.aggregate(aggregation, PropertyCategory.class, PropertyCategory.class).getMappedResults().size();
    }

    @DeleteMapping("delete")
    public ResponseEntity<?> deletePropertyCategory(@PathParam("id") String id) {
        if (propertyService.checkIfPropertyCategoryExistsInProperty(id)) {
            return ResponseEntity.ok().body("\"برای حذف این دسته بندی ویژگی ابتدا آن را از قسمت ویژگی ها پاک کنید\"");
        } else {
            Query query = new Query();
            query.addCriteria(Criteria.where("deleted").ne(true));
            query.addCriteria(Criteria.where("id").is(id));
            Update update = new Update();
            update.set("deleted", true);
            mongoOperations.updateFirst(query, update, PropertyCategory.class);
            return ResponseEntity.ok().body(true);
        }
    }

    @GetMapping("check-if-code-exists")
    public ResponseEntity<?> checkIfCodeExists(@PathParam("code") String code) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("code").is(code));
        return ResponseEntity.ok().body(mongoOperations.exists(query, PropertyCategory.class));
    }

    @GetMapping("get-all-property-category-title")
    public ResponseEntity<?> getAllPropertyCategoryTitle() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields()
                .include("id")
                .include("title");
        return ResponseEntity.ok().body(mongoOperations.find(query, PropertyCategory.class));
    }


}

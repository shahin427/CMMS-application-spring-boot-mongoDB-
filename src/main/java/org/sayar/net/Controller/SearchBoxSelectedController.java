package org.sayar.net.Controller;

import org.sayar.net.Model.SearchBoxSelected;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller("search-box-selected")
public class SearchBoxSelectedController {

    @Autowired
    private MongoOperations mongoOperations;

    @PostMapping("save")
    public ResponseEntity<?> createSearchBox(@RequestBody SearchBoxSelected searchBoxSelected) {
        return ResponseEntity.ok().body(mongoOperations.save(searchBoxSelected));
    }

    @PutMapping("update")
    public ResponseEntity<?> updateSearchBox(@RequestBody SearchBoxSelected searchBoxSelected) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(searchBoxSelected.getId()));
        Update update = new Update();
        update.set("title", searchBoxSelected.isTitle());
        update.set("code", searchBoxSelected.isCode());
        update.set("statusId", searchBoxSelected.isStatusId());
        update.set("assetId", searchBoxSelected.isAssetId());
        update.set("projectId", searchBoxSelected.isProjectId());
        update.set("priority", searchBoxSelected.isPriority());
        update.set("maintenanceType", searchBoxSelected.isMaintenanceType());
        update.set("fromSchedule", searchBoxSelected.isFromSchedule());
        update.set("startDate", searchBoxSelected.isStartDate());
        update.set("endDate", searchBoxSelected.isEndDate());
        return ResponseEntity.ok().body(mongoOperations.updateFirst(query, update, SearchBoxSelected.class));
    }

}

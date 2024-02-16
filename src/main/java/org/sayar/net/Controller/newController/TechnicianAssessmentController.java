package org.sayar.net.Controller.newController;


import org.sayar.net.Model.DTO.TechnicianAveragePointDTO;
import org.sayar.net.Model.TechnicianAssessment;
import org.sayar.net.Service.WorkRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.text.DecimalFormat;
import java.util.List;

@RestController
@RequestMapping("technician-assessment")
public class TechnicianAssessmentController {

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private WorkRequestService workRequestService;


    @PostMapping("save")
    public ResponseEntity<?> saveAssessment(@RequestBody List<TechnicianAssessment> technicianAssessmentList) {
        String workRequestId = technicianAssessmentList.get(0).getWorkRequestId();
        workRequestService.setAssessmentFalse(workRequestId);
        mongoOperations.insertAll(technicianAssessmentList);
        return ResponseEntity.ok().body(true);
    }

    @GetMapping("get-all-average-point-of-personnel")
    public ResponseEntity<?> averagePointOfPersonnel(@PathParam("userId") String userId) {
        Criteria criteria = new Criteria();

        if (userId != null && !userId.equals(""))
            criteria.and("userId").is(userId);

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("userId")
                        .first("userId").as("userId")
                        .count().as("count")
                        .sum("point").as("totalPoint"),
                Aggregation.project()
                        .and(ArithmeticOperators.Divide.valueOf("totalPoint").divideBy("count")).as("average")
                        .and(ConvertOperators.ToObjectId.toObjectId("$userId")).as("userId"),
                Aggregation.lookup("user", "userId", "_id", "user"),
                Aggregation.project()
                        .and("average").as("average")
                        .and("userId").as("userId")
                        .and("user.name").as("userName")
                        .and("user.family").as("userFamily")
        );
        List<TechnicianAveragePointDTO> technicianAveragePointDTOList = mongoOperations.aggregate(aggregation, TechnicianAssessment.class, TechnicianAveragePointDTO.class).getMappedResults();
        DecimalFormat df = new DecimalFormat("0.0");
        technicianAveragePointDTOList.forEach(technicianAveragePointDTO -> {
            technicianAveragePointDTO.setAverage(Double.parseDouble(df.format(technicianAveragePointDTO.getAverage())));
        });
        return ResponseEntity.ok().body(technicianAveragePointDTOList);
    }
}

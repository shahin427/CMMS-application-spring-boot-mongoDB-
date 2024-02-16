package org.sayar.net.Controller.newController;

import org.sayar.net.Model.Mongo.UserTask;
import org.sayar.net.Model.newModel.Helper.ResponseContent;
import org.sayar.net.Model.newModel.Task.model.LaborTask;
import org.sayar.net.Service.newService.LaborTaskService;
import org.sayar.net.Tools.PatternList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("labortask")
public class LaborTaskController {

    @Autowired
    private LaborTaskService service;



    @RequestMapping(method = RequestMethod.POST,value = "/upload/{workOrderId:"+ PatternList.digitNumber+"}")
    public ResponseEntity<?> addTask(@RequestBody LaborTask entity, @PathVariable String workOrderId){
        return new ResponseContent().sendOkResponseEntity(
                "",
                service.addTask(entity,workOrderId)
        );
    }


    @RequestMapping(method = RequestMethod.GET,value = "/myTask")
    public ResponseEntity<?> myTask(){
        return new ResponseContent().sendOkResponseEntity(
                "",
                service.myTask()
        );
    }
    @RequestMapping(method = RequestMethod.GET,value = "/workOrder/{workOrderId:"+ PatternList.digitNumber+"}")
    public ResponseEntity<?> workOrderTask(@PathVariable String workOrderId){
        return new ResponseContent().sendOkResponseEntity(
                "",
                service.workOrderTask(workOrderId)
        );
    }
    @RequestMapping(method = RequestMethod.PUT,value = "/update/myTask")
    public ResponseEntity<?> updateTask(@RequestBody UserTask entity){
        return new ResponseContent().sendOkResponseEntity(
                "",
                service.updateTask(entity)
        );
    }


    @RequestMapping(method = RequestMethod.GET, value = "{taskId}")
    private ResponseEntity<?> findOne(@PathVariable String taskId) {
        return new org.sayar.net.Model.ResponseModel.ResponseContent().sendOkResponseEntity("", service.findOneById(taskId, LaborTask.class));
    }


    @RequestMapping(method = RequestMethod.GET, value = "all")
    public ResponseEntity<?> getAll() {
        return new org.sayar.net.Model.ResponseModel.ResponseContent().sendOkResponseEntity("", service.findAllNotLogicDeleted(LaborTask.class));
    }

    @RequestMapping(method = RequestMethod.POST, value = "save")
    public ResponseEntity<?> save(@RequestBody LaborTask task) {
        return new org.sayar.net.Model.ResponseModel.ResponseContent().sendOkResponseEntity("", service.save(task));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "update")
    public ResponseEntity<?> update(@RequestBody LaborTask task) {
        return new org.sayar.net.Model.ResponseModel.ResponseContent().sendOkResponseEntity("", service.save(task));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "{taskId}")
    private ResponseEntity<?> delete(@PathVariable String taskId) {
        return new org.sayar.net.Model.ResponseModel.ResponseContent().sendOkResponseEntity("", service.logicDeleteById(taskId, LaborTask.class));
    }

}

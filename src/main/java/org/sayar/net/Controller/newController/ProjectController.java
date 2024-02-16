package org.sayar.net.Controller.newController;

import org.sayar.net.Model.Asset.AssignedToGroup;
import org.sayar.net.Model.Asset.AssignedToPerson;
import org.sayar.net.Model.DTO.ProjectDTOCodeAndName;
import org.sayar.net.Model.newModel.Project;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.service.ScheduleMaintananceService;
import org.sayar.net.Service.newService.ProjectService;
import org.sayar.net.Service.newService.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("project")
public class ProjectController {


    @Autowired
    private ProjectService projectService;
    @Autowired
    private WorkOrderService workOrderService;
    @Autowired
    private ScheduleMaintananceService scheduleMaintananceService;


    @PostMapping("save")
    public ResponseEntity<?> postOneProject(@RequestBody Project project) {
        return ResponseEntity.ok().body(projectService.postOneProject(project));
    }

    @PostMapping("get-all-by-pagination")
    public ResponseEntity<?> getAllFilterAndPagination(@RequestBody ProjectDTOCodeAndName projectDTOCodeAndName, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(projectService.getAllFilterAndPagination(projectDTOCodeAndName, pageable, totalElement));
    }

    @DeleteMapping("delete")
    public ResponseEntity<?> delete(@PathParam("projectId") String projectId) {
        if (workOrderService.ifProjectExistsInWorkOrder(projectId)) {
            return ResponseEntity.ok().body("\"برای حذف این پروژه ابتدا آن را از قسمت دستور کارها پاک کنید\"");
        } else if (scheduleMaintananceService.ifProjectExistsInScheduleMaintenance(projectId)) {
            return ResponseEntity.ok().body("\"برای حذف این پروژه ابتدا آن را از قسمت زمانبندی نت پاک کنید\"");
        } else {
            return ResponseEntity.ok().body(projectService.logicDeleteById(projectId, Project.class));
        }
    }

    @GetMapping("check-project-code")
    public ResponseEntity<?> checkProjectCode(@PathParam("code") String code) {
        return ResponseEntity.ok().body(projectService.checkProjectCode(code));
    }

    @GetMapping("get-all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().body(projectService.getAllProjectNameAndCode());
    }

    @GetMapping("get-one")
    public ResponseEntity<?> getOne(@PathParam("projectId") String projectId) {
        return ResponseEntity.ok().body(projectService.getOne(projectId));
    }

    @PutMapping("update")
    public ResponseEntity<?> updateProject(@RequestBody Project project, @PathParam("projectId") String projectId) {
        return ResponseEntity.ok().body(projectService.updateProject(project, projectId));
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteProject(@PathParam("projectId") String projectId) {
        return ResponseEntity.ok().body(projectService.logicDeleteById(projectId, Project.class));
    }

    @GetMapping("get-personnel-of-project")
    public ResponseEntity<?> getPersonnelOfProject(@PathParam("projectId") String projectId) {
        return ResponseEntity.ok().body(projectService.getPersonnelOfProject(projectId));
    }

    @GetMapping("get-personnel-group-of-project")
    public ResponseEntity<?> getPersonnelGroupOfProject(@PathParam("projectId") String projectId) {
        return ResponseEntity.ok().body(projectService.getPersonnelGroupOfProject(projectId));
    }

    @PutMapping("add-personnel-to-project")
    public ResponseEntity<?> addPersonnelToProject(@PathParam("projectId") String projectId, @RequestBody List<AssignedToPerson> assignedToPersonList) {
        return ResponseEntity.ok().body(projectService.addPersonnelToProject(projectId, assignedToPersonList));
    }

    @PutMapping("add-group-personnel-to-project")
    public ResponseEntity<?> addGroupPersonnelToProject(@PathParam("projectId") String projectId, @RequestBody List<AssignedToGroup> assignedToGroupList) {
        return ResponseEntity.ok().body(projectService.addGroupPersonnelToProject(projectId, assignedToGroupList));
    }

//     @GetMapping("get-project-schedule-maintenance")
//    public ResponseEntity<?> getProjectScheduleMaintenance(@PathParam("projectId") String projectId) {
//        return ResponseEntity.ok().body(projectService.getProjectScheduleMaintenance(projectId));
//    }

}

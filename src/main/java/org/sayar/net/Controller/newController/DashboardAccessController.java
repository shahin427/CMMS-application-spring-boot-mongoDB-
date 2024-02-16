package org.sayar.net.Controller.newController;

import org.sayar.net.Model.DashboardAccess;
import org.sayar.net.Security.TokenPrinciple;
import org.sayar.net.Service.DashboardAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("dashboard-access")
public class DashboardAccessController {
    @Autowired
    private DashboardAccessService dashboardAccessService;

    @Autowired
    private TokenPrinciple tokenPrinciple;
    @PostMapping("create-user-dashboard-access")
    public ResponseEntity<?> createUserDashboardAccess(@RequestBody DashboardAccess dashboardAccess) {
        String creatorId = tokenPrinciple.getUserId();
        dashboardAccess.setCreatorId(creatorId);
        return ResponseEntity.ok().body(dashboardAccessService.createUserDashboardAccess(dashboardAccess));
    }

    @GetMapping("get-user-dashboard-access")
    public ResponseEntity<?> getUserDashboardAccess(@PathParam("userId") String userId) {
        String creatorId = tokenPrinciple.getUserId();
        userId = creatorId;
        return ResponseEntity.ok().body(dashboardAccessService.getUserDashboardAccess(userId, creatorId));
    }
}

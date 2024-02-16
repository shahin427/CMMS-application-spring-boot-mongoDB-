package org.sayar.net.Controller.newController;

import org.sayar.net.Model.DTO.CompanySearchDTO;
import org.sayar.net.Model.ResponseModel.ResponseContent;
import org.sayar.net.Model.newModel.Company;
import org.sayar.net.Service.newService.AssetService;
import org.sayar.net.Service.newService.CompanyService;
import org.sayar.net.Service.newService.PartService;
import org.sayar.net.Service.newService.WarrantyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("company")
public class CompanyController {

    private final CompanyService companyService;
    private final AssetService assetService;
    private final WarrantyService warrantyService;
    private final PartService partService;

    @Autowired
    public CompanyController(CompanyService companyService, AssetService assetService, WarrantyService warrantyService, PartService partService) {
        this.companyService = companyService;
        this.assetService = assetService;
        this.warrantyService = warrantyService;
        this.partService = partService;
    }


    @RequestMapping(method = RequestMethod.GET, value = "all/treenode")
    public ResponseEntity<?> getWorkOrderTreeNode(@RequestParam(required = false, defaultValue = "") String searchTerm) {
        if (searchTerm.isEmpty()) {
            return new ResponseContent().sendOkResponseEntity("", companyService.getCompanyTreeNode());
        } else {
            return new ResponseContent().sendOkResponseEntity("", companyService.getCompanyTreeNodeSearch(searchTerm));
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all/compress")
    public ResponseEntity<?> getCompressAllAsset() {
        return ResponseEntity.ok().body(companyService.getCompressAll());
    }

    @RequestMapping(method = RequestMethod.GET, value = "all/treenode/search/{searchTerm}")
    public ResponseEntity<?> getWorkOrderTreeNodeSearch(@PathVariable String searchTerm) {
        return ResponseEntity.ok().body(companyService.getCompanyTreeNodeSearch(searchTerm));
    }

    @RequestMapping(method = RequestMethod.GET, value = "supplier")
    public ResponseEntity<?> getSupplierCompany() {
//        return new ResponseContent().sendOkResponseEntity("", companyService.getSupplier(tokenPrinciple.getOrganCode()));
        return null;
    }

    @GetMapping("get-one")
    public ResponseEntity<?> findOne(@PathParam("companyId") String companyId) {
        return ResponseEntity.ok().body(companyService.getOne(companyId));
    }

    @RequestMapping(method = RequestMethod.GET, value = "all")
    public ResponseEntity<?> getAll() {
        return new ResponseContent().sendOkResponseEntity("", companyService.findAllNotLogicDeleted(Company.class));
    }

    @RequestMapping(method = RequestMethod.POST, value = "save")
    public ResponseEntity<?> saveCompany(@RequestBody Company company) {
        return new ResponseContent().sendOkResponseEntity("", companyService.postOne(company));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "update")
    public ResponseEntity<?> update(@RequestBody Company company) {
        return ResponseEntity.ok().body(companyService.updateCompany(company));
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathParam("companyId") String companyId) {

        if (assetService.ifCompanyExistsInAsset(companyId)) {
            return ResponseEntity.ok().body("{\"برای حذف این شرکت ابتدا آن را از قسمت دارایی ها حذف نمایید\"");
        } else if (partService.ifCompanyExistsInPart(companyId)) {
            return ResponseEntity.ok().body("{\"برای حذف این شرکت ابتدا آن را از قسمت قطعات حذف نمایید\"");
        } else if (warrantyService.ifCompanyExistsInWarranty(companyId)) {
            return ResponseEntity.ok().body("{\"برای حذف این شرکت ابتدا آن را از قسمت گارانتی ها حذف نمایید\"");
        } else {
            return ResponseEntity.ok().body(companyService.logicDeleteById(companyId, Company.class));
        }
    }

    @PostMapping("by-id-list")
    public ResponseEntity<?> getByIdList(@RequestBody List<String> companyIdList) {
        return new ResponseContent().sendOkResponseEntity("", companyService.getByIdList(companyIdList));
    }

    @GetMapping("code-unique-check")
    public ResponseEntity<?> codeUniqueCheck(@RequestParam("code") String code) {
        return ResponseEntity.ok().body(companyService.codeUniqueCheck(code));
    }

    @PostMapping("get-all-by-pagination")
    public ResponseEntity<?> getAllByTermAndPagination(@RequestBody CompanySearchDTO companySearchDTO, Pageable pageable) {
        return ResponseEntity.ok().body(companyService.getAllByTermAndPagination(companySearchDTO, pageable));
    }

    @GetMapping("get-all")
    public ResponseEntity<?> getAllCompany() {
        return ResponseEntity.ok().body(companyService.getAllCompany());
    }
}

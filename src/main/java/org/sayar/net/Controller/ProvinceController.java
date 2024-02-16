package org.sayar.net.Controller;


import org.sayar.net.Model.ResponseModel.ResponseContent;
import org.sayar.net.Model.newModel.Location.Province;
import org.sayar.net.Service.ProvinceService;
import org.sayar.net.Service.newService.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/province")
public class ProvinceController {
    @Autowired
    private ProvinceService service;
    @Autowired
    private CityService cityService;
    @Autowired
    private OrganService organService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private StorageService storageService;

    @RequestMapping(method = RequestMethod.GET, value = "city/{provinceId}")
    public ResponseEntity<?> getCity(@PathVariable String provinceId) {
        return ResponseEntity.ok().body(service.getCities(provinceId));
    }

    @RequestMapping(method = RequestMethod.GET, value = "organ/{organId}")
    public ResponseEntity<?> getProvinceByOrganId(@PathVariable String organId) {
        return new ResponseContent().sendOkResponseEntity("", service.getProvinceByOrganId(organId));
    }

    @RequestMapping(method = RequestMethod.GET, value = "nameid")
    public ResponseEntity<?> getAllNameIdAndLocation() {
        return ResponseEntity.ok().body(service.getAllCityNameIdAndLocation());
    }

    @GetMapping("/get-one")
    public ResponseEntity<?> findOne(@PathParam("provinceId") String provinceId) {
        return ResponseEntity.ok().body(service.findOneById(provinceId, Province.class));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "update")
    public ResponseEntity<?> save(@RequestBody Province province) {
        return ResponseEntity.ok().body(service.updateProvince(province));
    }

    @RequestMapping(method = RequestMethod.POST, value = "save")
    public ResponseEntity<?> update(@RequestBody Province province) {
        return ResponseEntity.ok().body(service.saveOne(province));
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathParam("provinceId") String provinceId) {

        if (cityService.ifProvinceExistsInCity(provinceId)) {
            return ResponseEntity.ok().body("\"برای حذف این استان ابتدا شهرهای آن را پاک کنید\"");
        } else if (organService.ifProvinceExistsInOrganization(provinceId)) {
            return ResponseEntity.ok().body("\"برای حذف این استان ابتدا سایت هایی که در این استان قرار دارند را پاک کنید\"");
        } else if (companyService.ifProvinceExistsInCompany(provinceId)) {
            return ResponseEntity.ok().body("\"برای حذف این استان ابتدا شرکت هایی که در این استان قرار دارند را پاک کنید\"");
        } else if (storageService.ifProvinceExistsInStorage(provinceId)) {
            return ResponseEntity.ok().body("\"برای حذف این استان ابتدا انبارهایی که در این استان قرار دارند را پاک کنید\"");
        } else {
            return ResponseEntity.ok().body(service.logicDeleteById(provinceId, Province.class));
        }
    }

    @GetMapping("get-all-by-term")
    public ResponseEntity<?> getAllProvince(@PathParam("term") String term) {
        return ResponseEntity.ok().body(service.getAllProvince(term));
    }

    @GetMapping("get-all-by-pagination")
    public ResponseEntity<?> getAllByPagination(@PathParam("term") String term, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(service.getAllByPagination(term, pageable, totalElement));
    }
}

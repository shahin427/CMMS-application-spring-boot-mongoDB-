package org.sayar.net.Controller.newController;


import org.sayar.net.Model.newModel.Location.City;
import org.sayar.net.Service.newService.*;
import org.sayar.net.exceptionHandling.ApiInputIsInComplete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/city")
public class CityController {
    @Autowired
    private CityService service;
    @Autowired
    private OrganService organService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private AssetService assetService;


    @GetMapping("/organization")
    public ResponseEntity<?> getCityOrganes(@RequestParam("cityId") String cityId, @RequestParam(value = "parentOrganId", required = false) String parentOrgan) {
        return ResponseEntity.ok().body(service.getCityOrganization(cityId, parentOrgan));
    }

    @GetMapping("get-one")
    public ResponseEntity<?> findOne(@PathParam("cityId") String cityId) {
        if (cityId == null) {
            HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
            throw new ApiInputIsInComplete("آی دی فرستاده نمیشود", httpStatus);
        } else {
            return ResponseEntity.ok().body(service.findOneById(cityId));
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().body(service.findAll(City.class));
    }

    @RequestMapping(method = RequestMethod.POST, value = "save")
    public ResponseEntity<?> save(@RequestBody City city) {
        return ResponseEntity.ok().body(service.postOne(city));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "update")
    public ResponseEntity<?> update(@RequestBody City city) {
        return ResponseEntity.ok().body(service.updateCity(city));
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathParam("cityId") String cityId) {
        if (organService.ifCityExistsInOrganization(cityId)) {
            return ResponseEntity.ok().body("{\"برای خذف این شهر ابتدا سایت هایی که در این شهر قرار دارند را پاک کنید\"");
        } else if (companyService.ifCityExistsInCompany(cityId)) {
            return ResponseEntity.ok().body("{\"برای خذف این شهر ابتدا شرکت هایی را که در این شهر قرار دارند را پاک کنید\"");
        } else if (storageService.ifCityExistsInStorage(cityId)) {
            return ResponseEntity.ok().body("{\"برای خذف این شهر ابتدا انبارهایی را که در این شهر قرار دارند را پاک کنید\"");
        } else if (assetService.ifCityExistsInAsset(cityId)) {
            return ResponseEntity.ok().body("{\"برای خذف این شهر ابتدا دارایی هایی را که در این شهر قرار دارند را پاک کنید\"");
        } else {
            return ResponseEntity.ok().body(service.deleteOneCity(cityId));
        }
    }

    @GetMapping("get-all-by-province-id")
    public ResponseEntity<?> getAllByProvinceId(@PathParam("provinceId") String provinceId) {
        if (provinceId == null) {
            HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
            throw new ApiInputIsInComplete("آی دی فرستاده نمیشود", httpStatus);
        } else {
            return ResponseEntity.ok().body(service.getAllByProvinceId(provinceId));
        }
    }

    @GetMapping("get-all-by-pagination-by-province_id")
    public ResponseEntity<?> getAllByPaginationByProvinceId(@PathParam("provinceId") String provinceId, @PathParam("term") String term, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(service.getAllByPaginationByProvinceId(provinceId, term, pageable, totalElement));
    }
}

package org.sayar.net.Controller.newController;

import org.sayar.net.Model.DTO.CurrencyDTO;
import org.sayar.net.Model.ResponseModel.ResponseContent;
import org.sayar.net.Model.newModel.Currency;
import org.sayar.net.Service.newService.BudgetService;
import org.sayar.net.Service.newService.CompanyService;
import org.sayar.net.Service.newService.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("currency")
public class CurrencyController {
    @Autowired
    private CurrencyService service;
    @Autowired
    private BudgetService budgetService;
    @Autowired
    private CompanyService companyService;

    @RequestMapping(method = RequestMethod.GET, value = "/get-one")
    public ResponseEntity<?> findOne(@PathParam("currencyId") String currencyId) {
        return ResponseEntity.ok().body(service.findOneById(currencyId, Currency.class));
    }

    @RequestMapping(method = RequestMethod.GET, value = "get-all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().body(service.getAllCurrency());
    }

    @RequestMapping(method = RequestMethod.POST, value = "save")
    public ResponseEntity<?> saveCurrency(@RequestBody Currency currency) {
        return new ResponseContent().sendOkResponseEntity("", service.saveCurrency(currency));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "update")
    public ResponseEntity<?> update(@RequestBody Currency currency) {
        return ResponseEntity.ok().body(service.updateCurrency(currency));
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathParam("currencyId") String currencyId) {
        if (budgetService.ifCurrencyExistsInBudget(currencyId)) {
            return ResponseEntity.ok().body("{\"برای خذف این واحد پولی ابتدا آن را از قسمت بودجه ها پاک کنید\"");
        } else if (companyService.chickIfCurrencyExistsInCompany(currencyId)) {
            return ResponseEntity.ok().body("{\"برای خذف این واحد پولی ابتدا آن را از قسمت شرکت ها پاک کنید\"");
        } else {
            return ResponseEntity.ok().body(service.logicDeleteById(currencyId, Currency.class));
        }
    }

    @GetMapping("check-if-currency-is-unique")
    public ResponseEntity<?> checkIfCurrencyIsUnique(@PathParam("isoCode") String isoCode, @PathParam("title") String title) {
        return ResponseEntity.ok().body(service.checkIfCurrencyIsUnique(isoCode, title));
    }

    @PostMapping("get-all-by-pagination")
    public ResponseEntity<?> getAllByPagination(@RequestBody CurrencyDTO currencyDTO, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(service.getAllByPagination(currencyDTO, pageable, totalElement));
    }

    @PostMapping("check-if-title-and-iso-code-exist")
    public ResponseEntity<?> checkIfTitleAndIsoCodeExist(@RequestBody CurrencyDTO currencyDTO) {
        return ResponseEntity.ok().body(service.checkIfTitleAndIsoCodeExist(currencyDTO));
    }
}

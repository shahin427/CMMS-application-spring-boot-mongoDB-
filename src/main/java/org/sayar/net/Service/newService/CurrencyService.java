package org.sayar.net.Service.newService;


import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.DTO.CurrencyDTO;
import org.sayar.net.Model.newModel.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CurrencyService extends GeneralService<Currency> {

    Currency saveCurrency(Currency currency);

    Currency updateCurrency(Currency currency);

    boolean checkIfCurrencyIsUnique(String isoCode, String title);

    List<Currency> getAllCurrency();

    Page<Currency> getAllByPagination(CurrencyDTO currencyDTO, Pageable pageable, Integer totalElement);

    boolean checkIfTitleAndIsoCodeExist(CurrencyDTO currencyDTO);
}

package org.sayar.net.Dao.NewDao;


import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.DTO.CurrencyDTO;
import org.sayar.net.Model.newModel.Currency;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CurrencyDao extends GeneralDao<Currency> {
    Currency saveCurrency(Currency currency);

    Currency updateCurrency(Currency currency);

    boolean checkIfCurrencyIsUnique(String isoCode, String title);

    List<Currency> getAllCurrency();

    List<Currency> getAllByPagination(CurrencyDTO currencyDTO, Pageable pageable, Integer totalElement);

    long getAllCount(CurrencyDTO currencyDTO);

    boolean checkIfTitleAndIsoCodeExist(CurrencyDTO currencyDTO);
}

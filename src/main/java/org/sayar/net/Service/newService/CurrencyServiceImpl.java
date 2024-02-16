package org.sayar.net.Service.newService;


import org.sayar.net.Dao.NewDao.CurrencyDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.DTO.CurrencyDTO;
import org.sayar.net.Model.newModel.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyServiceImpl extends GeneralServiceImpl<Currency> implements CurrencyService {
    @Autowired
    CurrencyDao currencyDao;

    @Override
    public Currency saveCurrency(Currency currency) {
        return currencyDao.saveCurrency(currency);
    }

    @Override
    public Currency updateCurrency(Currency currency) {
        return currencyDao.updateCurrency(currency);
    }

    @Override
    public boolean checkIfCurrencyIsUnique(String isoCode, String title) {
        return currencyDao.checkIfCurrencyIsUnique(isoCode, title);
    }

    @Override
    public List<Currency> getAllCurrency() {
        return currencyDao.getAllCurrency();
    }

    @Override
    public Page<Currency> getAllByPagination(CurrencyDTO currencyDTO, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                currencyDao.getAllByPagination(currencyDTO, pageable, totalElement)
                , pageable
                , currencyDao.getAllCount(currencyDTO)
        );
    }

    @Override
    public boolean checkIfTitleAndIsoCodeExist(CurrencyDTO currencyDTO) {
        return currencyDao.checkIfTitleAndIsoCodeExist(currencyDTO);
    }

}

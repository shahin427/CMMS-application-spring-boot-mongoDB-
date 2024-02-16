package org.sayar.net.Service;

import org.sayar.net.Dao.DocumentDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.newModel.Document.DocumentFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentServiceImpl extends GeneralServiceImpl<DocumentFile> implements DocumentService {

    @Autowired
    private DocumentDao documentDao;

    @Override
    public List<DocumentFile> getAllByExtraId(String extraId) {
        return documentDao.getAllByExtraId(extraId);
    }

    @Override
    public List<DocumentFile> getAllByUserId(String userId) {
        return documentDao.getAllByUserId(userId);
    }

    @Override
    public List<DocumentFile> getAllDocumentsOfWorkOrderByExtraId(List<String> documentsList) {
        return documentDao.getAllDocumentsOfWorkOrderByExtraId(documentsList);
    }

    @Override
    public List<DocumentFile> getDocumentsOfRepository(List<String> documentsList) {
        return documentDao.getDocumentsOfRepository(documentsList);
    }

}

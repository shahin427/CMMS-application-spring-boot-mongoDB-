package org.sayar.net.Model.newModel.file.service;

import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.newModel.file.domain.FileAsByte;
import org.sayar.net.Model.newModel.file.dto.IdSetterDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface FileAsByteService extends GeneralService<FileAsByte> {

    boolean deleteOne(String id);
    FileAsByte upload(MultipartFile file) throws IOException;
    FileAsByte getOne(String id);
    List<FileAsByte> getAllUser(String uploaderId);

    ResponseEntity<?> downloadOne(String id, HttpServletResponse response, HttpServletRequest request) throws IOException;

    boolean categoryIdSetter(IdSetterDTO dto);

}

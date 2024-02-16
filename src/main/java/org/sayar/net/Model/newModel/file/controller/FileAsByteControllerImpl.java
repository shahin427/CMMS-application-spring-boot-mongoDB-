package org.sayar.net.Model.newModel.file.controller;

import org.sayar.net.Model.newModel.Helper.ResponseContent;
import org.sayar.net.Model.newModel.file.dto.IdSetterDTO;
import org.sayar.net.Model.newModel.file.service.FileAsByteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class FileAsByteControllerImpl implements FileAsByteController {
    private final FileAsByteService fileAsByteService;

    @Autowired
    public FileAsByteControllerImpl(FileAsByteService fileAsByteService) {
        this.fileAsByteService = fileAsByteService;
    }

    @Override
    public ResponseEntity<?> save(MultipartFile file) throws IOException {

        return new ResponseContent().sendOkResponseEntity(
                "",
                fileAsByteService.upload(file)
        );
    }

    @Override
    public ResponseEntity<?> downloadOne(String id, HttpServletResponse response, HttpServletRequest request) throws IOException {
         return new ResponseContent().sendOkResponseEntity("", fileAsByteService.downloadOne(id,response,request));
    }

    @Override
    public ResponseEntity<?> categoryIdSetter(IdSetterDTO dto) {

        return new ResponseContent().sendOkResponseEntity(
                "",
                fileAsByteService.categoryIdSetter(dto)
        );
    }
}

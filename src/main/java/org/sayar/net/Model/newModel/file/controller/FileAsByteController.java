package org.sayar.net.Model.newModel.file.controller;

import org.sayar.net.Model.newModel.file.dto.IdSetterDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RequestMapping("fileApi")
public interface FileAsByteController {
    @PostMapping
    ResponseEntity<?> save(@RequestPart("file") MultipartFile file) throws IOException;


    @GetMapping("download-one")
    ResponseEntity<?> downloadOne(@RequestParam("id") String id, HttpServletResponse response, HttpServletRequest request) throws IOException;



    @PostMapping("category-id-setter")
    ResponseEntity<?> categoryIdSetter(@RequestBody IdSetterDTO dto);

}

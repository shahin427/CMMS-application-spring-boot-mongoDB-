package org.sayar.net.Model.newModel.file.service;

import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.newModel.file.dao.FileAsByteDao;
import org.sayar.net.Model.newModel.file.domain.FileAsByte;
import org.sayar.net.Model.newModel.file.dto.IdSetterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Service
public class FileAsByteServiceImpl extends GeneralServiceImpl<FileAsByte> implements FileAsByteService {
    private final FileAsByteDao dao;

    @Autowired
    public FileAsByteServiceImpl(FileAsByteDao dao) {
        this.dao = dao;
    }

    @Override
    public boolean deleteOne(String id) {
        return dao.deleteOne(id);
    }

    @Override
    public FileAsByte upload( MultipartFile file) throws IOException {
        FileAsByte fileAsByte = new FileAsByte();
        System.out.println(1);
        if (file.getContentType().contains("application") && !file.getContentType().equals("application/pdf"))
            throw new RuntimeException();
        System.out.println(2);
        String contentType = file.getContentType();
        int slashIndex = contentType.indexOf('/');
        String extension = contentType.substring(slashIndex + 1, contentType.length());
        fileAsByte.setExtension(extension);
        byte[] originalBytes = file.getBytes();


        System.out.println(3);
        fileAsByte.setOriginalByte(originalBytes);
        if (file.getContentType().contains("image")) {

            InputStream inputStream = null;
            try {
                inputStream = new ByteArrayInputStream(file.getBytes());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            assert inputStream != null;

            InputStream initialStream = resizeImage(inputStream, 50, 50);

            byte[] smallBytes = new byte[initialStream.available()];
            initialStream.read(smallBytes);
            fileAsByte.setSmallByte(smallBytes);
        }

        fileAsByte.setCreationDate(new Date());
        fileAsByte.setOriginalFileName(file.getOriginalFilename());
        dao.save(fileAsByte);
        fileAsByte.setOriginalByte(null);
        return fileAsByte;
    }

    @Override
    public FileAsByte getOne(String id) {
        return dao.getOne(id);
    }

    @Override
    public List<FileAsByte> getAllUser(String uploaderId) {
        return dao.getAllUser(uploaderId);
    }

    @Override
    public ResponseEntity<?> downloadOne(String id, HttpServletResponse response, HttpServletRequest request) throws IOException {
        FileAsByte fileAsByte =dao.getOne(id);
        org.springframework.core.io.Resource resource = null;
        try {

       resource = new ByteArrayResource(fileAsByte.getOriginalByte());

       }
       catch (Exception e){
           e.printStackTrace();
       }
//        try {
//            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
//        } catch (IOException ex) {
//            throw new IOException();
//        }

        // Fallback to the default content type if type could not be determined
        String contentType = "application/octet-stream";

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @Override
    public boolean categoryIdSetter(IdSetterDTO dto) {
        try {
            assert dto!=null;
            assert dto.getCategoryId()!=null;
            assert dto.getImageIdList()!=null;
            assert dto.getImageIdList().size()>0;
            return dao.categoryIdSetter(dto);
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static InputStream resizeImage(InputStream inputStream, int width, int height) throws IOException {
        BufferedImage sourceImage = ImageIO.read(inputStream);
        java.awt.Image thumbnail = sourceImage.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        BufferedImage bufferedThumbnail = new BufferedImage(thumbnail.getWidth(null),
                thumbnail.getHeight(null),
                BufferedImage.TYPE_INT_RGB);
        bufferedThumbnail.getGraphics().drawImage(thumbnail, 0, 0, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedThumbnail, "jpeg", baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }
}

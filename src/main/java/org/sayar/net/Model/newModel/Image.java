package org.sayar.net.Model.newModel;

import lombok.Data;
import org.sayar.net.Enumes.ImageStatus;
import org.sayar.net.Model.Mongo.poll.model.elements.BasicElement;
import org.sayar.net.Model.newModel.Enum.ImageSaveStatus;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;

@Data
public class Image {
    @Id
    private String id;
    public String imageData;
    private byte[] fileByte;
    private double size;
    private long score;
    private long width;
    private long height;
    private ImageStatus imageStatus = ImageStatus.WITHOUT_IMAGE;
    private ImageSaveStatus saveStatus = ImageSaveStatus.SAVED;
    private String extension;
    private ArrayList<BasicElement> subQuestionList = new ArrayList<>();
    private String organId;
    private String path;
    private String smallPath;
    private String alt;
    private String caption;

}

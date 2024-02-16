package org.sayar.net.Model.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class NoticeBoardDTO {
    private String assetName;
    private boolean isPlanned;
    private Date fromStartDate;
    private Date fromEndDate;
}

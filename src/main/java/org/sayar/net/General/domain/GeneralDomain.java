package org.sayar.net.General.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.sayar.net.General.db.util.SecurityUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author yaqub
 */
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneralDomain {

    @Id
    protected String id;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    protected Date systemCreationDate;
    @Indexed
    protected String creatorId;
    @Indexed // logic logicDeleteById
    protected Boolean deleted;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    protected Date deleteDate;
    @Indexed
    protected String deleteBy;

    public GeneralDomain(String id) {
        super();
        this.id = id;

    }

    public GeneralDomain() {
    }

    public void initToSave() {
        this.setCreatorId(SecurityUtils.getLoggedInUserId());
        this.setSystemCreationDate(new Date());
    }

    public void initToSave(String creatorId) {
        this.setCreatorId(creatorId);
        this.setSystemCreationDate(new Date());
    }

    public enum GFN {
        id, systemCreationDate, creatorId, deleted, deleteDate, deleteBy
    }

}

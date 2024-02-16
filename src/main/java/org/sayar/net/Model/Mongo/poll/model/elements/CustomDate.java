package org.sayar.net.Model.Mongo.poll.model.elements;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sayar.net.Model.Mongo.poll.enums.DateType;

import java.io.Serializable;

/**
 * Created by sina on 4/8/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
public class CustomDate implements Serializable {
    private int year;
    private int month;
    private int day;
    private DateType dateType;
    private String integerDate = null;

    public CustomDate(Integer year, Integer month, Integer day) {
        this.year = year;
        this.month = month;
        this.day = day;
        String m=Integer.toString(month);
        String d=Integer.toString(day);
        m=(m.getBytes().length==1) ? m=0+m : m;
        d=(d.getBytes().length==1) ? d=0+d : d;
        this.integerDate = Integer.valueOf(year)+m+d;
    }

    public void setIntegerDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        String m=Integer.toString(month);
        String d=Integer.toString(day);
        m=(m.getBytes().length==1) ? m=0+m : m;
        d=(d.getBytes().length==1) ? d=0+d : d;
        this.integerDate = Integer.valueOf(year)+m+d;
    }

}

package org.sayar.net.Model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//import javax.persistence.Id;

@Document
@Data
@NoArgsConstructor
public class MongoTest {

    @Id
    private String id;
    private String name;
}

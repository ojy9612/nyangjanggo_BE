package com.hanghae99_team3.model.resource.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Document(indexName = "resource")
@NoArgsConstructor
@Mapping(mappingPath = "elastic/resource-mapping.json")
@Setting(settingPath = "elastic/resource-setting.json")
public class ResourceDocument {

    @Id
    private Long id;

    private String resourcename;

    private String amount;

    private String category;

    private Long board_id;

    private LocalDateTime createdat;

    private LocalDateTime modifiedat;

    public ResourceDocument(Resource resource){
        this.id = resource.getId();
        this.resourcename = resource.getResourceName();
        this.amount = resource.getAmount();
        this.category = resource.getCategory();
        this.board_id = resource.getBoard().getId();
        this.createdat = resource.getCreatedAt();
        this.modifiedat = resource.getModifiedAt();
    }
}

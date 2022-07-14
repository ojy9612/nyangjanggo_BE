package com.hanghae99_team3.model.resource.domain;

import com.hanghae99_team3.model.board.Board;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String resourcename;

    private String amount;

    private String category;

    private Board board;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    public ResourceDocument(Resource resource){
        this.id = resource.getId();
        this.resourcename = resource.getResourceName();
        this.amount = resource.getAmount();
        this.category = resource.getCategory();
        this.board = resource.getBoard();
        this.createdAt = resource.getCreatedAt();
        this.modifiedAt = resource.getModifiedAt();
    }
}

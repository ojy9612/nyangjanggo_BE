package com.hanghae99_team3.model.resource.domain;

import com.hanghae99_team3.model.Timestamped;
import com.hanghae99_team3.model.board.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.*;

import javax.persistence.*;
import java.time.LocalDateTime;

import static org.springframework.data.elasticsearch.annotations.DateFormat.date_hour_minute_second_millis;
import static org.springframework.data.elasticsearch.annotations.DateFormat.epoch_millis;

@Getter
@Document(indexName = "resource")
@NoArgsConstructor
//@Mapping(mappingPath = "elastic/resource-mapping.json")
//@Setting(settingPath = "elastic/resource-setting.json")
public class ResourceDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String resourceName;

    private String amount;

    private String category;

    private Board board;

    @Field(type = FieldType.Date, format = {date_hour_minute_second_millis, epoch_millis})
    private LocalDateTime createdAt;

    @Field(type = FieldType.Date, format = {date_hour_minute_second_millis, epoch_millis})
    private LocalDateTime modifiedAt;

    public ResourceDocument(Resource resource){
        this.id = resource.getId();
        this.resourceName = resource.getResourceName();
        this.amount = resource.getAmount();
        this.category = resource.getCategory();
        this.board = resource.getBoard();
        this.createdAt = resource.getCreatedAt();
        this.modifiedAt = resource.getModifiedAt();
    }
}

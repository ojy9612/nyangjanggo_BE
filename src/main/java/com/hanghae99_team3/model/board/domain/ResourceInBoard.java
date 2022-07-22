package com.hanghae99_team3.model.board.domain;

import com.hanghae99_team3.model.resource.domain.Resource;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;
import java.time.LocalDateTime;

import static org.springframework.data.elasticsearch.annotations.DateFormat.date_hour_minute_second_millis;
import static org.springframework.data.elasticsearch.annotations.DateFormat.epoch_millis;

@Getter
@NoArgsConstructor
public class ResourceInBoard {

    private Long id;
    private String resourceName;
    private String amount;
    private String category;
    private Long boardId;
    @Field(type = FieldType.Date, format = {date_hour_minute_second_millis, epoch_millis})
    private LocalDateTime createdAt;
    @Field(type = FieldType.Date, format = {date_hour_minute_second_millis, epoch_millis})
    private LocalDateTime modifiedAt;

    public ResourceInBoard(Resource resource) {
        this.id = resource.getId();
        this.resourceName = resource.getResourceName();
        this.amount = resource.getAmount();
        this.category = resource.getCategory();
        this.boardId = resource.getBoard().getId();
        this.createdAt = resource.getCreatedAt();
        this.modifiedAt = resource.getModifiedAt();
    }

}

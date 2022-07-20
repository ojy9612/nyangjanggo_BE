package com.hanghae99_team3.model.resource.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.*;

import javax.persistence.Id;

@Getter
@Document(indexName = "resource_keyword")
@NoArgsConstructor
@Mapping(mappingPath = "elastic/resource-mapping.json")
@Setting(settingPath = "elastic/nori-setting.json")
public class ResourceKeywordDocument {

    @Id
    private Long id;

    private String resourceName;

    private Integer cnt;

    public void plusCnt(){
        this.cnt += 1;
    }

    public void minusCnt(){
        if (cnt > 0) this.cnt -= 1;
    }

    @Builder
    public ResourceKeywordDocument(Resource resource) {
        this.id = resource.getId();
        this.resourceName = resource.getResourceName();
        this.cnt = 1;
    }
}

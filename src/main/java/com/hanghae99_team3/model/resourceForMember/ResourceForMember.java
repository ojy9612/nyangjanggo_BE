package com.hanghae99_team3.model.resourceForMember;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class ResourceForMember {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String resourceName;

    @Column
    private String category;

    @Column
    private String endtime;

//    private Member member;

}

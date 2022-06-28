package com.hanghae99_team3.model.resourceForBoard;

import com.hanghae99_team3.model.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class ResourceForBoard extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String resourceName;

    @Column
    private String category;

//    private Board board;
}

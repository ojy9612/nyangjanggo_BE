package com.hanghae99_team3.model.fridge;


import com.hanghae99_team3.model.Timestamped;
import com.hanghae99_team3.model.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Entity
@NoArgsConstructor
public class Fridge extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String resourceName;

    @Column
    private String num;

    @Column
    private String category;

    @Column
    private String endTime;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public Fridge(@NotNull String resourceName,
                  @NotNull String num,
                  @NotNull String category,
                  @NotNull String endTime,
                  @NotNull User user) {
        this.resourceName = resourceName;
        this.num = num;
        this.category = category;
        this.endTime = endTime;
        user.addFridge(this);
    }

}

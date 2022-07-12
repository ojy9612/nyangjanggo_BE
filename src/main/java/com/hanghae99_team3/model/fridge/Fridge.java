package com.hanghae99_team3.model.fridge;


import com.hanghae99_team3.model.Timestamped;
import com.hanghae99_team3.model.fridge.dto.FridgeRequestDto;
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
    private String amount;

    @Column
    private String category;

    @Column
    private String endTime;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public Fridge(@NotNull FridgeRequestDto fridgeRequestDto,
                  @NotNull User user) {
        this.resourceName = fridgeRequestDto.getResourceName();
        this.amount = fridgeRequestDto.getAmount();
        this.category = fridgeRequestDto.getCategory();
        this.endTime = fridgeRequestDto.getEndTime();
        user.addFridge(this);
    }

}

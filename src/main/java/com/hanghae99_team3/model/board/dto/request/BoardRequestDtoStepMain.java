package com.hanghae99_team3.model.board.dto.request;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@NoArgsConstructor
public class BoardRequestDtoStepMain {

    private String title;
    private String subTitle;
    private String content;


    /*
    이미지는 없어도 전송할 수 있지만 비어있는 값이 와야함.(List size 는 1)
    그러므로 List<MultipartFile>는 null 이 되면 안됨.
    */
    @Builder
    public BoardRequestDtoStepMain(@NotNull String title,
                                   @NotNull String subTitle,
                                   @NotNull String content) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;

    }

}

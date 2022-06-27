package com.hanghae99_team3.exception;

public class ErrorMessage {

    private ErrorMessage() {
        throw new IllegalArgumentException("ErrorMessageClass 에서 에러가 났습니다. 개발자에게 알려주세요.");
    }

    // 400
    public static final String BOARD_NOT_FOUND = "생성된 게시글이 없습니다.";
    public static final String COMMENT_NOT_FOUND = "생성된 댓글이 없습니다.";

    // 403
    public static final String ID_DUPLICATE = "게시글 작성자가 아닙니다.";

}


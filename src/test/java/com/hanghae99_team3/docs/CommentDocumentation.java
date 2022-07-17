package com.hanghae99_team3.docs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae99_team3.model.comment.dto.CommentRequestDto;
import org.springframework.test.web.servlet.ResultHandler;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;

public class CommentDocumentation {

    static ObjectMapper objectMapper = new ObjectMapper();

    public static ResultHandler createComment(CommentRequestDto commentRequestDto) throws JsonProcessingException {

        return document("post-createComment",
                requestHeaders(
                        headerWithName("Access-Token").description("Jwt Access-Token")
                ),
                requestParts(
                        partWithName("commentRequestDto").description(objectMapper.writeValueAsString(commentRequestDto))
                )
        );
    }

    public static ResultHandler updateComment(CommentRequestDto commentRequestDto) throws JsonProcessingException {

        return document("put-updateComment",
                requestHeaders(
                        headerWithName("Access-Token").description("Jwt Access-Token")
                ),
                requestParts(
                        partWithName("commentRequestDto").description(objectMapper.writeValueAsString(commentRequestDto))
                )
        );
    }

    public static ResultHandler removeComment() {

        return document("delete-removeComment",
                requestHeaders(
                        headerWithName("Access-Token").description("Jwt Access-Token")
                )
        );
    }
}

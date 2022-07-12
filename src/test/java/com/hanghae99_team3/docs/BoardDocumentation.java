package com.hanghae99_team3.docs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae99_team3.model.board.dto.BoardRequestDtoStepMain;
import com.hanghae99_team3.model.board.dto.BoardRequestDtoStepRecipe;
import com.hanghae99_team3.model.resource.dto.ResourceRequestDto;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultHandler;

import java.util.List;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;

public class BoardDocumentation {

    static ObjectMapper objectMapper = new ObjectMapper();

    static FieldDescriptor[] boardResponseDto = new FieldDescriptor[] {
            fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시글 ID").optional(),
            fieldWithPath("status").type(JsonFieldType.STRING).description("게시글 상태(step 1, step 2, step 3,complete)"),
            fieldWithPath("nickname").type(JsonFieldType.STRING).description("유저 닉네임"),
            fieldWithPath("userImg").type(JsonFieldType.STRING).description("유저 이미지"),
            fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
            fieldWithPath("subTitle").type(JsonFieldType.STRING).description("게시글 간단한 설명"),
            fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용"),
            fieldWithPath("mainImg").type(JsonFieldType.STRING).description("게시글 대표 이미지").optional(),
            fieldWithPath("commentCount").type(JsonFieldType.NUMBER).description("댓글 수"),
            fieldWithPath("goodCount").type(JsonFieldType.NUMBER).description("좋아요 수"),
            fieldWithPath("resourceResponseDtoList").type(JsonFieldType.ARRAY).description("재료 리스트").optional(),
            fieldWithPath("resourceResponseDtoList[].resourceName").type(JsonFieldType.STRING).description("재료 이름").optional(),
            fieldWithPath("resourceResponseDtoList[].amount").type(JsonFieldType.STRING).description("재료 수량").optional(),
            fieldWithPath("resourceResponseDtoList[].category").type(JsonFieldType.STRING).description("재료 카테고리").optional(),
            fieldWithPath("createdAt").type(JsonFieldType.STRING).description("게시글 생성 날짜").optional(),
            fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("게시글 수정 날짜").optional()
    };

    static FieldDescriptor[] boardDetailResponseDto = new FieldDescriptor[] {
            fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시글 ID").optional(),
            fieldWithPath("status").type(JsonFieldType.STRING).description("게시글 상태(step 1, step 2, step 3,complete)"),
            fieldWithPath("nickname").type(JsonFieldType.STRING).description("유저 닉네임"),
            fieldWithPath("userImg").type(JsonFieldType.STRING).description("유저 이미지"),
            fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
            fieldWithPath("subTitle").type(JsonFieldType.STRING).description("게시글 간단한 설명"),
            fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용"),
            fieldWithPath("mainImg").type(JsonFieldType.STRING).description("게시글 대표 이미지").optional(),
            fieldWithPath("resourceResponseDtoList").type(JsonFieldType.ARRAY).description("재료 리스트").optional(),
            fieldWithPath("resourceResponseDtoList[].resourceName").type(JsonFieldType.STRING).description("재료 이름").optional(),
            fieldWithPath("resourceResponseDtoList[].amount").type(JsonFieldType.STRING).description("재료 수량").optional(),
            fieldWithPath("resourceResponseDtoList[].category").type(JsonFieldType.STRING).description("재료 카테고리").optional(),
            fieldWithPath("recipeStepResponseDtoList").type(JsonFieldType.ARRAY).description("재료 리스트").optional(),
            fieldWithPath("recipeStepResponseDtoList[].stepNum").type(JsonFieldType.NUMBER).description("레시피 Step").optional(),
            fieldWithPath("recipeStepResponseDtoList[].content").type(JsonFieldType.STRING).description("레시피 내용").optional(),
            fieldWithPath("recipeStepResponseDtoList[].imageLink").type(JsonFieldType.STRING).description("레시피 사진").optional(),
            fieldWithPath("commentList").type(JsonFieldType.ARRAY).description("댓글 리스트").optional(),
            fieldWithPath("commentList[].nickname").type(JsonFieldType.NUMBER).description("댓글 유저 닉네임").optional(),
            fieldWithPath("commentList[].userImg").type(JsonFieldType.STRING).description("댓글 유저 이미지").optional(),
            fieldWithPath("commentList[].comment").type(JsonFieldType.STRING).description("댓글 내용").optional(),
            fieldWithPath("commentList[].createdAt").type(JsonFieldType.STRING).description("댓글 생성일자").optional(),
            fieldWithPath("commentList[].modifiedAt").type(JsonFieldType.STRING).description("댓글 수정일자").optional(),
            fieldWithPath("goodList").type(JsonFieldType.ARRAY).description("좋아요 리스트").optional(),
            fieldWithPath("goodList[].nickname").type(JsonFieldType.NUMBER).description("좋아요 유저 닉네임").optional(),
            fieldWithPath("goodList[].userImg").type(JsonFieldType.STRING).description("좋아요 유저 이미지").optional(),
            fieldWithPath("createdAt").type(JsonFieldType.STRING).description("게시글 생성 날짜").optional(),
            fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("게시글 수정 날짜").optional()
    };



    public static ResultHandler getOneBoard() {

        return document("get-getOneBoard",
                responseFields(
                        boardDetailResponseDto
                )
        );
    }

    public static ResultHandler getAllBoards() {

        return document("get-getAllBoards",
                responseFields(

                )
        );
    }

    public static ResultHandler createBoardStepStart() {

        return document("get-createBoardStepStart",
                requestHeaders(
                        headerWithName("Access-Token").description("Jwt Access-Token")
                ),
                responseFields(
                        boardDetailResponseDto
                )
        );
    }

    public static ResultHandler createBoardStepMain(BoardRequestDtoStepMain boardRequestDtoStepMain) throws JsonProcessingException {

        return document("post-createBoardStepMain",
                requestHeaders(
                        headerWithName("Access-Token").description("Jwt Access-Token")
                ),
                requestParts(
                        partWithName("multipartFile").description("게시글 대표 이미지"),
                        partWithName("boardRequestDtoStepMain").description(objectMapper.writeValueAsString(boardRequestDtoStepMain))
                ),
                responseFields(
                        fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시글 ID")
                )
        );
    }

    public static ResultHandler createBoardStepResource(List<ResourceRequestDto> resourceRequestDtoList) throws JsonProcessingException {

        return document("post-createBoardStepResource",
                requestHeaders(
                        headerWithName("Access-Token").description("Jwt Access-Token")
                ),
                requestParts(
                        partWithName("boardId").description("게시글 아이디"),
                        partWithName("resourceRequestDtoList").description(objectMapper.writeValueAsString(resourceRequestDtoList))
                ),
                responseFields(
                        fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시글 ID")
                )
        );
    }

    public static ResultHandler createBoardStepRecipe(BoardRequestDtoStepRecipe boardRequestDtoStepRecipe) throws JsonProcessingException {

        return document("post-createBoardStepRecipe",
                requestHeaders(
                        headerWithName("Access-Token").description("Jwt Access-Token")
                ),
                requestParts(
                        partWithName("multipartFile").description("RecipeStep 이미지"),
                        partWithName("boardRequestDtoStepRecipe").description(objectMapper.writeValueAsString(boardRequestDtoStepRecipe))
                ),
                responseFields(
                        fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시글 ID")
                )
        );
    }

    public static ResultHandler createBoardStepEnd() {

        return document("post-createBoardStepEnd",
                requestHeaders(
                        headerWithName("Access-Token").description("Jwt Access-Token")
                ),
                requestParts(
                        partWithName("boardId").description("게시글 ID")
                )
        );
    }

    public static ResultHandler updateBoardStepMain(BoardRequestDtoStepMain boardRequestDtoStepMain) throws JsonProcessingException {

        return document("put-updateBoardStepMain",
                requestHeaders(
                        headerWithName("Access-Token").description("Jwt Access-Token")
                ),
                requestParts(
                        partWithName("boardId").description("게시글 ID"),
                        partWithName("multipartFile").description("게시글 대표 이미지"),
                        partWithName("boardRequestDtoStepMain").description(objectMapper.writeValueAsString(boardRequestDtoStepMain))
                ),
                responseFields(
                        fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시글 ID")
                )
        );
    }


    public static ResultHandler updateBoardStepResource(List<ResourceRequestDto> resourceRequestDtoList) throws JsonProcessingException {

        return document("put-updateBoardStepResource",
                requestHeaders(
                        headerWithName("Access-Token").description("Jwt Access-Token")
                ),
                requestParts(
                        partWithName("boardId").description("게시글 아이디"),
                        partWithName("resourceRequestDtoList").description(objectMapper.writeValueAsString(resourceRequestDtoList))
                ),
                responseFields(
                        fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시글 ID")
                )
        );
    }

    public static ResultHandler updateBoardStepRecipe(BoardRequestDtoStepRecipe boardRequestDtoStepRecipe) throws JsonProcessingException {

        return document("put-updateBoardStepRecipe",
                requestHeaders(
                        headerWithName("Access-Token").description("Jwt Access-Token")
                ),
                requestParts(
                        partWithName("multipartFile").description("RecipeStep 이미지"),
                        partWithName("boardRequestDtoStepRecipe").description(objectMapper.writeValueAsString(boardRequestDtoStepRecipe))
                ),
                responseFields(
                        fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시글 ID")
                )
        );
    }

    public static ResultHandler deleteRecipeStep() {

        return document("delete-deleteRecipeStep",
                requestHeaders(
                        headerWithName("Access-Token").description("Jwt Access-Token")
                ),
                requestParts(
                        partWithName("boardId").description("게시글 ID"),
                        partWithName("stepNum").description("삭제할 Recipe Step")
                )
        );
    }

    public static ResultHandler deleteBoard() {

        return document("delete-deleteBoard",
                requestHeaders(
                        headerWithName("Access-Token").description("Jwt Access-Token")
                )
        );
    }

}

package com.hanghae99_team3.model.board.config;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
public class SaveGoodCount {
    private Set<Long> boardIdSet = new HashSet<>();

    public void appendBoardId(Long boardId){
        boardIdSet.add(boardId);
    }

    public Set<Long> popAllBoardId(){
        Set<Long> result = boardIdSet;
        boardIdSet = new HashSet<>();
        return result;
    }
}

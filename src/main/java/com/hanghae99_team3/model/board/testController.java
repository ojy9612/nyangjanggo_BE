package com.hanghae99_team3.model.board;

import com.hanghae99_team3.model.board.service.BoardDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class testController {

    private final BoardDocumentService boardDocumentService;

    @PostMapping("/test/resource/data")
    public void createResourceData(){

    }

}

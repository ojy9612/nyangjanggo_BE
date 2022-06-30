package com.hanghae99_team3.model.images;

import com.hanghae99_team3.model.board.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImagesService {

    private final ImagesRepository imagesRepository;

    public void createImages(List<String> s3Lists, Board board) {


        s3Lists.forEach(imageLink -> {
            if (!imageLink.equals("")) {
                Images images = Images.builder()
                        .imageLink(imageLink)
                        .board(board)
                        .build();

                imagesRepository.save(images);
            }
        });

    }

}

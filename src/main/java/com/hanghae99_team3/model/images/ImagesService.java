package com.hanghae99_team3.model.images;

import com.hanghae99_team3.model.board.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImagesService {

    private final ImagesRepository imagesRepository;

    public void createImages(List<List<String>> s3Lists, Board board){

        List<String> imageKey = s3Lists.get(0);
        List<String> imageLink = s3Lists.get(1);

        for (int i = 0 ; i < imageKey.size(); i++){
            if (! imageKey.get(i).equals("")){
                Images images = Images.builder()
                        .imageKey(imageKey.get(i))
                        .imageLink(imageLink.get(i))
                        .board(board)
                        .build();

                imagesRepository.save(images);
            }
        }

    }

}

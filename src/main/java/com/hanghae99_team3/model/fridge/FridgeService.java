package com.hanghae99_team3.model.fridge;

import com.hanghae99_team3.model.fridge.dto.FridgeRequestDto;
import com.hanghae99_team3.model.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FridgeService {

    private final FridgeRepository fridgeRepository;

    public void createFridge(List<FridgeRequestDto> fridgeRequestDtoList, User user){

        fridgeRequestDtoList.forEach(fridgeRequestDto -> {
            Fridge fridge = Fridge.builder()
                    .fridgeRequestDto(fridgeRequestDto)
                    .user(user)
                    .build();

            fridgeRepository.save(fridge);
        });
    }

    public void updateFridge(List<FridgeRequestDto> fridgeRequestDtoList, User user) {
        this.deleteAllFridge(user);
        this.createFridge(fridgeRequestDtoList,user);
    }

    public void deleteAllFridge(User user){
        fridgeRepository.deleteAll(fridgeRepository.findAllByUser(user));
    }
}

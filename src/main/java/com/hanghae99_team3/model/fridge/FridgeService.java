package com.hanghae99_team3.model.fridge;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FridgeService {

    private final FridgeRepository fridgeRepository;

    public void createFridge(List<String> resourceInfos){

    }

}

package com.hanghae99_team3.model.fridge;

import com.hanghae99_team3.model.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FridgeRepository extends JpaRepository<Fridge, Long> {

    List<Fridge> findAllByUser(User user);
}

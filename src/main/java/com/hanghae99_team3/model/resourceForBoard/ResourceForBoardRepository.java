package com.hanghae99_team3.model.resourceForBoard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceForBoardRepository extends JpaRepository<ResourceForBoard, Long> {

}

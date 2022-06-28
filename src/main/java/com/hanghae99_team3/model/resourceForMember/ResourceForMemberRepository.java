package com.hanghae99_team3.model.resourceForMember;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceForMemberRepository extends JpaRepository<ResourceForMember, Long> {

}

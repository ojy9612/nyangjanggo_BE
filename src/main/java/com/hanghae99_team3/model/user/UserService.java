package com.hanghae99_team3.model.user;


import com.hanghae99_team3.config.redis.CacheKey;
import com.hanghae99_team3.model.fridge.Fridge;
import com.hanghae99_team3.model.fridge.FridgeService;
import com.hanghae99_team3.model.fridge.dto.FridgeRequestDto;
import com.hanghae99_team3.model.s3.AwsS3Service;
import com.hanghae99_team3.model.user.domain.User;
import com.hanghae99_team3.model.user.domain.dto.UserReqDto;
import com.hanghae99_team3.login.jwt.PrincipalDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final AwsS3Service awsS3Service;
    private final FridgeService fridgeService;


    @Autowired
    public UserService(UserRepository userRepository, AwsS3Service awsS3Service, FridgeService fridgeService) {
        this.userRepository = userRepository;
        this.awsS3Service = awsS3Service;
        this.fridgeService = fridgeService;
    }


    /**
     * PrincipalDetails 에서 User 객체가 필요할 때 사용
     * @param principalDetails : 인증된 user 정보
     * @return User 객체
     */
    public User findUserByAuthEmail(PrincipalDetails principalDetails) {
        return userRepository.findByEmail(principalDetails.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("유저 정보가 없습니다."));
    }


    /**
     * User update method
     * @param email : 캐싱된 데이터의 Key 구분자
     * @param userReqDto : nickname, userDescription
     * @param multipartFile : 변경할 유저 사진
     * @param principalDetails : 인증된 user 정보
     */
    @Transactional
    @CacheEvict(value = CacheKey.USER, key = "#email", cacheManager = "cacheManager")
    public void updateUser(String email, UserReqDto userReqDto, MultipartFile multipartFile, PrincipalDetails principalDetails) {
        User user = this.findUserByAuthEmail(principalDetails);

        String newImg = awsS3Service.uploadFile(multipartFile);
        if (newImg.equals("")) {
            user.update(userReqDto, user.getUserImg());
        }
        else {
            //기존 이미지 삭제
            awsS3Service.deleteFile(user.getUserImg());
            user.update(userReqDto, newImg);
        }
    }


    /**
     * User delete method
     * @param email : 캐싱된 데이터의 Key 구분자
     * @param principalDetails : 인증된 user 정보
     */
    @Transactional
    @CacheEvict(value = CacheKey.USER, key = "#email", cacheManager = "cacheManager")
    public void deleteUser(String email, PrincipalDetails principalDetails) {
        // 기존 이미지 삭제
        awsS3Service.deleteFile(principalDetails.getUserImg());
        // User 삭제
        userRepository.deleteById(principalDetails.getUserId());
    }


    /**
     * Duplicated nickname check method
     * @param nickname : 중복 확인할 nickname
     * @return : 사용할 수 있다면 true, 중복된 닉네임이면 false
     */
    public boolean checkNicknameDup(String nickname) {
        Optional<User> user = userRepository.findByNickname(nickname);
        return user.isEmpty();
    }

    @Transactional(readOnly = true)
    public List<Fridge> getFridge(PrincipalDetails principalDetails) {
        User user = this.findUserByAuthEmail(principalDetails);

        return fridgeService.getFridge(user);
    }

    @Transactional
    public void createFridge(PrincipalDetails principalDetails, List<FridgeRequestDto> fridgeRequestDtoList) {
        User user = this.findUserByAuthEmail(principalDetails);

        fridgeService.createFridge(fridgeRequestDtoList,user);
    }

    @Transactional
    public void updateFridge(PrincipalDetails principalDetails, List<FridgeRequestDto> fridgeRequestDtoList) {
        User user = this.findUserByAuthEmail(principalDetails);

        fridgeService.updateFridge(fridgeRequestDtoList,user);
    }

}

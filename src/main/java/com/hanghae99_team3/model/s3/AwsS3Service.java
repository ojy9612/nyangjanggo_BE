package com.hanghae99_team3.model.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.hanghae99_team3.exception.newException.S3UploadFailedException;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AwsS3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    private final AmazonS3 amazonS3;

    private static final String DIR = "team3";
    // https://hanhae99homework2.s3.ap-northeast-2.amazonaws.com/team3/c4a3f43f-3276-4e70-81ae-0719749e6bb0_.jpg

    private static final String OBJECTLINK = "https://hanhae99homework2.s3.ap-northeast-2.amazonaws.com/";

    public List<String> uploadFile(List<MultipartFile> multipartFiles) {
        List<String> fileLinkList = new ArrayList<>();

        // forEach 구문을 통해 multipartFile로 넘어온 파일들 하나씩 fileNameList에 추가
        multipartFiles.forEach(file -> {
            if(file.getContentType() != null){
                String fileName = createFileName(file.getOriginalFilename(),DIR);
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentLength(file.getSize());
                objectMetadata.setContentType(file.getContentType());

                try(InputStream inputStream = file.getInputStream()) {
                    // 파일 유효성 검사
                    Tika tika = new Tika();
                    String detectedFile = tika.detect(file.getBytes());  // getBytes() 사용시 느려질 수 있음
                    if(!(detectedFile.startsWith("image"))){
                        throw new IllegalArgumentException("AwsS3 : 올바른 이미지 파일을 올려주세요.");
                    }

                    // 업로드
                    amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));


                } catch(IOException e) {
                    throw new S3UploadFailedException("S3파일 업로드 실패, bucket 에 남겨진 이미지를 확인하세요.");
                }

                fileLinkList.add(OBJECTLINK + amazonS3.getObject(bucket,fileName).getKey());
            } else {
                fileLinkList.add("");
            }
        });

        return fileLinkList;
    }

    public void deleteFile(String imgUrl) {
        if (!imgUrl.equals("")) {
            String fileName = imgUrl.split("/")[3];
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
        }
    }

    private String createFileName(String fileName,String dirName) { // 먼저 파일 업로드 시, 파일명을 난수화하기 위해 random으로 돌립니다.
        return dirName + "/" + UUID.randomUUID().toString().concat("_" + getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) { // file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며, 파일 타입과 상관없이 업로드할 수 있게 하기 위해 .의 존재 유무만 판단하였습니다.
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }

}
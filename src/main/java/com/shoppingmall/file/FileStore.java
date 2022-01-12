package com.shoppingmall.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FileStore {

    private final AmazonS3Client amazonS3Client;
    //s3 버킷
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    //로컬 저장소
    @Value("${file.upload.dir}")
    private String fileDir;

    //새로운 이미지 업로드
    public UploadFile newUpload(MultipartFile multipartFile) throws IOException {
        //원본 파일명
        String originalFilename = multipartFile.getOriginalFilename();
        //DB에 저장할 파일명
        String storeFilename = createStoreFilename(originalFilename);
        //파일을 지정된 위치에 저장
        File uploadFile = new File(getFullPath(storeFilename));
        multipartFile.transferTo(uploadFile);
        //s3로 업로드
        String uploadImageUrl = putS3(uploadFile, "static/" + storeFilename);
        //로컬에 있는 파일 삭제
        if(uploadFile.delete()) log.info("Local file delete success");

        return new UploadFile(uploadImageUrl, storeFilename);
    }

    //원래 이미지 변경
    public UploadFile replaceUpload(MultipartFile multipartFile, String oldFilename) throws IOException {
        //파일을 지정된 위치에 저장
        File uploadFile = new File(getFullPath(oldFilename));
        multipartFile.transferTo(uploadFile);
        //s3로 업로드
        String uploadImageUrl = putS3(uploadFile, "static/" + oldFilename);
        //로컬에 있는 파일 삭제
        if(uploadFile.delete()) log.info("Local file delete success");

        return new UploadFile(uploadImageUrl, oldFilename);
    }

    // S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    //S3에 있는 이미지 삭제
    public void deleteS3(String fileName){
        boolean isExistObject = amazonS3Client.doesObjectExist(bucket, fileName);
        if(isExistObject){
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
        }
    }

    public String getFullPath(String filename){
        return fileDir + filename;
    }

    private String createStoreFilename(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}

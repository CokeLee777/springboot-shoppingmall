package com.shoppingmall.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UploadFile {

    private String uploadFilename;  //회원이 업로드한 파일명
    private String storeFilename;   //DB에 저장할 파일명
}

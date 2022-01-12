package com.shoppingmall.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UploadFile {

    private String storeFileUrl;    //DB에 저장된 Url
    private String storeFilename;   //DB에 저장할 파일명
}

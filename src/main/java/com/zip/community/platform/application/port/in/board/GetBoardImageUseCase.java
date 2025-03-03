package com.zip.community.platform.application.port.in.board;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface GetBoardImageUseCase {

    // 게시글 조회하면서, 사진 가져오기
    ResponseEntity<Resource> getFile(String fileName);

}

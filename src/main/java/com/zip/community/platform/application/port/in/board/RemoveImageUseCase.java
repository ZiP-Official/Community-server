package com.zip.community.platform.application.port.in.board;

import java.util.List;

public interface RemoveImageUseCase {

    // 게시글 수정하면서 사진 삭제
    void delelteFiles(List<String> fileNames);

}

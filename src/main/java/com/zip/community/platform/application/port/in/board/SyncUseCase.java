package com.zip.community.platform.application.port.in.board;

public interface SyncUseCase {

    /*
        데이터 동기화 위한 유즈케이스 입니다.
        레디스의 데이터를 DB에 이동하여 저장함으로써
        레디스의 사라짐에도 대응할 수 있습니다.
     */

    /// 데이터 동기화 업데이트
    void syncData(Long boardId);

}

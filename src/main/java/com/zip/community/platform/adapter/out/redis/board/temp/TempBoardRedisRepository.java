package com.zip.community.platform.adapter.out.redis.board.temp;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TempBoardRedisRepository extends CrudRepository<TempBoardRedisHash, Long> {

    /*
        레디스 해쉬에 관련된 내용을 정의한다.
     */

    // 임시저장글 목록 가져오기
    List<TempBoardRedisHash> findByMemberId(Long memberId);


}

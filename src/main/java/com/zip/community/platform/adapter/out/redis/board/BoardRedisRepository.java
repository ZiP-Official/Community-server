package com.zip.community.platform.adapter.out.redis.board;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BoardRedisRepository extends CrudRepository<BoardRedisHash, Long> {

    /*
        레디스 해쉬에 관련된 내용을 정의한다.
     */

    // 임시저장글 목록 가져오기
    List<BoardRedisHash> findByMemberId(Long memberId);


}

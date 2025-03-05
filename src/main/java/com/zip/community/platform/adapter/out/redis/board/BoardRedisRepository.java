package com.zip.community.platform.adapter.out.redis.board;

import org.springframework.data.repository.CrudRepository;

public interface BoardRedisRepository extends CrudRepository<BoardRedisHash, Long> {

    /*
        레디스 해쉬에 관련된 내용을 정의한다.
     */

}

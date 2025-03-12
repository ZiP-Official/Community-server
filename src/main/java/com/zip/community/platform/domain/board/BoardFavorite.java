package com.zip.community.platform.domain.board;


import com.zip.community.platform.domain.BaseDomain;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class BoardFavorite extends BaseDomain {

    /*
       인기글 목록을 저장하는 도메인 영역이다.
     */

    private Long id;

    private Long boardId;

    // 생성자
    public static BoardFavorite of(Long boardId) {
        return BoardFavorite.builder()
                .boardId(boardId)
                .build();

    }
}

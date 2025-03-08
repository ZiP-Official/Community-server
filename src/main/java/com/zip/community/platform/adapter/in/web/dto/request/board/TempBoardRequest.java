package com.zip.community.platform.adapter.in.web.dto.request.board;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TempBoardRequest {

    /*
        해당 DTO는 게시글 작성 DTO와 변수 내용이 같아야,
        프런트측에서 처리할 때
        버튼 하나로 임시저장 혹은 저장을 할 수 있게 할 수 있다.
     */

    // 카테고리
    private Long categoryId;

    // 게시물
    private Long memberId;
    private String title;
    private String content;

    //
    private String thumbnailUrl;

}

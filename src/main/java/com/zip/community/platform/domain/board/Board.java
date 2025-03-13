package com.zip.community.platform.domain.board;


import com.zip.community.platform.adapter.in.web.dto.request.board.BoardUpdateRequest;
import com.zip.community.platform.domain.BaseDomain;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Board extends BaseDomain {

    private Long id;
    private Long memberId;
    private Long categoryId;

    private BoardSnippet snippet;
    private BoardStatistics statistics;

    private boolean anonymous;
    private boolean deleted;

    // 생성자
    public static Board of(Long memberId, Long categoryId, BoardSnippet snippet, BoardStatistics statistics, boolean anonymous) {
        return Board.builder()
                .memberId(memberId)
                .snippet(snippet)
                .statistics(statistics)
                .categoryId(categoryId)
                .anonymous(anonymous)
                .build();

    }

    /// 수정 로직
    public void update(BoardUpdateRequest request) {
        // 제목이 null이 아니면 제목 업데이트
        if (request.getTitle() != null) {
            this.snippet.changeTitle(request.getTitle());
        }

        // 내용이 null이 아니면 내용 업데이트
        if (request.getContent() != null) {
            this.snippet.changeContent(request.getContent());
        }

        // 카테고리 ID가 null이 아니면 카테고리 ID 업데이트
        if (request.getCategoryId() != null) {
            this.categoryId = request.getCategoryId();
        }
    }
}

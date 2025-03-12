package com.zip.community.platform.adapter.out.jpa.board;


import com.zip.community.platform.adapter.out.jpa.BaseEntity;
import com.zip.community.platform.domain.board.Board;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private Long memberId;

    private Long categoryId;

    @Embedded
    private BoardSnippetJpaEntity boardSnippet;

    @Embedded
    private BoardStatisticsJpaEntity boardStatistics;

    // From
    public static BoardJpaEntity from(Board board) {
        return BoardJpaEntity.builder()
                .memberId(board.getMemberId())
                .boardSnippet(BoardSnippetJpaEntity.from(board.getSnippet()))
                .boardStatistics(BoardStatisticsJpaEntity.from(board.getStatistics()))
                .categoryId(board.getCategoryId())
                .build();
    }

    // toDomain
    public Board toDomain() {
        return Board.builder()
                .id(this.id)
                .categoryId(this.categoryId)
                .memberId(this.memberId)
                .snippet(this.boardSnippet.toDomain())
                .statistics(this.boardStatistics.toDomain())
                .createdAt(this.getCreated())
                .updatedAt(this.getUpdated())
                .build();
    }

    /// 비즈니스 로직
    // 실제 DB 내에서 조회수를 매핑할때 사용하는 로직
    public void updateBoardStatics(long viewCount, long likeCount, long dislikeCount, long commentCount) {
        this.boardStatistics.changeStatistics(viewCount, likeCount, dislikeCount, commentCount);
    }

    

}

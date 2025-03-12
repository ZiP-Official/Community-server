package com.zip.community.platform.adapter.out.jpa.board;

import com.zip.community.platform.adapter.out.jpa.BaseEntity;
import com.zip.community.platform.domain.board.BoardFavorite;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardFavoriteJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_favorite_id")
    private Long id;

    private Long boardId;

    // from
    public static BoardFavoriteJpaEntity from(BoardFavorite boardFavorite) {
        return BoardFavoriteJpaEntity.builder()
                .boardId(boardFavorite.getBoardId())
                .build();
    }

    // toDomain
    public BoardFavorite toDomain() {
        return BoardFavorite.builder()
                .id(id)
                .boardId(boardId)
                .createdAt(this.getCreated())
                .build();
    }

}

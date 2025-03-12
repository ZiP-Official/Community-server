package com.zip.community.platform.application.service.board;

import com.zip.community.common.response.CustomException;
import com.zip.community.common.response.errorcode.BoardErrorCode;
import com.zip.community.platform.application.port.in.board.SyncUseCase;
import com.zip.community.platform.application.port.out.board.*;
import com.zip.community.platform.application.port.out.comment.*;
import com.zip.community.platform.domain.board.Board;
import com.zip.community.platform.domain.comment.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SyncService implements SyncUseCase {

    private final SaveBoardPort savePort;
    private final SaveBoardReactionPort saveReactionPort;
    private final SaveCommentReactionPort saveCommentReactionPort;

    private final LoadBoardPort loadPort;
    private final LoadBoardReactionPort loadReactionPort;
    private final LoadCommentPort loadCommentPort;
    private final LoadCommentReactionPort loadCommentReactionPort;

    private final RemoveBoardPort removePort;
    private final RemoveBoardReactionPort removeReactionPort;
    private final RemoveCommentPort removeCommentPort;
    private final RemoveCommentReactionPort removeCommentReactionPort;

    @Override
    public void syncData(Long boardId) {

        // 게시글 가져오기
        Board board = loadPort.loadBoardById(boardId)
                .orElseThrow(() -> new CustomException(BoardErrorCode.NOT_FOUND_BOARD));

        long viewCount = loadPort.loadViewCount(board.getId());
        long likeCount = loadReactionPort.loadBoardLikeCount(board.getId()) != null ? loadReactionPort.loadBoardLikeCount(board.getId()) : 0L;
        long disLikeCount = loadReactionPort.loadBoardDisLikeCount(board.getId()) != null ? loadReactionPort.loadBoardDisLikeCount(board.getId()) : 0L;
        long commentCount = loadCommentPort.loadCommentCount(board.getId()) != null ? loadCommentPort.loadCommentCount(board.getId()) : 0L;

        // 해당 글에 대한 댓글 목록 조화
        List<Comment> comments = loadCommentPort.loadCommentsByBoardId(boardId);

        // JPA 에 연동하기
        savePort.syncData(boardId, viewCount, likeCount, disLikeCount, commentCount);
        saveReactionPort.syncBoardReaction(boardId);
        comments.forEach(comment -> {saveCommentReactionPort.syncBoardReaction(comment.getId());});

        /// 해당 레디스들은 전부 삭제하기

        // 리액션 관련 내용 삭제하기
        removePort.removeCache(boardId);
        removeReactionPort.removeCache(board.getId());

        /// 댓글 관련 내용 삭제하기
        removeCommentPort.removeCache(board.getId());

        comments.forEach(comment -> {
            removeCommentReactionPort.removeCache(comment.getId());
        });



    }
}

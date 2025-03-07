package com.zip.community.platform.application.service.comment;

import com.zip.community.platform.application.port.in.comment.CreateCommentUseCase;
import com.zip.community.platform.application.port.in.comment.GetCommentUseCase;
import com.zip.community.platform.application.port.in.comment.RemoveCommentUseCase;
import com.zip.community.platform.adapter.in.web.dto.request.board.CommentRequest;
import com.zip.community.platform.application.port.out.user.LoadUserPort;
import com.zip.community.platform.application.port.out.board.LoadBoardPort;
import com.zip.community.platform.application.port.out.comment.LoadCommentPort;
import com.zip.community.platform.application.port.out.comment.RemoveCommentPort;
import com.zip.community.platform.application.port.out.comment.SaveCommentPort;
import com.zip.community.platform.domain.comment.CommentStatistics;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.zip.community.platform.domain.board.Board;
import com.zip.community.platform.domain.comment.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService implements CreateCommentUseCase, GetCommentUseCase, RemoveCommentUseCase {

    private final LoadBoardPort loadBoardPort;
    private final LoadUserPort loadUserPort;
    private final SaveCommentPort saveCommentPort;
    private final LoadCommentPort loadCommentPort;
    private final RemoveCommentPort removeCommentPort;

    @Override
    public Comment createComment(CommentRequest commentRequest) {

        Board board = loadBoardPort.loadBoardById(commentRequest.getBoardId())
                .orElseThrow(() -> new EntityNotFoundException("해당 게시판이 존재하지 않습니다."));

        // 부모가 없는 경우 null 처리
        Comment parent = null;
        if (commentRequest.getParentId() != null) {
            parent = loadCommentPort.loadCommentById(commentRequest.getParentId()).orElseThrow();
        }

        // 통계
        CommentStatistics statistics = CommentStatistics.of();

        return saveCommentPort.saveComment(Comment.of(board.getId(), commentRequest.getMemberId(), parent.getId(), commentRequest.getContent(), statistics));
    }

    @Override
    public Page<Comment> getByBoardId(Long boardId, Pageable pageable) {
       return loadCommentPort.loadCommentsByBoardId(boardId, pageable);
    }

    @Override
    public void removeComment(String id) {
        removeCommentPort.removeComment(id);
    }
}

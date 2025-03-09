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
import com.zip.community.platform.domain.comment.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService implements CreateCommentUseCase, GetCommentUseCase, RemoveCommentUseCase {

    private final SaveCommentPort savePort;
    private final LoadCommentPort loadPort;
    private final RemoveCommentPort removePort;

    private final LoadBoardPort loadBoardPort;
    private final LoadUserPort loadUserPort;


    @Override
    public Comment createComment(CommentRequest request) {

        // 게시글 존재 여부 파악
        if (!loadBoardPort.existBoard(request.getBoardId())) {
            throw new EntityNotFoundException("해당 게시글이 존재하지 않습니다.");
        }

        // 대댓글이 없는 경우 예외처리
        if (request.getParentId() != null) {
            if (!loadPort.getCheckedExistComment(request.getParentId())) {
                throw new EntityNotFoundException("대댓글을 작성할 댓글이 존재하지 않습니다.");
            }
        }

        // Comment 객체 생성
        CommentStatistics statistics = CommentStatistics.of();
        Comment comment = Comment.of(
                UUID.randomUUID().toString(), request.getBoardId(), request.getMemberId(), request.getParentId(), request.getContent(), statistics
        );

        Comment saveComment = savePort.saveComment(comment);

        /// 게시글 작성자 여부 파악
        Long writerId = loadBoardPort.loadWriterIdByBoardId(request.getBoardId())
                .orElseThrow(() -> new EntityNotFoundException("해당하는 게시글이 존재하지 않습니다."));

        /// 작성자 부분은 DB 저장없이, 처리한다.
        if (saveComment.getMemberId().equals(writerId)) {
            saveComment.changeWriter();
        }

        return saveComment;
    }

    /// 게시글 아이디로 댓글 가져오기
    @Override
    public Page<Comment> getByBoardId(Long boardId, Pageable pageable) {

        Long writerId = loadBoardPort.loadWriterIdByBoardId(boardId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 게시글이 존재하지 않습니다."));

        // 게시글에 해당하는 댓글을 페이지 단위로 조회
        Page<Comment> result = loadPort.loadCommentsByBoardId(boardId, pageable);
        List<Comment> comments = result.getContent();

        // 댓글 및 자식 댓글 처리
        comments.forEach(comment -> {
            updateWriterStatus(comment, writerId);

            // 자식 댓글 처리
            List<Comment> children = loadPort.loadCommentsByCommentId(comment.getId());
            children.forEach(child -> updateWriterStatus(child, writerId));

            comment.changeChildren(children);
        });

        // 댓글을 변경 후 다시 반환
        return new PageImpl<>(comments, pageable, result.getTotalElements());
    }
    /// 내부 메서드

    // 작성자 여부 판단 후 상태 변경
    private void updateWriterStatus(Comment comment, Long writerId) {
        if (comment.getMemberId().equals(writerId)) {
            comment.changeWriter();
        }
    }

    /// 댓글 삭제하기
    @Override
    public void removeComment(String id) {
        removePort.removeComment(id);
    }


}

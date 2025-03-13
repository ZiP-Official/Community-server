package com.zip.community.platform.application.service.comment;

import com.zip.community.common.response.CustomException;
import com.zip.community.common.response.errorcode.BoardErrorCode;
import com.zip.community.platform.application.port.in.comment.CreateCommentUseCase;
import com.zip.community.platform.application.port.in.comment.GetCommentUseCase;
import com.zip.community.platform.application.port.in.comment.RemoveCommentUseCase;
import com.zip.community.platform.adapter.in.web.dto.request.board.CommentRequest;
import com.zip.community.platform.application.port.out.comment.*;
import com.zip.community.platform.application.port.out.board.LoadBoardPort;
import com.zip.community.platform.application.port.out.member.MemberPort;
import com.zip.community.platform.domain.comment.CommentStatistics;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.zip.community.platform.domain.comment.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService implements CreateCommentUseCase, GetCommentUseCase, RemoveCommentUseCase {

    private final SaveCommentPort savePort;
    private final SaveCommentReactionPort saveReactionPort;

    private final LoadCommentPort loadPort;

    private final LoadCommentReactionPort loadReactionPort;
    private final LoadBoardPort loadBoardPort;

    private final RemoveCommentPort removePort;
    private final RemoveCommentReactionPort removeReactionPort;

    private final MemberPort memberPort;

    @Override
    public Comment createComment(CommentRequest request) {

        // 게시글 존재 여부 파악
        if (!loadBoardPort.existBoard(request.getBoardId())) {
            throw new CustomException(BoardErrorCode.NOT_FOUND_BOARD);
        }

        // 대댓글이 없는 경우 예외처리
        if (request.getParentId() != null) {
            if (!loadPort.getCheckedExistComment(request.getParentId())) {
                throw new CustomException(BoardErrorCode.NOT_FOUND_COMMENT);
            }
        }

        // Comment 객체 생성
        CommentStatistics statistics = CommentStatistics.of();
        Comment comment = Comment.of(
                request.getBoardId(), request.getMemberId(), request.getParentId(), request.getContent(), statistics, request.isAnonymous()
        );

        Comment saveComment = savePort.saveComment(comment);

        // Cache 댓글 개수 업데이트
        savePort.incrementCommentCount(comment.getBoardId());

        /// 게시글 작성자 여부 파악
        Long writerId = loadBoardPort.loadWriterIdByBoardId(request.getBoardId())
                .orElseThrow(() -> new CustomException(BoardErrorCode.NOT_FOUND_BOARD));

        /// 작성자 부분은 DB 저장없이, 처리한다.
        if (saveComment.getMemberId().equals(writerId)) {
            saveComment.changeWriter();
        }


        return saveComment;
    }

    /// 게시글 아이디로 댓글 가져오기
    @Override
    public Page<Comment> getByBoardId(Long boardId, Pageable pageable) {

        // 작성자 아이디 가져오기
        Long writerId = loadBoardPort.loadWriterIdByBoardId(boardId)
                .orElseThrow(() -> new CustomException(BoardErrorCode.NOT_FOUND_BOARD));

        // 게시글에 해당하는 댓글을 페이지 단위로 조회
        Page<Comment> result = loadPort.loadCommentsByBoardId(boardId, pageable);
        List<Comment> comments = new ArrayList<>(result.getContent());  // 가변 리스트로 복사


        // 댓글 및 자식 댓글 처리
        comments.forEach(comment -> {
            updateWriterStatus(comment, writerId);
            updateStatstics(comment);

            // 자식 댓글 처리
            List<Comment> children = loadPort.loadCommentsByCommentId(comment.getId());
            children.forEach(child -> {
                updateWriterStatus(child, writerId);
                updateStatstics(child);
            });

            comment.changeChildren(children);
        });

        /// 인기게시글 저장하기
        savePinnedComments(boardId);

        // 댓글을 변경 후 다시 반환
        return new PageImpl<>(comments, pageable, result.getTotalElements());
    }

    ///
    private void savePinnedComments(Long boardId) {

        // BoardId에 해당하는 댓글 목록을 로드하고, 이를 ArrayList로 변환
        List<Comment> list = new ArrayList<>(loadPort.loadCommentsByBoardId(boardId));

        // 인기게시글 적용하기
        saveReactionPort.savePinnedComment(list);
    }

    // 인기댓글 보기
    @Override
    public List<Comment> getPinnedComments(Long boardId) {
        List<Comment> comments = loadPort.getPinnedComment(boardId);
        comments.forEach(
                comment -> {
                    updateWriterStatus(comment, boardId);
                    updateStatstics(comment);
                }
        );

        return comments;
    }

    private void updateStatstics(Comment comment) {
        Long likeCount = loadReactionPort.loadCommentLikeCount(comment.getId());
        Long disLikeCount = loadReactionPort.loadCommentDisLikeCount(comment.getId());

        // 좋아요, 싫어요 값 넣기
        comment.getStatistics().bindReactionCount(likeCount, disLikeCount);
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
    @Transactional
    public void removeComment(String commentId, Long userId) {

        /// 해당 유저가 작성했는지 체크한다.

        /// 댓글이 존재하는지 체크한다.
        if (!loadPort.getCheckedExistComment(commentId)) {
            throw new CustomException(BoardErrorCode.NOT_FOUND_COMMENT);
        }

        // 댓글 자체의 삭제
        removePort.removeComment(commentId);

        // 댓글 관련 리액션 삭제
        removeReactionPort.removeAllByCommentId(commentId);

    }


}

package com.zip.community.platform.application.service.comment;

import com.zip.community.platform.application.port.in.comment.AddLikeReactionUseCase;
import com.zip.community.platform.application.port.in.comment.RemoveLikeReactionUseCase;
import com.zip.community.platform.adapter.in.web.dto.request.board.CommentReactionRequest;
import com.zip.community.platform.application.port.out.user.LoadMemberPort;
import com.zip.community.platform.application.port.out.comment.CommentReactionPort;
import com.zip.community.platform.application.port.out.comment.LoadCommentPort;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.zip.community.platform.domain.comment.Comment;
import com.zip.community.platform.domain.comment.CommentReaction;
import com.zip.community.platform.domain.board.UserReaction;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentReactionService implements AddLikeReactionUseCase, RemoveLikeReactionUseCase {

    private final CommentReactionPort commentReactionPort;
    private final LoadCommentPort loadCommentPort;
    private final LoadMemberPort loadMemberPort;

    @Override
    public CommentReaction addReaction(CommentReactionRequest request) {

        Comment comment = loadCommentPort.loadCommentById(request.getCommentId())
                .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다."));

        // 동일한 회원이 동일한 게시글에 이미 반응을 남겼는지 확인
        commentReactionPort.loadBoardReaction(comment.getId(), request.getMemberId()).ifPresent(reaction -> {
            throw new IllegalStateException("이미 반응을 눌렀습니다.");
        });

        // 새로운 반응 생성 및 저장
        CommentReaction reaction = CommentReaction.of(comment.getId(), request.getMemberId(), request.getReactionType());

        return commentReactionPort.saveBoardReaction(reaction);
    }

    @Override
    public void removeReaction(CommentReactionRequest request) {
        Comment comment = loadCommentPort.loadCommentById(request.getCommentId())
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        // 요청된 반응 유형 가져오기
        UserReaction reactionType = request.getReactionType();

        // 요청된 반응 찾기
        CommentReaction reaction = commentReactionPort.loadBoardReactionByType(comment.getId(), request.getMemberId(), reactionType)
                .orElseThrow(() -> new IllegalArgumentException("Specified reaction not found"));

        // 반응 삭제
        commentReactionPort.removeBoardReaction(reaction);

    }
}

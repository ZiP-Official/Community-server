package com.zip.community.platform.application.service.comment;

import com.zip.community.platform.application.port.in.board.response.ReactionStatus;
import com.zip.community.platform.application.port.in.comment.CommentReactionUseCase;
import com.zip.community.platform.adapter.in.web.dto.request.board.CommentReactionRequest;
import com.zip.community.platform.application.port.out.comment.LoadCommentReactionPort;
import com.zip.community.platform.application.port.out.comment.RemoveCommentReactionPort;
import com.zip.community.platform.application.port.out.user.LoadUserPort;
import com.zip.community.platform.application.port.out.comment.SaveCommentReactionPort;
import com.zip.community.platform.application.port.out.comment.LoadCommentPort;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentReactionService implements CommentReactionUseCase{

    private final SaveCommentReactionPort savePort;
    private final LoadCommentReactionPort loadPort;
    private final RemoveCommentReactionPort removePort;

    private final LoadCommentPort loadCommentPort;
    private final LoadUserPort loadUserPort;

    @Override
    public ReactionStatus addLikeReaction(CommentReactionRequest request) {

        // 예외처리
        checkException(request);

        // 이미 좋아요를 눌렀다면 삭제되도록 한다.
        if (loadPort.checkCommentLikeReaction(request.getCommentId(), request.getMemberId())) {
            removePort.removeCommentLikeReaction(request.getCommentId(), request.getMemberId());
            return ReactionStatus.REMOVED;
        }

        // 이미 싫어요를 눌렀다면, 해당 싫어요를 지우고 새롭게 좋아요를 추가한다.
        if (loadPort.checkCommentDisLikeReaction(request.getCommentId(), request.getMemberId())) {
            removePort.removeCommentDisLikeReaction(request.getCommentId(), request.getMemberId());
        }

        savePort.saveLikeCommentReaction(request.getCommentId(), request.getMemberId());
        return ReactionStatus.CREATED;
    }

    @Override
    public ReactionStatus addDisLikeReaction(CommentReactionRequest request) {

        // 예외처리
        checkException(request);


        // 이미 싫어요를 눌렀다면 삭제되도록 한다.
        if (loadPort.checkCommentDisLikeReaction(request.getCommentId(), request.getMemberId())) {
            removePort.removeCommentDisLikeReaction(request.getCommentId(), request.getMemberId());
            return ReactionStatus.REMOVED;
        }

        // 이미 좋아요를 눌렀다면, 해당 좋아요를 지우고 새롭게 싫어요를 추가한다.
        if (loadPort.checkCommentLikeReaction(request.getCommentId(), request.getMemberId())) {
            removePort.removeCommentLikeReaction(request.getCommentId(), request.getMemberId());
        }

        savePort.saveDisLikeCommentReaction(request.getCommentId(), request.getMemberId());
        return ReactionStatus.CREATED;
    }

    ///  내부 함수 처리
    private void checkException(CommentReactionRequest request) {

        // 댓글이 존재하지 않으면 증가시키면 안된다.
        if (!loadCommentPort.getCheckedExistComment(request.getCommentId())) {
            throw new EntityNotFoundException("해당하는 댓글이 존재하지 않습니다.");
        }

        // 존재하지 않는 유저가 반응을 해선 안된다.
        if (!loadUserPort.getCheckedExistUser(request.getMemberId())) {
            throw new EntityNotFoundException("해당하는 유저가 존재하지 않습니다.");
        }
    }
}

package com.zip.community.platform.application.service.board;

import com.zip.community.platform.application.port.in.board.ReactionUseCase;
import com.zip.community.platform.adapter.in.web.dto.request.board.BoardReactionRequest;
import com.zip.community.platform.application.port.out.board.LoadBoardPort;
import com.zip.community.platform.application.port.out.board.LoadBoardReactionPort;
import com.zip.community.platform.application.port.out.board.RemoveBoardReactionPort;
import com.zip.community.platform.application.port.out.board.SaveBoardReactionPort;
import com.zip.community.platform.application.port.out.user.LoadUserPort;
import com.zip.community.platform.application.port.in.board.response.ReactionStatus;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardReactionService implements ReactionUseCase {

    private final SaveBoardReactionPort saveReactionPort;
    private final LoadBoardReactionPort loadReactionPort;
    private final RemoveBoardReactionPort removeReactionPort;

    private final LoadBoardPort loadBoardPort;
    private final LoadUserPort loadUserPort;

    /*
        좋아요나, 싫어요같은 기능은 사람들이 누르는 경우가 많으니
        레디스를 통해 성능을 향상시킨다.

        레디스로 값을 변경 시킨 후, 한번에 DB에 저장하는 형태로 기록한다.
     */

    /// 감정표현 추가 UseCase
    // 좋아요 반응 생성 및 저장
    @Override
    public ReactionStatus addLikeReaction(BoardReactionRequest request) {
        checkException(request);

        if (loadReactionPort.checkBoardLikeReaction(request.getBoardId(), request.getMemberId())) {
            removeReactionPort.removeBoardLikeReaction(request.getBoardId(), request.getMemberId());
            return ReactionStatus.REMOVED;
        }

        if (loadReactionPort.checkBoardDisLikeReaction(request.getBoardId(), request.getMemberId())) {
            removeReactionPort.removeBoardDisLikeReaction(request.getBoardId(), request.getMemberId());
        }

        saveReactionPort.saveLikeBoardReaction(request.getBoardId(), request.getMemberId());
        return ReactionStatus.CREATED;
    }

    @Override
    public ReactionStatus addDisLikeReaction(BoardReactionRequest request) {
        checkException(request);

        if (loadReactionPort.checkBoardDisLikeReaction(request.getBoardId(), request.getMemberId())) {
            removeReactionPort.removeBoardDisLikeReaction(request.getBoardId(), request.getMemberId());
            return ReactionStatus.REMOVED;
        }

        if (loadReactionPort.checkBoardLikeReaction(request.getBoardId(), request.getMemberId())) {
            removeReactionPort.removeBoardLikeReaction(request.getBoardId(), request.getMemberId());
        }

        saveReactionPort.saveDisLikeBoardReaction(request.getBoardId(), request.getMemberId());
        return ReactionStatus.CREATED;
    }

    /// 내부 예외 체크 함수
    private void checkException(BoardReactionRequest request) {
        if (!loadBoardPort.existBoard(request.getBoardId())) {
            throw new EntityNotFoundException("해당 게시판이 존재하지 않습니다.");
        }
        if (!loadUserPort.getCheckedExistUser(request.getMemberId())) {
            throw new EntityNotFoundException("해당하는 멤버가 존재하지 않습니다.");
        }
    }

}

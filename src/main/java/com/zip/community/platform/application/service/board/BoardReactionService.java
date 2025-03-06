package com.zip.community.platform.application.service.board;

import com.zip.community.platform.application.port.in.board.AddReactionUseCase;
import com.zip.community.platform.application.port.in.board.RemoveReactionUseCase;
import com.zip.community.platform.adapter.in.web.dto.request.board.BoardReactionRequest;
import com.zip.community.platform.application.port.out.board.LoadBoardPort;
import com.zip.community.platform.application.port.out.board.LoadBoardReactionPort;
import com.zip.community.platform.application.port.out.board.RemoveBoardReactionPort;
import com.zip.community.platform.application.port.out.board.SaveBoardReactionPort;
import com.zip.community.platform.application.port.out.user.LoadUserPort;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardReactionService implements AddReactionUseCase, RemoveReactionUseCase {

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
    public void addLikeReaction(BoardReactionRequest request) {

        // 게시글과 유저에 대한 예외처리
        checkException(request);

        // 이미 좋아요를 눌렀다면, 한번 더 누르지 못하도록 예외처리
        if (loadReactionPort.checkBoardLikeReaction(request.getBoardId(), request.getMemberId())) {

            throw new IllegalStateException("이미 해당 글에 좋아요를 눌렀습니다.");
        }

        // 이미 싫어요에 등록했다면 싫어요가 취소되도록 설정
        if(loadReactionPort.checkBoardDisLikeReaction(request.getBoardId(), request.getMemberId())){
            removeReactionPort.removeBoardDisLikeReaction(request.getBoardId(), request.getMemberId());
        }

        // Set에 등록하기 때문에, 중복으로 저장되지 않는다.
        saveReactionPort.saveLikeBoardReaction(request.getBoardId(), request.getMemberId());
    }

    // 싫어요 반응 생성 및 저장
    @Override
    public void addDisLikeReaction(BoardReactionRequest request) {

        // 보드 예외처리
        checkException(request);

        // 이미 싫어요를 눌렀다면, 한번 더 누르지 못하도록 예외처리
        if (loadReactionPort.checkBoardLikeReaction(request.getBoardId(), request.getMemberId())) {
            throw new IllegalStateException("이미 해당 글에 싫어요를 눌렀습니다.");
        }

        // 이미 좋아요에 등록했다면 좋아요가 취소되도록 설정
        if(loadReactionPort.checkBoardLikeReaction(request.getBoardId(), request.getMemberId())){
            removeReactionPort.removeBoardLikeReaction(request.getBoardId(), request.getMemberId());
        }

        // 싫어요 반응 생성 및 저장
        saveReactionPort.saveDisLikeBoardReaction(request.getBoardId(), request.getMemberId());

    }

    private void checkException(BoardReactionRequest request) {

        // 보드 예외처리
        if(!loadBoardPort.existBoard(request.getBoardId())) {
            throw new EntityNotFoundException("해당 게시판이 존재하지 않습니다");
        }

        // 회원 예외처리
        if(!loadUserPort.existsById(request.getMemberId())){
            throw new EntityNotFoundException("해당하는 멤버가 존재하지 않습니다.");
        }
    }

    /// 감정표현 제거 UseCase
    @Override
    public void removeLikeReaction(BoardReactionRequest request) {

        // 요청된 반응 찾기
        if(!loadReactionPort.checkBoardLikeReaction(request.getBoardId(), request.getMemberId())){
            throw new IllegalStateException("해당유저는 아직 아무런 감정표현을 누르지 않았습니다.");
        }
        // 반응 삭제
        removeReactionPort.removeBoardLikeReaction(request.getBoardId(), request.getMemberId());
    }

    @Override
    public void removeDisLikeReaction(BoardReactionRequest request) {

        // 요청된 반응 찾기
        if(!loadReactionPort.checkBoardLikeReaction(request.getBoardId(), request.getMemberId())){
            throw new IllegalStateException("해당유저는 아직 아무런 감정표현을 누르지 않았습니다.");
        }

        // 반응 삭제
        removeReactionPort.removeBoardDisLikeReaction(request.getBoardId(), request.getMemberId());


    }
}

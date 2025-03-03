package com.zip.community.platform.application.service.board;

import com.zip.community.platform.application.port.in.board.AddReactionUseCase;
import com.zip.community.platform.application.port.in.board.RemoveReactionUseCase;
import com.zip.community.platform.adapter.in.web.dto.request.board.BoardReactionRequest;
import com.zip.community.platform.application.port.out.board.LoadBoardPort;
import com.zip.community.platform.application.port.out.board.LoadBoardReactionPort;
import com.zip.community.platform.application.port.out.board.RemoveBoardReactionPort;
import com.zip.community.platform.application.port.out.board.SaveBoardReactionPort;
import com.zip.community.platform.application.port.out.user.LoadMemberPort;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.zip.community.platform.domain.board.Board;
import com.zip.community.platform.domain.board.BoardReaction;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardReactionService implements AddReactionUseCase, RemoveReactionUseCase {

    private final SaveBoardReactionPort saveReactionPort;
    private final LoadBoardReactionPort loadReactionPort;
    private final RemoveBoardReactionPort removeReactionPort;
    private final LoadBoardPort loadBoardPort;
    private final LoadMemberPort loadMemberPort;

    @Override
    public BoardReaction addReaction(BoardReactionRequest request) {

        // 보드
        Board board = loadBoardPort.loadBoardById(request.getBoardId())
                .orElseThrow(() -> new EntityNotFoundException("해당 게시판이 존재하지 않습니다"));

        // 동일한 회원이 동일한 게시글에 이미 반응을 남겼는지 확인
        loadReactionPort.loadBoardReaction(board.getId(), request.getMemberId()).ifPresent(reaction -> {
            throw new IllegalStateException("리액션이 이미 존재합니다.");
        });

        // 새로운 반응 생성 및 저장
        BoardReaction reaction = BoardReaction.of(request.getBoardId(), request.getMemberId(), request.getReactionType());

        return saveReactionPort.saveBoardReaction(reaction);
    }

    @Override
    public void removeReaction(BoardReactionRequest request) {

        // 요청된 반응 찾기
        BoardReaction boardReaction = loadReactionPort.loadBoardReactionByType(request.getBoardId(), request.getMemberId(), request.getReactionType())
                .orElseThrow(() -> new IllegalArgumentException("리액션을 삭제할 수 없습니다."));

        // 반응 삭제
        removeReactionPort.removeBoardReaction(boardReaction);
    }
}

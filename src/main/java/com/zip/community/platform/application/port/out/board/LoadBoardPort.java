package com.zip.community.platform.application.port.out.board;

import com.zip.community.platform.domain.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface LoadBoardPort {

    Optional<Board> loadBoardById(Long boardId);

//    List<Category> findRootCategories();

    Page<Board> loadByCategoryId(Long categoryId, Pageable pageable);
}

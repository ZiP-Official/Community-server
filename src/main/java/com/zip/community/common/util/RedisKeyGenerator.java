package com.zip.community.common.util;

import static com.zip.community.common.util.CacheNames.*;

public class RedisKeyGenerator {

    public static String getUserCommentBlock(String userId) {
        return USER_COMMENT_BLOCK + SEPARATOR + userId;
    }


    public static String getBoardViewCountKey(Long boardId) {
        return BOARD_VIEW_COUNT + SEPARATOR +  boardId;
    }

    public static String getBoardViewCountSetKey() {
        return BOARD_VIEW_COUNT_SET;
    }

    public static String getBoardLikeKey(Long boardId) {
        return BOARD_LIKE + SEPARATOR +  boardId;
    }

    public static String getBoardDisLikeKey(Long boardId) {
        return BOARD_DISLIKE + SEPARATOR +  boardId;
    }

    public static String getCommentLikeKey(String commentId) {
        return COMMENT_LIKE + SEPARATOR + commentId;
    }

    public static String getPinnedCommentKey(Long boardId) {
        return COMMENT_PINNED + SEPARATOR + boardId;
    }

}

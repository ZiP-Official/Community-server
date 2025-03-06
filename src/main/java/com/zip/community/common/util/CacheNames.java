package com.zip.community.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.lang.reflect.Modifier.isStatic;

public class CacheNames {
    public static final String SEPARATOR = ":";

    public static final String USER = "user";
    public static final String USER_COMMENT_BLOCK = USER + SEPARATOR + "comment-block";

    public static final String BOARD = "board";
    public static final String BOARD_LIST = BOARD + SEPARATOR + "list";
    public static final String BOARD_VIEW_COUNT = BOARD + SEPARATOR + "view-count";
    public static final String BOARD_VIEW_COUNT_SET = BOARD + SEPARATOR + "view-count-set";
    public static final String BOARD_LIKE = BOARD + SEPARATOR + "like";
    public static final String BOARD_DISLIKE = BOARD + SEPARATOR + "dislike";

    public static final String COMMENT = "comment";
    public static final String COMMENT_LIKE = COMMENT + SEPARATOR + "like";
    public static final String COMMENT_PINNED = COMMENT + SEPARATOR + "pinned";

    public static List<String> getCacheNames() {
        List<String> cacheNames = new ArrayList<>();

        Field[] declaredFields = CacheNames.class.getDeclaredFields();
        for (Field field : declaredFields) {
            if (isStatic(field.getModifiers())) {
                try {
                    if (!SEPARATOR.equals(field.get(null))) {
                        cacheNames.add((String) field.get(null));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return cacheNames;
    }
}

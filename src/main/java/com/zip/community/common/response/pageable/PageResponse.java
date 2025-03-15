package com.zip.community.common.response.pageable;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResponse<E> {

    private List<E> dtoList;
    private List<Integer> pageNumList;
    private PageRequest pageRequest;

    private boolean prev, next;
    private int totalCount, prevPage, nextPage, totalPage, current;

    @Builder(builderMethodName = "withAll")
    public PageResponse(List<E> dtoList, PageRequest pageRequest, long total) {
        this.dtoList = dtoList;
        this.pageRequest = pageRequest;
        this.totalCount = (int) total;

        int current = pageRequest.getPage();
        this.current = current;

        int pageSize = pageRequest.getSize();
        int last = (int) Math.ceil((double) totalCount / pageSize);

        int end = (int) (Math.ceil(current / 10.0)) * 10;
        int start = end - 9;

        end = Math.min(end, last);

        this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

        this.prev = current > 1;
        this.next = current < last;

        this.prevPage = prev ? current - 1 : 0;
        this.nextPage = next ? current + 1 : 0;

        this.totalPage = last;
    }
}

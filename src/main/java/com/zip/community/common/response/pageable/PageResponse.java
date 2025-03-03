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
        this.totalCount = (int)total;

        int end =   (int)(Math.ceil( pageRequest.getPage() / 10.0 )) *  10;

        int start = end - 9;

        int last =  (int)(Math.ceil((totalCount/(double) pageRequest.getSize())));

        end =  end > last ? last: end;

        this.prev = start > 1;
        this.next =  totalCount > end * pageRequest.getSize();

        this.pageNumList = IntStream.rangeClosed(start,end).boxed().collect(Collectors.toList());

        if(prev) {
            this.prevPage = start -1;
        }

        if(next) {
            this.nextPage = end + 1;
        }

        this.totalPage = this.pageNumList.size();

        this.current = pageRequest.getPage();

    }
}


package com.example.demo.model.DTO.paging;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;

@Data
public class APIPageableDTO {
    int pageNumber;
    int pageSize;
    int offset;
    int numberOfElements;
    long totalElements;
    int totalPages;
    boolean sorted;
    boolean first;
    boolean last;
    boolean empty;

    public <T> APIPageableDTO(Page<T> page)
    {
        Pageable pageable = page.getPageable();
        setPageNumber(pageable.getPageNumber());
        setPageSize(pageable.getPageSize());
        setNumberOfElements(page.getNumberOfElements());
        setTotalElements(page.getTotalElements());
        setTotalPages(page.getTotalPages());
        setSorted(pageable.getSort().isSorted());
        setFirst(page.isFirst());
        setLast(page.isLast());
        setEmpty(page.isEmpty());
    }
}

package com.senpare.paymentservice.utilities;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public class Util {

    public static <T, R extends JpaRepository> Page<T> paginate(R repository, int page, int size, String sortBy) {
        Pageable pageRequest = getPageable(page, size, sortBy);

        return repository.findAll(pageRequest == null ? Pageable.unpaged() : pageRequest);
    }

    public static Pageable getPageable(int page, int size, String sortBy) {
        int actualPage = page - 1;
        Pageable pageRequest;
        if(sortBy != null && !sortBy.isEmpty()) {
            if(sortBy.startsWith("-")) {
                sortBy = sortBy.substring(1);
                pageRequest =  PageRequest.of(actualPage, size, Sort.by(Sort.Direction.DESC, sortBy));
            } else {
                pageRequest =  PageRequest.of(actualPage, size, Sort.by(Sort.Direction.ASC, sortBy));
            }
        } else {
            pageRequest =  PageRequest.of(actualPage, size);
        }
        return pageRequest;
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotNullAndEmpty(String str) {
        return str != null && !str.isEmpty();
    }

}

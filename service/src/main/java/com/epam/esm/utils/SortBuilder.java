package com.epam.esm.utils;

import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SortBuilder {
    public static Sort buildSort(String sorting, String sortingOrder) {
        Sort sort = Sort.unsorted();
        if (sorting == null) {
            return sort;
        }
        if (sortingOrder == null) {
            sortingOrder = "asc";
        }
        sort = buildSortBody(sort, sorting);
        return buildSortOrder(sort, sortingOrder);
    }

    private static Sort buildSortBody(Sort sort, String sorting) {
        List<String> sortElements = Arrays.stream(sorting.split(",")).map(String::trim).collect(Collectors.toList());
        if (sortElements.size() <= 1) {
            sort = Sort.by(sorting);
        } else {
            sort = Sort.by(sortElements.get(0)).and(Sort.by(sortElements.get(1)));
        }
        return sort;
    }

    private static Sort buildSortOrder(Sort sort, String sortingOrder) {
        if (sortingOrder.equals("asc")) {
            return sort.ascending();
        } else {
            return sort.descending();
        }
    }
}

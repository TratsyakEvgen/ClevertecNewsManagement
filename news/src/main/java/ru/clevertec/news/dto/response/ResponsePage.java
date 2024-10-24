package ru.clevertec.news.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ResponsePage<T> {
    private List<T> content;
    private int page;
    private int size;
    private int totalElements;
    private int totalPages;
}
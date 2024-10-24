package ru.clevertec.news.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResponseNewWithComments extends ResponseNews{
    private ResponsePage<ResponseComment> comments;
}

package ru.clevertec.news.mapper;

import org.mapstruct.*;
import org.springframework.data.domain.Page;
import ru.clevertec.news.dto.request.CreateComment;
import ru.clevertec.news.dto.request.UpdateComment;
import ru.clevertec.news.dto.response.ResponseComment;
import ru.clevertec.news.entity.Comment;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {
    @Mapping(target = "newsId", source = "news.id")
    ResponseComment toResponseComment(Comment comment);

    Page<ResponseComment> toResponsePage(Page<Comment> page);

    Comment toComment(CreateComment createComment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(UpdateComment updateComment, @MappingTarget Comment comment);

}

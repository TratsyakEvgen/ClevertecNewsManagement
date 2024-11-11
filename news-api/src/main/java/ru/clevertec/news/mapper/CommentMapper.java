package ru.clevertec.news.mapper;

import org.mapstruct.*;
import ru.clevertec.news.dto.request.CreateComment;
import ru.clevertec.news.dto.request.UpdateComment;
import ru.clevertec.news.dto.response.ResponseComment;
import ru.clevertec.news.entity.Comment;

/**
 * Конвертор комментариев
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {
    /**
     * Конвертирует сущность комментария в DTO
     *
     * @param comment сущность комментария
     * @return DTO комментария
     */
    @Mapping(target = "newsId", source = "news.id")
    ResponseComment toResponseComment(Comment comment);

    /**
     * Конвертирует DTO в сущность комментария
     *
     * @param createComment DTO комментария
     * @return сущность комментария
     */
    Comment toComment(CreateComment createComment);

    /**
     * Частичное обновление сущности комментария. Null поля для updateComment игнорируются
     *
     * @param updateComment информация об обновлении
     * @param comment       обновляемая сущность
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(UpdateComment updateComment, @MappingTarget Comment comment);

}

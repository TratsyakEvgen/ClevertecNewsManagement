package ru.clevertec.news.mapper;

import org.mapstruct.*;
import ru.clevertec.news.dto.request.CreateNews;
import ru.clevertec.news.dto.request.UpdateNews;
import ru.clevertec.news.dto.response.ResponseNewWithComments;
import ru.clevertec.news.dto.response.ResponseNews;
import ru.clevertec.news.entity.News;

/**
 * Конвертор новостей
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = CommentMapper.class)
public interface NewsMapper {
    /**
     * Конвертирует сущность новости в DTO новости
     *
     * @param news сущность новости
     * @return DTO новости
     */
    ResponseNews toResponseNews(News news);

    /**
     * Конвертирует сущность новости в DTO новости
     *
     * @param news сущность новости
     * @return DTO новости
     */
    @Mapping(target = "comments", ignore = true)
    ResponseNewWithComments toResponseNewWithComments(News news);

    /**
     * Конвертирует DTO новости в сущность новости
     *
     * @param createNews DTO новости
     * @return сущность новости
     */
    News toNews(CreateNews createNews);


    /**
     * Частичное обновление сущности новостей. Null поля для updateNews игнорируются
     *
     * @param updateNews информация об обновлении
     * @param news       обновляемая сущность
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(UpdateNews updateNews, @MappingTarget News news);
}
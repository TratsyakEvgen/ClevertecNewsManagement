package ru.clevertec.news.mapper;

import org.mapstruct.*;
import org.springframework.data.domain.Page;
import ru.clevertec.news.dto.request.CreateNews;
import ru.clevertec.news.dto.request.UpdateNews;
import ru.clevertec.news.dto.response.ResponseNewWithComments;
import ru.clevertec.news.dto.response.ResponseNews;
import ru.clevertec.news.dto.response.ResponsePage;
import ru.clevertec.news.entity.News;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = CommentMapper.class)
public interface NewsMapper {
    ResponseNews toResponseNews(News news);

    @Mapping(target = "comments", ignore = true)
    ResponseNewWithComments toResponseNewWithComments(News news);

    ResponsePage<ResponseNews> toResponsePage(Page<News> newsPage);

    News toNews(CreateNews createNews);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    News partialUpdate(UpdateNews updateNews, @MappingTarget News news);
}
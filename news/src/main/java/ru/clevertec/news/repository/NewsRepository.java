package ru.clevertec.news.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.news.dto.request.Filter;
import ru.clevertec.news.entity.News;

public interface NewsRepository extends JpaRepository<News, Long> {
    @Query(value = "SELECT * FROM news WHERE " +
            "(cast(:#{#filter.search} as text) is null or text_tsv @@ plainto_tsquery(:#{#filter.search}))",
            nativeQuery = true)
    Page<News> findAllUseFilter(Pageable pageable, Filter filter);
}
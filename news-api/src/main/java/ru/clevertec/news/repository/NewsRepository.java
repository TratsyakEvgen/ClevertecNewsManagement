package ru.clevertec.news.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.news.dto.request.SearchText;
import ru.clevertec.news.entity.News;

/**
 * Репозиторий новостей
 */
public interface NewsRepository extends JpaRepository<News, Long> {

    /**
     * Осуществляет постраничный полнотекстовый поиск новостей.
     * SearchText должен быть не равен null,
     * но в случае searchText.text == null полнотекстовый поиск игнорируется
     *
     * @param pageable   информация о пагинации
     * @param searchText искомый текст
     * @return страница новостей
     */
    @Query(value = "SELECT * FROM news WHERE " +
            "(cast(:#{#searchText.text} as text) is null or text_tsv @@ plainto_tsquery(:#{#searchText.text}))",
            nativeQuery = true)
    Page<News> findAllWithText(Pageable pageable, SearchText searchText);
}
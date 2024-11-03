package ru.clevertec.news.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.news.dto.request.SearchText;
import ru.clevertec.news.entity.Comment;

import java.util.Optional;

/**
 * Репозиторий комментариев
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Удаляет комментарий принадлежащий новости
     *
     * @param newsId id новости
     * @param id     id комментария
     */
    void deleteByNewsIdAndId(long newsId, long id);

    /**
     * Осуществляет поиск комментария в новости
     *
     * @param newsId id новости
     * @param id     id комментария
     * @return комментарий
     */
    Optional<Comment> findByNewsIdAndId(long newsId, long id);
    /**
     * Осуществляет постраничный полнотекстовый поиск комментариев.
     * SearchText должен быть не равен null,
     * но в случае searchText.text == null полнотекстовый поиск игнорируется
     *
     * @param pageable информация о пагинации
     * @param searchText искомый текст
     * @return страница новостей
     */
    @Query(value = "SELECT * FROM comments WHERE news_id = :newsId AND " +
            "(cast(:#{#searchText.text} as text) is null or text_tsv @@ plainto_tsquery(:#{#searchText.text}))",
            nativeQuery = true)
    Page<Comment> findByNewsIdWithText(long newsId, Pageable pageable, SearchText searchText);

}
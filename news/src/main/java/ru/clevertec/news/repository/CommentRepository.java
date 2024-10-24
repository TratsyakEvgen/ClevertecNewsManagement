package ru.clevertec.news.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.news.dto.request.Filter;
import ru.clevertec.news.entity.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteByNewsIdAndId(long newsId, long id);

    Optional<Comment> findByNewsIdAndId(long newsId, long id);

    @Query(value = "SELECT * FROM comments WHERE news_id = :newsId AND " +
            "(cast(:#{#filter.search} as text) is null or text_tsv @@ plainto_tsquery(:#{#filter.search}))",
            nativeQuery = true)
    Page<Comment> findByNewsIdUseFilter(long newsId, Pageable pageable, Filter filter);

}
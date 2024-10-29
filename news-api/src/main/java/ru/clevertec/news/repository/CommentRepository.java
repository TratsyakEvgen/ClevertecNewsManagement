package ru.clevertec.news.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.news.dto.request.SearchText;
import ru.clevertec.news.entity.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteByNewsIdAndId(long newsId, long id);

    Optional<Comment> findByNewsIdAndId(long newsId, long id);

    @Query(value = "SELECT * FROM comments WHERE news_id = :newsId AND " +
            "(cast(:#{#searchText.text} as text) is null or text_tsv @@ plainto_tsquery(:#{#searchText.text}))",
            nativeQuery = true)
    Page<Comment> findByNewsIdWithText(long newsId, Pageable pageable, SearchText searchText);

}
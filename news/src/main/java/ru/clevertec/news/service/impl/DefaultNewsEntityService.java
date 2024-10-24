package ru.clevertec.news.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.news.dto.request.Filter;
import ru.clevertec.news.entity.News;
import ru.clevertec.news.repository.NewsRepository;
import ru.clevertec.news.service.NewsEntityService;
import ru.clevertec.news.service.ServiceException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultNewsEntityService implements NewsEntityService {
    private final NewsRepository newsRepository;

    @Override
    public News save(News news) {
        return newsRepository.save(news);
    }

    @Override
    public Page<News> findAll(Pageable pageable, Filter filter) {
        Filter currentFilter = Optional.ofNullable(filter)
                .orElseGet(Filter::new);
        return newsRepository.findAllUseFilter(pageable, currentFilter);
    }

    @Override
    public News findById(long id) {
        return newsRepository.findById(id)
                .orElseThrow(() -> new ServiceException("News with id " + id + " not found"));
    }

    @Override
    public void deleteById(long id) {
        newsRepository.deleteById(id);
    }
}

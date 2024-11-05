package ru.clevertec.news.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.news.dto.request.CreateComment;
import ru.clevertec.news.dto.request.SearchText;
import ru.clevertec.news.dto.request.UpdateComment;
import ru.clevertec.news.dto.response.ResponseComment;
import ru.clevertec.news.service.CommentService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {
    private CommentController commentController;
    @Mock
    private CommentService commentService;


    @BeforeEach
    void setUp() {
        commentController = new CommentController(commentService);
    }

    @Test
    void getComment() {
        ResponseComment responseComment = new ResponseComment().setId(1);
        when(commentService.get(1,1)).thenReturn(responseComment);

        ResponseComment actual = commentController.getComment(1, 1);

        verify(commentService, times(1)).get(1, 1);
        assertEquals(responseComment,actual);
    }

    @Test
    void getAllComments() {
        Pageable pageable = PageRequest.of(1, 1);
        SearchText searchText = new SearchText("text");
        Page<ResponseComment> page = new PageImpl<>(Collections.emptyList());
        when(commentService.get(1,pageable,searchText)).thenReturn(page);

        Page<ResponseComment> actual = commentController.getAllComments(1, pageable, searchText);

        verify(commentService, times(1)).get(1, pageable, searchText);
        assertEquals(page, actual);
    }

    @Test
    void createComment() {
        CreateComment createComment = new CreateComment("user", "text");
        ResponseComment responseComment = new ResponseComment().setId(1);
        when(commentService.create(1, createComment)).thenReturn(responseComment);

        ResponseComment actual = commentController.createComment(1, createComment);

        verify(commentService, times(1)).create(1, createComment);
        assertEquals(responseComment,actual);
    }

    @Test
    void updateComment() {
        UpdateComment updateComment = new UpdateComment("text");
        ResponseComment responseComment = new ResponseComment().setId(1);
        when(commentService.update(1,1, updateComment)).thenReturn(responseComment);

        ResponseComment actual = commentController.updateComment(1, 1, updateComment);

        verify(commentService, times(1)).update(1, 1, updateComment);
        assertEquals(responseComment,actual);
    }

    @Test
    void deleteComment() {
        commentController.deleteComment(1, 1);

        verify(commentService, times(1)).delete(1, 1);
    }
}
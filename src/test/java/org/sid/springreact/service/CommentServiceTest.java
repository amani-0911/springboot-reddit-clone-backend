package org.sid.springreact.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sid.springreact.exceptions.SpringRedditException;


import static org.junit.jupiter.api.Assertions.*;

public class CommentServiceTest {

    @Test
    @DisplayName("test should Pass when Comment do not contains Swear Word")
    void shouldNotContainSwearWordsInsideComment() {
        CommentService commentService=new CommentService(null,null,null,null,null,null,null);
        org.assertj.core.api.Assertions.assertThat(commentService.containsSwearWords("this is  a clean comment")).isFalse();
    }

    @Test
    @DisplayName("Should throw Exception when Exception Contains Swear Words")
    public void shouldFailWhenCommentContainsSwaerWords(){
        CommentService commentService=new CommentService(null,null,null,null,null,null,null);
     //   SpringRedditException exception=assertThrows(SpringRedditException.class,
     //           ()->{
       //     commentService.containsSwearWords("this is shitty comment");
     //           });
     //   assertTrue(exception.getMessage().contains("Comments contains unacceptable language"));
        org.assertj.core.api.Assertions.assertThatThrownBy(()->{
            commentService.containsSwearWords("this is shitty comment");
        }).isInstanceOf(SpringRedditException.class)
        .hasMessage("Comments contains unacceptable language");
    }


}
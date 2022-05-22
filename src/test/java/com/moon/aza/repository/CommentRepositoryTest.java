package com.moon.aza.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void commentRepositoryTest1(){
        List<Long> result = commentRepository.findByMemberId(67L);
        for(Long value : result){
            System.out.println(value);
        }
    }

}
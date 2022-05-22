package com.moon.aza.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class LikesRepositoryTest {
    @Autowired
    private LikesRepository likesRepository;

    @Test
    public void findByMemberIdTest1(){
        List<Long> result = likesRepository.findByMemberId(67L);
        for(Long value : result)
            System.out.println(value);
    }
}
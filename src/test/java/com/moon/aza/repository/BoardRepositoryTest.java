package com.moon.aza.repository;

import com.moon.aza.entity.Board;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardRepositoryTest {
    @Autowired
    private BoardRepository boardRepository;

    @Transactional
    @Test
    public void test1(){
      List<Long> result = boardRepository.findByMemberId(14L);
      for(Long value : result)
          System.out.println(value);
    }
}
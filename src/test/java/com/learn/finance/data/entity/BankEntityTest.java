package com.learn.finance.data.entity;

import com.learn.finance.data.repository.BankRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class BankEntityTest {

    @Autowired
    private BankRepository bankRepository;

    @BeforeEach
    void init() {
        BankEntity bankEntity = new BankEntity();
        bankEntity.setBankName("Test");
        bankEntity.setBic("testbic");

        CommentEntity comment = new CommentEntity();
        comment.setComment("Test comment");
        comment.setBank(bankEntity);
        bankEntity.getCommentEntities().add(comment);
        bankRepository.save(bankEntity);
    }

    @Test
    void testLazy() {
        BankEntity bankEntity = bankRepository.findByBic("testbic");
        bankEntity.getCommentEntities();
        assertEquals(1, bankEntity.getCommentEntities().size());
    }

    @AfterEach
    void tearDown() {
        bankRepository.deleteAll();
    }

}
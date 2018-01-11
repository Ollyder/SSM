package com.yuhang.service.impl;

import com.yuhang.BaseTest;
import com.yuhang.dto.AppointExecution;
import com.yuhang.exception.AppointException;
import com.yuhang.service.BookService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BookServoceImplTest extends BaseTest {

    @Autowired
    private BookService bookService;

    @Test
    public void testAppoint() throws Exception {
        long bookId = 1001;
        long studentId = 12345678910L;
        AppointExecution execution = bookService.appoint(bookId,studentId);
        System.out.println(execution);
    }
}

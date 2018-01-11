package com.yuhang.service;

import com.yuhang.dto.AppointExecution;
import com.yuhang.entity.Book;

import java.util.List;


public interface BookService {

    Book getById(long bookid);

    List<Book> getList();

    AppointExecution appoint(long bookId,long studentId);
}

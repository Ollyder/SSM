package com.yuhang.dao;

import com.yuhang.entity.Book;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookDao {
    Book queryById(long id);

    List<Book> queryAll(@Param("offset")int offest,@Param("limit")int limit);

    int reduceNumber(long bookId);
}

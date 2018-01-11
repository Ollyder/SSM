package com.yuhang.service.impl;

import com.yuhang.dao.AppointmentDao;
import com.yuhang.dao.BookDao;
import com.yuhang.dto.AppointExecution;
import com.yuhang.entity.Appointment;
import com.yuhang.entity.Book;
import com.yuhang.enums.AppointStateEnum;
import com.yuhang.exception.AppointException;
import com.yuhang.exception.NoNumberException;
import com.yuhang.exception.RepeatAppointException;
import com.yuhang.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class BookServiceImpl implements BookService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BookDao bookDao;

    @Autowired
    private AppointmentDao appointmentDao;

    public Book getById(long bookid) {
        return bookDao.queryById(bookid);
    }

    public List<Book> getList() {
        return bookDao.queryAll(0,1000);
    }

    @Transactional
    public AppointExecution appoint(long bookId, long studentId) {
        try {
            int update = bookDao.reduceNumber(bookId);
            if(update<=0) {
                throw new NoNumberException("no number");
            } else {
                int insert = appointmentDao.insertAppointment(bookId,studentId);
                if(insert<0) {
                    throw new RepeatAppointException("repeat appoint");
                } else {
                    Appointment appointment = appointmentDao.queryByKeyWithBook(bookId,studentId);
                    return new AppointExecution(bookId, AppointStateEnum.SUCCESS,appointment);
                }
            }
        } catch(NoNumberException e) {
            throw e;
        } catch (RepeatAppointException e2) {
            throw e2;
        } catch (Exception e3) {
            logger.error(e3.getMessage(),e3);
            throw new AppointException("appoint inner error:" + e3.getMessage());
        }
    }
}

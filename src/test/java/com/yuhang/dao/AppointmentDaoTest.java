package com.yuhang.dao;

import com.yuhang.BaseTest;
import com.yuhang.entity.Appointment;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AppointmentDaoTest extends BaseTest {

    @Autowired
    private AppointmentDao appointmentDao;

    @Test
    public void testInsertAppointment() throws Exception {
        long bookId = 1000L;
        long studentId = 123455667L;
        int insert = appointmentDao.insertAppointment(bookId,studentId);
        System.out.println(insert);
    }

    @Test
    public void testQueryByKeyWithBook() throws  Exception {
        long bookId = 1000L;
        long studentId = 123455667L;
        Appointment appointment = appointmentDao.queryByKeyWithBook(bookId,studentId);
        System.out.println(appointment);
        System.out.println(appointment.getBook());
    }
}

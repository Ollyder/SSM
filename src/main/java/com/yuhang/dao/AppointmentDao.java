package com.yuhang.dao;

import com.yuhang.entity.Appointment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentDao {
    int insertAppointment(@Param("bookId") long bookId,@Param("studentId")long studentId);

    Appointment queryByKeyWithBook(@Param("bookId")long bookId,@Param("studentId")long studentId);
}

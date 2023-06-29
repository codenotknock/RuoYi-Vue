package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.TraGrades;
import com.ruoyi.system.service.ITraGradesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class TraGradesServiceImplTest {
    @Autowired
    private ITraGradesService traGradesService;


    @Test
    @Rollback
    void testInsertTraGrades() {
        Long c = 0L;
        TraGrades traGrades = new TraGrades();
        traGrades.setUserName("test1");
        traGrades.setTraCount(c);

        int result = traGradesService.insertTraGrades(traGrades);
        assertEquals(1, result);
        assertNotNull(traGrades.getId());
    }

    @Test
    @Rollback
    public void testUpdateTraGrades() {
        Long id = 11L;
        TraGrades traGrades = traGradesService.selectTraGradesById(id);

        Long c = 0L;
        traGrades.setTraCount(c);

        int result = traGradesService.updateTraGrades(traGrades);
        assertEquals(1, result);
    }


    @Test
    @Rollback
    public void testDeleteTraGradesById() {
        Long id = 11L;
        int result = traGradesService.deleteTraGradesById(id);
        assertEquals(1, result);
    }
    @Test
    @Rollback
    void updateTraGrades() {
        Long id = 11L;
        TraGrades traGrades = traGradesService.selectTraGradesById(id);
        assertNotNull(traGrades);

        Long c = 10L;
        traGrades.setTraCount(c);

        int result = traGradesService.updateTraGrades(traGrades);
        assertEquals(1, result);
    }

    @Test
    @Rollback
    void deleteTraGradesByIds() {
        Long[] ids = {11L, 12L, 13L};
        int result = traGradesService.deleteTraGradesByIds(ids);
        assertEquals(3, result);
    }

    @Test
    @Rollback
    void deleteTraGradesById() {
        Long id = 11L;
        int result = traGradesService.deleteTraGradesById(id);
        assertEquals(1, result);
    }


}
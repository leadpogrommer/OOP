package ru.leadpogrommer.oop.gradebook;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GradeBookTest {
    @Test
    void myData(){
        var gb = new GradeBook(4)
                .addSubject("Матан", 4, 5, null, null)
                .addSubject("Матлог", 5, 5, null, null)
                .addSubject("История", 5, null, null, null)
                .addSubject("ОКР", 5, null, null, null)
                .addSubject("Декларативка", 5, 5, null, null)
                .addSubject("Императивка", 5, 5, null, null)
                .addSubject("Хоцкина", 5, 4, null, null)
                .addSubject("ЦП", null, 5, null, null);
        assertFalse(gb.canGetRedDiploma());
        assertFalse(gb.hasBigScholarship(1));
        assertEquals(gb.averageGrade(), 4.7, 0.2);
    }
}
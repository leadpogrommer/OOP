package ru.leadpogrommer.oop.gradebook;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GradeBookTest {
    @Test
    void myData() {
        var gb = new GradeBook(4)
                .setGrade(1, "Матан", 4)
                .setGrade(1, "Дискретка", 5)
                .setGrade(1, "Декларативка", 5)
                .setGrade(1, "Императивка", 5)
                .setGrade(1, "История", 5)
                .setGrade(1, "ОКР", 5)
                .setGrade(2, "Дискретка", 5)
                .setGrade(2, "Декларативка", 5)
                .setGrade(2, "Императивка", 5)
                .setGrade(2, "Хоцкина", 4)
                .setGrade(2, "Иртегов-бог", 5);

        assertFalse(gb.canGetRedDiploma());
        assertFalse(gb.hasBigScholarship(1));
        assertEquals(gb.averageGrade(), 4.7, 0.2);
    }

    @Test
    void canGetRedDiploma() {
        var gb = new GradeBook(1)
                .setGrade(1, "1", 5)
                .setGrade(1, "2", 5);
        assertFalse(gb.canGetRedDiploma());
        gb.setQualificationMark(4);
        assertFalse(gb.canGetRedDiploma());
        gb.setQualificationMark(5);
        assertTrue(gb.canGetRedDiploma());
        gb.setGrade(1, "1", 4);
        assertFalse(gb.canGetRedDiploma());
    }

    @Test
    void hasBigScholarship() {
        var gb = new GradeBook(2)
                .setGrade(1, "1", 5)
                .setGrade(2, "1", 4);
        assertFalse(gb.hasBigScholarship(2));
        assertTrue(gb.hasBigScholarship(1));
    }

    @Test
    void averageGrade() {
        var gb = new GradeBook(2)
                .setGrade(1, "1", 5)
                .setGrade(1, "2", 3)
                .setGrade(2, "1", 4);
        assertEquals(4, gb.averageGrade(), 0.1);
    }

    @Test
    void validateSemester() {
        var gb = new GradeBook(2);
        assertThrows(IllegalArgumentException.class, () -> gb.setGrade(0, "1", 5));
        assertThrows(IllegalArgumentException.class, () -> gb.setGrade(3, "1", 5));
        assertThrows(IllegalArgumentException.class, () -> gb.hasBigScholarship(-1));
    }
}
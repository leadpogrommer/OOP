package ru.leadpogrommer.oop.gradebook;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GradeBook {
    private final List<Map<String, Integer>> grades;
    Integer qualificationWorkMark = null;


    GradeBook(int numSemesters) {
        grades = IntStream.range(0, numSemesters).boxed().map((i) -> new HashMap<String, Integer>()).collect(Collectors.toList());
    }

    double averageGrade() {
        return grades.stream().flatMap((semester) -> semester.values().stream()).mapToDouble(Double::valueOf).average().orElse(Double.NaN);

    }

    boolean hasBigScholarship(int semester) {
        return grades.get(convertSemester(semester)).values().stream().noneMatch((i) -> i != 5);
    }

    boolean canGetRedDiploma() {
        if (grades.stream().flatMap((semester) -> semester.values().stream()).anyMatch((i) -> i <= 3)) return false;
        if (averageGrade() < 4.75) return false;
        return qualificationWorkMark != null && qualificationWorkMark == 5;
    }

    GradeBook setGrade(int semester, String name, int grade) {
        grades.get(convertSemester(semester)).put(name, grade);
        return this;
    }

    GradeBook setQualificationMark(Integer m) {
        this.qualificationWorkMark = m;
        return this;
    }

    private int convertSemester(int semester) {
        if (semester < 1 || semester > grades.size()) throw new IllegalArgumentException();
        return semester - 1;
    }

}

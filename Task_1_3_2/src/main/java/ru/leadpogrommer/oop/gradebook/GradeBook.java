package ru.leadpogrommer.oop.gradebook;


import java.util.*;

public class GradeBook {
    private final Map<String, List<Integer>> subjects = new HashMap<>();
    private final int numSemesters;
    Integer qualificationWorkMark = null;

    GradeBook(int numSemesters){
        this.numSemesters = numSemesters;
    }

    float averageGrade(){
       int numGrades = 0;
       float sum = 0;
       for(var subject: subjects.values()){
           for(int i = 0; i < numSemesters; i++){
               if(subject.get(i) != null){
                   numGrades++;
                   sum += subject.get(i);
               }
           }
       }
       return sum/numGrades;
    }

    boolean hasBigScholarship(int semester){
        for(var subject: subjects.values()){
            if(subject.get(semester) != null && subject.get(semester) != 5)return false;
        }
        return true;
    }

    boolean canGetRedDiploma(){
        var lastMarkCount = 0.;
        var lastMarkExcCount = 0.;

        if(qualificationWorkMark == null || qualificationWorkMark != 5) return false;

        for(var subject: subjects.values()){
            for(var i = numSemesters -1; i >= 0; i--){
                if(subject.get(i) != null){
                    if(subject.get(i) < 4)return false;
                    lastMarkCount++;
                    if(subject.get(i) == 5)lastMarkExcCount++;
                }
            }
        }

        return !(lastMarkExcCount / lastMarkCount < 0.75);
    }

    GradeBook addSubject(String name, Integer ...grades){
        if(grades.length != numSemesters)throw new IllegalArgumentException("Grades count does not math number of semesters");
        subjects.put(name, Arrays.asList(grades));
        return this;
    }

    GradeBook setQualificationMark(Integer m){
        this.qualificationWorkMark = m;
        return this;
    }

}

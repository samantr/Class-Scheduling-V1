package com.esenyurt.entity;



public class Schedule {
    public Subject subject;
    public Teacher teacher;
    public Classroom classroom;
    public TimeSlot timeSlot;

    public int runId;
    public int generationNo;
    public int fitness;

    public int duration;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Schedule(Subject subject, Teacher teacher, Classroom classroom, TimeSlot timeSlot, int runId, int generationNo, int fitness) {
        this.subject = subject;
        this.teacher = teacher;
        this.classroom = classroom;
        this.timeSlot = timeSlot;
        this.runId = runId;
        this.generationNo = generationNo;
        this.fitness = fitness;
    }

    public Schedule() {
    }

    public Schedule(Subject subject, Teacher teacher, Classroom classroom, TimeSlot timeSlot) {
        this.subject = subject;
        this.teacher = teacher;
        this.classroom = classroom;
        this.timeSlot = timeSlot;
    }

    public Schedule(Subject subject, Teacher teacher, Classroom classroom, TimeSlot timeSlot, int duration) {
        this.subject = subject;
        this.teacher = teacher;
        this.classroom = classroom;
        this.timeSlot = timeSlot;
        this.duration = duration;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public int getRunId() {
        return runId;
    }

    public void setRunId(int runId) {
        this.runId = runId;
    }

    public int getGenerationNo() {
        return generationNo;
    }

    public void setGenerationNo(int generationNo) {
        this.generationNo = generationNo;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }
}
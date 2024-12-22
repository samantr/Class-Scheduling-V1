package com.esenyurt.entity;

import com.esenyurt.enums.Hours;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TimeSlot {

    public DayOfWeek day;
    public Hours time;

    public static List<TimeSlot> generateSampleTimeSlots()
    {
        List<TimeSlot> timeSlotList = new ArrayList<>();
        TimeSlot timeSlot = new TimeSlot();
        DayOfWeek[] dayOfWeeks =  DayOfWeek.values();
        Hours[] hours = Hours.values();
        for (DayOfWeek day:dayOfWeeks) {
            for (Hours hour: hours) {
                timeSlot = new TimeSlot();
                timeSlot.day = day;
                timeSlot.time = hour;
                timeSlotList.add(timeSlot);
            }
        }
        return timeSlotList;
    }

    public TimeSlot() {
    }

    public static TimeSlot getRandomTimeSlot()
    {
        Random random = new Random();
        TimeSlot timeSlot = new TimeSlot(DayOfWeek.of(random.nextInt(1,5)),Hours.values()[random.nextInt(1,9)]);

        return timeSlot;
    }
    public TimeSlot(DayOfWeek day, Hours time) {
        this.day = day;
        this.time = time;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public Hours getTime() {
        return time;
    }

    public void setTime(Hours time) {
        this.time = time;
    }
}

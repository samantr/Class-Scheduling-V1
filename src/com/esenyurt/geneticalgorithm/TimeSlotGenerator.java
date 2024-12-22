package com.esenyurt.geneticalgorithm;

import com.esenyurt.entity.*;
import com.esenyurt.enums.Hours;

import java.time.DayOfWeek;
import java.util.*;

public class TimeSlotGenerator {

    // Generate a pool of suitable time slots for scheduling
    public static List<TimeSlot> generateValidTimeSlots(
            List<Teacher> teachers,
            List<Classroom> classrooms,
            int maxDurationPerClass,
            List<Schedule> chromosome
    ) {
        List<TimeSlot> validTimeSlots = new ArrayList<>();

        // Define working hours and days
        int startHour = 9; // Classes start no earlier than 09:00
        int endHour = 18;  // Classes end no later than 18:00
        int workingDays = 5; // Monday to Friday

        // Iterate through working days
        for (int day = 1; day <= workingDays; day++) {
            // Iterate through each hour in the valid range
            for (int hour = startHour; hour < endHour; hour++) {
                // Ensure the class can fit within the working day
                if (hour + maxDurationPerClass > endHour) {
                    break;
                }

                // Iterate through classrooms
                for (Classroom classroom : classrooms) {
                    // Iterate through teachers
                    for (Teacher teacher : teachers) {
                        // Ensure teacher and classroom are available at this time
                        if (isTeacherAndClassroomAvailable(teacher, classroom, day, hour, maxDurationPerClass,chromosome)) {
                            // Create a valid time slot
                            TimeSlot timeSlot = new TimeSlot(DayOfWeek.of(day), Hours.values()[hour - startHour]);
                            validTimeSlots.add(timeSlot);
                        }
                    }
                }
            }
        }

        return validTimeSlots;
    }

    // Check if a teacher and classroom are available at the given day and time
    private static boolean isTeacherAndClassroomAvailable(
            Teacher teacher,
            Classroom classroom,
            int day,
            int startHour,
            int duration,
            List<Schedule> chromosome
    ) {
        // Check that teacher has no overlapping classes
        if (teacherHasConflict(teacher, day, startHour, duration, chromosome)) {
            return false;
        }

        // Check that classroom has no overlapping classes
        if (classroomHasConflict(classroom, day, startHour, duration, chromosome)) {
            return false;
        }

        return true; // Both are available
    }

    // Check teacher's schedule for conflicts
    private static boolean teacherHasConflict(Teacher teacher, int day, int startHour, int duration, List<Schedule> chromosome) {
        List <TimeSlot> timeSlotList = new ArrayList<>();
        for (Schedule schedule: chromosome) {
            if(schedule.getTeacher().id == teacher.getId())
                timeSlotList.add(schedule.getTimeSlot());
        }

        for (TimeSlot timeSlot: timeSlotList) {
            if(timeSlot.day.getValue() == day)
            {
                int existingStart = timeSlot.getTime().value;
                int existingEnd = existingStart + duration;
                int newEnd = startHour + duration;

                // Check for overlap
                if (startHour < existingEnd && newEnd > existingStart) {
                    return true; // Conflict detected
                }
            }
        }

        return false; // No conflict
    }

    // Check classroom's schedule for conflicts
    private static boolean classroomHasConflict(Classroom classroom, int day, int startHour, int duration, List<Schedule> chromosome) {

        List <TimeSlot> timeSlotList = new ArrayList<>();
        for (Schedule schedule: chromosome) {
            if(schedule.getClassroom().id == classroom.getId())
                timeSlotList.add(schedule.getTimeSlot());
        }

        for (TimeSlot timeSlot: timeSlotList) {
            if(timeSlot.day.getValue() == day)
            {
                int existingStart = timeSlot.getTime().value;
                int existingEnd = existingStart + duration;
                int newEnd = startHour + duration;

                // Check for overlap
                if (startHour < existingEnd && newEnd > existingStart) {
                    return true; // Conflict detected
                }
            }
        }

        return false; // No conflict
    }

    public static List<TimeSlot> teacherTimeSlots(Teacher teacher, List<Schedule> chromosome)
    {
        List <TimeSlot> timeSlotList = new ArrayList<>();
        for (Schedule schedule: chromosome) {
            if(schedule.getTeacher().id == teacher.getId())
                timeSlotList.add(schedule.getTimeSlot());
        }
        return timeSlotList;
    }

    public static List<TimeSlot> classroomTimeSlots(Classroom classroom, List<Schedule> chromosome)
    {
        List <TimeSlot> timeSlotList = new ArrayList<>();
        for (Schedule schedule: chromosome) {
            if(schedule.getClassroom().id == classroom.getId())
                timeSlotList.add(schedule.getTimeSlot());
        }
        return timeSlotList;
    }

    public static List<TimeSlot> freeTimeSlots(List<Schedule> chromosome)
    {
        List <TimeSlot> timeSlotList = new ArrayList<>();
        List <TimeSlot> allTimeSlots = new ArrayList<>();
        allTimeSlots = TimeSlot.generateSampleTimeSlots();
        for (Schedule schedule: chromosome) {
            if(!allTimeSlots.contains(schedule.getTimeSlot()))
                timeSlotList.add(schedule.getTimeSlot());
        }
        return timeSlotList;
    }
}

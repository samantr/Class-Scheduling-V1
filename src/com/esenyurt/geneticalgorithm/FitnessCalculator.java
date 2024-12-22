package com.esenyurt.geneticalgorithm;

import com.esenyurt.entity.Schedule;

import java.util.*;

public class FitnessCalculator {

    private static final int PRIORITY_HIGH = 15;
    private static final int PRIORITY_MEDIUM = 10;
    private static final int PRIORITY_LOW = 1;

    // Evaluate fitness for a single Schedule (gene)
    public static int calculateGeneFitness(Schedule schedule) {
        int fitness = 0;

        // Constraint 4: Teaching session duration matches subject's unit count
        if (schedule.getDuration() == schedule.getSubject().getUnits()) {
            //fitness += PRIORITY_HIGH; // Reward
        } else {
            fitness -= PRIORITY_HIGH; // Penalty
        }

        // Constraint 6: Class cannot end after 18:00
        int endTime = schedule.getTimeSlot().getTime().value + schedule.getDuration();
        if (endTime > 18) {
            fitness -= PRIORITY_MEDIUM; // Heavy penalty
        }

        if (!schedule.getClassroom().isLab && schedule.getSubject().isPractical)
            fitness -= PRIORITY_MEDIUM; // Heavy penalty

        return fitness;
    }

    // Evaluate fitness for the full chromosome (List<Schedule>)
    public static int calculateChromosomeFitness(List<Schedule> chromosome) {
        int fitness = 0;

        Set<String> teacherSchedule = new HashSet<>();
        Set<String> classroomSchedule = new HashSet<>();
        Map<Integer, Set<Integer>> teacherWorkingDays = new HashMap<>(); // <TeacherId, Days>

        for (Schedule gene : chromosome) {
            // Gene-level fitness
            fitness += calculateGeneFitness(gene);

            // Constraint 1: Classes must be on working days (1-5)
            int dayOfWeek = gene.getTimeSlot().getDay().getValue(); // Assuming Mon=1, Fri=5
            if (dayOfWeek < 1 || dayOfWeek > 5) {
                fitness -= PRIORITY_MEDIUM;
            }

            // Constraint 2: Each subject can only be taught by one teacher
            String subjectTeacherKey = gene.getSubject().getId() + "-" + gene.getTeacher().getId();
            if (!teacherSchedule.add(subjectTeacherKey)) {
                fitness -= PRIORITY_HIGH; // Penalty for multiple teachers for one subject
            }

            // Constraint 3: Classes must start between 09:00 and 18:00
            int startTime = gene.getTimeSlot().getTime().value;
            if (startTime < 9 || startTime >= 18) {
                fitness -= PRIORITY_MEDIUM;
            }

            // Constraint 5: One teacher cannot be in two classrooms at overlapping times
            for (Schedule otherGene : chromosome) {
                if (gene != otherGene && gene.getTeacher().getId() == otherGene.getTeacher().getId()) {
                    if (isOverlap(gene, otherGene)) {
                        fitness -= PRIORITY_HIGH; // Heavy penalty for overlapping classes
                    }
                }
            }

            // Constraint X: class overLap
            for (Schedule otherGene : chromosome) {
                if (gene != otherGene && gene.getClassroom().getId() == otherGene.getClassroom().getId()) {
                    if (isOverlap(gene, otherGene)) {
                        fitness -= PRIORITY_HIGH; // Heavy penalty for overlapping classes
                    }
                }
            }

            // Constraint 7: No overlapping classes in the same classroom
            String classroomKey = gene.getClassroom().getId() + "-" + gene.getTimeSlot().getDay() + "-" + gene.getTimeSlot().getTime();
            if (!classroomSchedule.add(classroomKey)) {
                fitness -= PRIORITY_HIGH; // Heavy penalty for overlapping classes in the same room
            }

            // Track teacher working days
            teacherWorkingDays.computeIfAbsent(gene.getTeacher().getId(), k -> new HashSet<>()).add(dayOfWeek);
        }

        // Constraint 8: Minimize the number of working days for each teacher
        for (Set<Integer> days : teacherWorkingDays.values()) {
            fitness -= PRIORITY_LOW * (days.size() - 3); // Reward fewer working days
        }

        return fitness;
    }

    // Check if two schedules overlap
    private static boolean isOverlap(Schedule s1, Schedule s2) {
        int s1Start = s1.getTimeSlot().getTime().value;
        int s1End = s1Start + s1.getDuration();
        int s2Start = s2.getTimeSlot().getTime().value;
        int s2End = s2Start + s2.getDuration();

        return s1.getTimeSlot().getDay() == s2.getTimeSlot().getDay() && // Same day
                (s1Start < s2End && s2Start < s1End); // Time overlap
    }
}

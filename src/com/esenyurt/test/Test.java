package com.esenyurt.test;

import com.esenyurt.database.MSSQLDatabaseConnector;
import com.esenyurt.entity.Schedule;
import com.esenyurt.enums.Hours;
import com.esenyurt.geneticalgorithm.FitnessCalculator;

import java.util.List;

public class Test {

    public static void main(String[] args) {

        List<Schedule> scheduleList = MSSQLDatabaseConnector.fetchSchedules(200,1);
        int fitness = FitnessCalculator.calculateChromosomeFitness(scheduleList);
        System.out.println(fitness);



    }
}

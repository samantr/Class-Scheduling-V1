package com.esenyurt.geneticalgorithm;

import com.esenyurt.database.MSSQLDatabaseConnector;
import com.esenyurt.entity.*;

import java.sql.SQLException;
import java.util.List;

public class GeneticAlgorithmExecutor {
    public static void main(String[] args) {
        // Initialize parameters
        int populationSize = 1000;
        int maxGenerations = 500;
        double mutationRate = 0.05;
        int maxStagnation = 500;
        double crossoverRate = 0.9;

        String desc = "Improved Algorithm";


        schedule(50,mutationRate, maxGenerations, maxStagnation, crossoverRate, desc );
        schedule(100,mutationRate, maxGenerations, maxStagnation,crossoverRate,desc );
        schedule(200,mutationRate, maxGenerations, maxStagnation,crossoverRate,desc );
        schedule(500,mutationRate, maxGenerations, maxStagnation,crossoverRate,desc );
        schedule(1000,mutationRate, maxGenerations, maxStagnation,crossoverRate,desc );

    }


    public static void schedule(int populationSize, double mutationRate, int maxGenerations, int maxStagnation, double crossoverRate, String desc)
    {
        // Fetch data from the database
        List<Teacher> teachers = MSSQLDatabaseConnector.fetchPersons();
        List<Classroom> classrooms = MSSQLDatabaseConnector.fetchClassRooms();
        List<Subject> subjects = MSSQLDatabaseConnector.fetchSubject();
        List<TimeSlot> timeSlots = TimeSlot.generateSampleTimeSlots(); // Mock data for now



        // Create initial population
        Population population = new Population(populationSize, subjects, teachers, classrooms, timeSlots);

        // Insert a new run into the database and retrieve its ID
        RunEntity run = new RunEntity();
        run.populationSize = populationSize;
        run.generationCount = maxGenerations;
        long runId = MSSQLDatabaseConnector.insertRun(run,desc);
        int bestFitness = Integer.MIN_VALUE;
        int stagnationCount = 0;
        int currentGeneration = 1;
        // Evolve the population over multiple generations
        for (int generation = 1; generation <= maxGenerations; generation++) {
            currentGeneration = generation;
            // Get the fittest chromosome of this generation
            List<Schedule> fittestChromosome = population.getFittestChromosome();
            int fitness = FitnessCalculator.calculateChromosomeFitness(fittestChromosome);


            // Generate the next generation
            population = population.evolve(crossoverRate, mutationRate, subjects, teachers, classrooms, timeSlots, stagnationCount);
            if(fitness > bestFitness)
            {
                bestFitness = fitness;
                stagnationCount = 0;
            }else
                stagnationCount++;

            System.out.printf("Generation %d: Best Fitness = %d%n", generation, fitness);

            try {
                MSSQLDatabaseConnector.insertSchedulesBatch(population.getFittestChromosome(), (int) runId, currentGeneration, fitness, 1);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if (stagnationCount > maxStagnation )
            {
                System.out.println("Exit due to stagnation ");
                break;
            }


        }

        // Save the final result
        List<Schedule> finalFittest = population.getFittestChromosome();
        int finalFitness = FitnessCalculator.calculateChromosomeFitness(finalFittest);
        System.out.printf("Final Best Fitness: %d%n", finalFitness);
        try {
            MSSQLDatabaseConnector.insertSchedules(finalFittest, (int) runId, currentGeneration, finalFitness, 1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

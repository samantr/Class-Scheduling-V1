package com.esenyurt.entity;

public class RunEntity {

    public int id;
    public int populationSize;
    public int generationCount;

    public RunEntity(int id, int populationSize, int generationCount) {
        this.id = id;
        this.populationSize = populationSize;
        this.generationCount = generationCount;
    }

    public RunEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public int getGenerationCount() {
        return generationCount;
    }

    public void setGenerationCount(int generationCount) {
        this.generationCount = generationCount;
    }
}

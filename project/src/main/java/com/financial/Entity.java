package com.financial;

import java.util.ArrayList;

public class Entity {
    String entityCode;
    String name;
    protected static ArrayList<Entity> entities = new ArrayList<>();

    public Entity (String entityCode, String name){
        this.entityCode = entityCode;
        this.name = name;
        entities.add(this);
    }

    public static void printAllEntities(){
        for (Entity entity : entities) {
            System.out.println(entity);
        }
    }

    public static Entity findEntityWithEntityCode(String entityCode){
        for (Entity entity : entities) {
            if (entity.getEntityCode().equals(entityCode)){
                return entity;
            }
        }
        return null;
    }

    public static void printEntitiesWithTheirTotalRegularExpenses(){
        for (Entity entity: entities) {
            long totalRegularExpenses = RegularBudgetExpense.getRegularSumOfEntityWithEntityCode(entity.entityCode);
            System.out.println(entity + ", Total Regular Expenses: " + String.format("%,d", totalRegularExpenses));
        }
    }

    public String getEntityCode() {
        return entityCode;
    }

    public String getName(){
        return name;
    }


    @Override
    public String toString(){
        return "Entity Code: " + entityCode + ", Name: " + name;
    }
}

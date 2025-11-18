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
}

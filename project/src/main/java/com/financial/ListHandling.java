package com.financial;

import java.util.ArrayList;

public class ListHandling {
    public static void printList (ArrayList<?> arrayList){
        for (Object object : arrayList){
            System.out.println(object);
        }
    }
}


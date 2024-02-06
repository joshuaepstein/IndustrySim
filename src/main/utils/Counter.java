package main.utils;
/*
 * Copyright (c) 2023. Joshua Epstein
 * All rights reserved.
 */

 public class Counter {
     private int value;
 
     public Counter() {
         this(0);
     }
 
     public Counter(int value) {
         this.value = value;
     }
 
     public int getValue() {
         return this.value;
     }
 
     public void decrement() {
         --this.value;
     }
 
     public void increment() {
         ++this.value;
     }

     public void setValue(int value) {
         this.value = value;
     }

     public void add(int value) {
         this.value += value;
     }

    public void subtract(int value) {
        this.value -= value;
    }

    public void multiply(int value) {
        this.value *= value;
    }

    public void divide(int value) {
        this.value /= value;
    }
 }
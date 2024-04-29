package com.utilities.todolist.enums;

import java.time.LocalDate;

public enum TaskType {

    DATE, DEADLINE, FREE;

    public boolean containsDaysLate(){
        return !this.equals(FREE);
    }


}

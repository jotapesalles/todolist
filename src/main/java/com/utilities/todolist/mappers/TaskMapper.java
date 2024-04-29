package com.utilities.todolist.mappers;

import com.utilities.todolist.models.Task;
import com.utilities.todolist.vos.TaskRequest;
import java.time.LocalDate;

public final class TaskMapper {

  public Task map(TaskRequest source) {
    var task = new Task();
    task.setName(source.getName());
    task.setCompleted(source.getCompleted());
    task.setType(source.getType());
    task.setPriority(source.getPriority());
    task.setFinalDate(source.getFinalDate());
    task.setInitialDate(LocalDate.now());
    return task;
  }
}

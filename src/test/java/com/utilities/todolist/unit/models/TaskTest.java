package com.utilities.todolist.unit.models;

import static org.junit.Assert.assertEquals;

import com.utilities.todolist.enums.Priority;
import com.utilities.todolist.enums.Status;
import com.utilities.todolist.enums.TaskType;
import com.utilities.todolist.models.Task;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class TaskTest {

  @Test
  void toStringWithSuccess() {
    Long id = 1L;
    String name = "Buy groceries";
    boolean completed = false;
    TaskType type = TaskType.DEADLINE;
    Priority priority = Priority.HIGH;
    LocalDate initialDate = LocalDate.now();
    LocalDate finalDate = LocalDate.now().plusDays(2);
    Status status = Status.CONCLUDED;
    Integer daysLate = 2;

    Task task = new Task();
    task.setId(id);
    task.setName(name);
    task.setCompleted(completed);
    task.setType(type);
    task.setPriority(priority);
    task.setInitialDate(initialDate);
    task.setFinalDate(finalDate);
    task.setStatus(status);
    task.setDaysLate(daysLate);

    String expectedString =
        "Task{id=1, name='Buy groceries', completed=false, type=DEADLINE, priority=HIGH, initialDate=2024-05-30, finalDate=2024-06-01, status=CONCLUDED, daysLate=2}";

    assertEquals(expectedString, task.toString());
  }
}

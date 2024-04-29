package com.utilities.todolist.models;

import com.utilities.todolist.enums.Priority;
import com.utilities.todolist.enums.Status;
import com.utilities.todolist.enums.TaskType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private Boolean completed;

  private TaskType type;

  private Priority priority;

  private LocalDate initialDate;

  private LocalDate finalDate;

  private Status status;

  private Integer daysLate;

  @Override
  public String toString() {
    return "Task{" +
            "priority=" + priority +
            ", type=" + type +
            ", completed=" + completed +
            ", name='" + name + '\'' +
            ", id=" + id +
            '}';
  }
}

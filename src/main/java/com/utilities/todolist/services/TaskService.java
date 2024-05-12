package com.utilities.todolist.services;

import com.utilities.todolist.enums.Status;
import com.utilities.todolist.models.Task;
import com.utilities.todolist.repository.TaskRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
  @Autowired TaskRepository taskRepository;

  public ResponseEntity<List<Task>> getAll() {
    try {
      List<Task> taskList = new ArrayList<>(taskRepository.findAll());
      if (taskList.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(taskList, HttpStatus.OK);
    } catch (Exception ex) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public ResponseEntity<Task> create(Task task) {
    try {
      return new ResponseEntity<>(taskRepository.save(task), HttpStatus.CREATED);
    } catch (Exception ex) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public ResponseEntity<Task> update(Long id, Task task) {
    try {
      Optional<Task> taskToUpdateOpt = taskRepository.findById(id);
      if (taskToUpdateOpt.isPresent()) {
        Task taskToUpdate = taskToUpdateOpt.get();
        taskToUpdate.setName(task.getName());
        return ResponseEntity.ok(taskRepository.save(taskToUpdate));
      } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    } catch (Exception ex) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public ResponseEntity<Task> complete(Long id) {
    try {
      Optional<Task> taskToUpdateOpt = taskRepository.findById(id);
      if (taskToUpdateOpt.isPresent()) {
        Task task = taskToUpdateOpt.get();
        task.setCompleted(true);
        if (task.getType().containsDaysLate()) {
          LocalDate today = LocalDate.now();
          if (today.isAfter(task.getFinalDate())) {
            task.setDaysLate(today.until(task.getFinalDate()).getDays());
            task.setStatus(Status.DAYS_LATE);
          } else if (today.isEqual(task.getFinalDate())) {
            task.setStatus(Status.EXPECTED);
          } else task.setStatus(Status.CONCLUDED);
        }
        return ResponseEntity.ok(taskRepository.save(task));
      } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    } catch (Exception ex) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public ResponseEntity<Task> undo(Long id) {
    try {
      Optional<Task> taskToUpdateOpt = taskRepository.findById(id);
      if (taskToUpdateOpt.isPresent()) {
        Task taskToUpdate = taskToUpdateOpt.get();
        taskToUpdate.setCompleted(false);
        return ResponseEntity.ok(taskRepository.save(taskToUpdate));
      } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    } catch (Exception ex) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public ResponseEntity<Void> delete(Long id) {
    try {
      taskRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    } catch (Exception ex) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}

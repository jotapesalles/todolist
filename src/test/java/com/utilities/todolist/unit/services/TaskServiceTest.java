package com.utilities.todolist.unit.services;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.utilities.todolist.enums.Priority;
import com.utilities.todolist.enums.Status;
import com.utilities.todolist.enums.TaskType;
import com.utilities.todolist.models.Task;
import com.utilities.todolist.repository.TaskRepository;
import com.utilities.todolist.services.TaskService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.client.HttpServerErrorException;
import org.webjars.NotFoundException;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class TaskServiceTest {
  @Mock Task task;

  @Mock TaskRepository repository;

  @InjectMocks TaskService service;

  @Test
  public void getAllWhenTasksExistShouldReturnTasksList() {
    List<Task> tasks = new ArrayList<>();
    tasks.add(task);

    when(repository.findAll()).thenReturn(tasks);

    ResponseEntity<List<Task>> response = service.getAll();
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(tasks, response.getBody());
  }

  @Test
  public void getAllWhenNoTasksExistShouldReturnNoContent() {
    when(repository.findAll()).thenReturn(emptyList());

    ResponseEntity<List<Task>> response = service.getAll();
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }

  @Test
  public void getAllWhenRepositoryThrowsExceptionShouldReturnInternalServerError() {
    when(repository.findAll()).thenThrow(HttpServerErrorException.InternalServerError.class);

    ResponseEntity<List<Task>> response = service.getAll();

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  public void createWithValidTaskShouldReturnCreatedTask() {
    when(repository.save(task)).thenReturn(task);

    ResponseEntity<Task> response = service.create(task);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(task, response.getBody());
  }

  @Test
  public void createWhenRepositoryThrowsExceptionShouldReturnInternalServerError() {
    when(repository.save(task)).thenThrow(JpaSystemException.class);

    ResponseEntity<Task> response = service.create(task);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  public void updateWithExistingIdAndValidTaskShouldReturnUpdatedTask() {
    Long taskId = 1L;
    Task existingTask = new Task();
    existingTask.setId(taskId);

    Task updatedTask = new Task();

    when(repository.findById(taskId)).thenReturn(Optional.of(existingTask));
    when(repository.save(existingTask)).thenReturn(existingTask);

    ResponseEntity<Task> response = service.update(taskId, updatedTask);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(existingTask, response.getBody());
  }

  @Test
  public void updateWithNotFoundIdShouldReturnNotFound() {
    Long taskId = 1L;
    Task updatedTask = new Task();

    when(repository.findById(taskId)).thenReturn(Optional.empty());

    ResponseEntity<Task> response = service.update(taskId, updatedTask);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void updateWhenRepositoryThrowsExceptionShouldReturnInternalServerError() {
    Long taskId = 1L;
    Task updatedTask = new Task();

    when(repository.findById(taskId)).thenThrow(HttpServerErrorException.InternalServerError.class);

    ResponseEntity<Task> response = service.update(taskId, updatedTask);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  public void completeWithNonexistentTaskIdShouldReturnNotFound() {
    Long taskId = 1L;

    when(repository.findById(taskId)).thenReturn(Optional.empty());

    ResponseEntity<Task> response = service.complete(taskId);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void undoWithValidTaskIdShouldUndoTaskAndUpdateStatus() {
    Long taskId = 1L;
    Task task = new Task();
    task.setId(taskId);
    task.setName("Buy groceries");
    task.setFinalDate(LocalDate.now().plusDays(2));
    task.setStatus(Status.CONCLUDED);

    when(repository.findById(taskId)).thenReturn(Optional.of(task));
    when(repository.save(task)).thenReturn(task);

    ResponseEntity<Task> response = service.undo(taskId);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertFalse(response.getBody().getCompleted());
  }

  @Test
  public void undoWithRepositoryExceptionShouldReturnInternalServerError() {
    Long taskId = 1L;

    when(repository.findById(taskId)).thenThrow(HttpServerErrorException.InternalServerError.class);

    ResponseEntity<Task> response = service.undo(taskId);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  public void undoWithNonexistentTaskIdShouldReturnNotFound() {
    Long taskId = 1L;

    when(repository.findById(taskId)).thenReturn(Optional.empty());

    ResponseEntity<Task> response = service.undo(taskId);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void deleteWithValidTaskIdShouldDeleteTaskAndReturnNoContent() {
    Long taskId = 1L;

    doNothing().when(repository).deleteById(taskId);

    ResponseEntity<Void> response = service.delete(taskId);
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }

  @Test
  public void deleteWithNonexistentTaskIdShouldReturnNotFound() {
    Long taskId = 1L;
    doThrow(NotFoundException.class).when(repository).deleteById(taskId);

    ResponseEntity<Void> response = service.delete(taskId);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  public void completeWithValidTaskIdShouldCompleteTaskAndUpdateStatus() {
    Long taskId = 1L;
    LocalDate finalDate = LocalDate.now().plusDays(2);

    Task task = new Task();
    task.setId(taskId);
    task.setName("Buy groceries");
    task.setType(TaskType.DEADLINE);
    task.setPriority(Priority.HIGH);
    task.setInitialDate(LocalDate.now());
    task.setFinalDate(finalDate);

    when(repository.findById(taskId)).thenReturn(Optional.of(task));
    when(repository.save(task)).thenReturn(task);

    ResponseEntity<Task> response = service.complete(taskId);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(true, response.getBody().getCompleted());
    assertEquals(Status.CONCLUDED, response.getBody().getStatus());
  }

  @Test
  public void completeWithValidTaskIdAndOverdueTaskShouldUpdateStatusAndDaysLate() {
    Long taskId = 1L;
    LocalDate finalDate = LocalDate.now().minusDays(1);

    Task task = new Task();
    task.setId(taskId);
    task.setName("Clean the house");
    task.setType(TaskType.DEADLINE);
    task.setPriority(Priority.MEDIUM);
    task.setStatus(Status.EXPECTED);
    task.setInitialDate(LocalDate.now().minusDays(2));
    task.setFinalDate(finalDate);
    task.setCompleted(false);

    when(repository.findById(taskId)).thenReturn(Optional.of(task));
    when(repository.save(task)).thenReturn(task);

    ResponseEntity<Task> response = service.complete(taskId);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(true, response.getBody().getCompleted());
    assertEquals(Status.DAYS_LATE, response.getBody().getStatus());
  }

  @Test
  public void completeWithValidTaskIdAndOnDueDateShouldUpdateStatusToExpected() {
    Long taskId = 1L;
    LocalDate finalDate = LocalDate.now();

    Task task = new Task();
    task.setId(taskId);
    task.setName("Meeting");
    task.setType(TaskType.DEADLINE);
    task.setPriority(Priority.LOW);
    task.setInitialDate(LocalDate.now().minusDays(7));
    task.setFinalDate(finalDate);
    task.setCompleted(false);

    when(repository.findById(taskId)).thenReturn(Optional.of(task));

    when(repository.save(task)).thenReturn(task);

    ResponseEntity<Task> response = service.complete(taskId);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().getCompleted());
    assertEquals(Status.EXPECTED, response.getBody().getStatus());
  }

  @Test
  public void completeWhenRepositoryThrowsExceptionShouldReturnInternalServerError() {
    Long taskId = 1L;

    when(repository.findById(taskId)).thenThrow(HttpServerErrorException.InternalServerError.class);

    ResponseEntity<Task> response = service.complete(taskId);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }
}

package com.utilities.todolist.controllers;

import com.utilities.todolist.mappers.TaskMapper;
import com.utilities.todolist.models.Task;
import com.utilities.todolist.services.TaskService;
import com.utilities.todolist.vos.TaskRequest;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {

  @Autowired private TaskService service;

  private final TaskMapper mapper = new TaskMapper();

  @GetMapping
  @Operation(summary = "Lista todas as tarefas da lista")
  public ResponseEntity<List<Task>> getAll() {
    return service.getAll();
  }

  @PostMapping
  @Operation(summary = "Cria uma nova tarefa")
  public ResponseEntity<Task> create(@RequestBody TaskRequest task) {
    return service.create(mapper.map(task));
  }

  @PutMapping(value = "/{id}")
  @Operation(summary = "Edita uma tarefa")
  public ResponseEntity<Task> update(@PathVariable Long id, @RequestBody Task task) {
    return service.update(id, task);
  }

  @PutMapping(value = "/{id}/complete")
  @Operation(summary = "Completa uma tarefa")
  public ResponseEntity<Task> complete(@PathVariable Long id) {
    return service.complete(id);
  }

  @PutMapping(value = "/{id}/undo")
  @Operation(summary = "Descompletar uma tarefa")
  public ResponseEntity<Task> undo(@PathVariable Long id) {
    return service.undo(id);
  }

  @DeleteMapping(value = "/{id}")
  @Operation(summary = "Apagar uma tarefa")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    return service.delete(id);
  }
}

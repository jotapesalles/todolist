package com.utilities.todolist.unit.cotrollers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.utilities.todolist.controllers.TaskController;
import com.utilities.todolist.mappers.TaskMapper;
import com.utilities.todolist.models.Task;
import com.utilities.todolist.services.TaskService;
import com.utilities.todolist.vos.TaskRequest;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class TaskControllerTest {

    private final TaskMapper mapper = new TaskMapper();

    @Mock
    TaskService service;

    @InjectMocks
    private TaskController controller;

    @Test
    public void getAllShouldReturnTasks(){
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task());

        when(service.getAll()).thenReturn(ResponseEntity.ok(tasks));

        ResponseEntity<List<Task>> response = controller.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tasks, response.getBody());
    }

    @Test
    public void createShouldCreateTask() {
        TaskRequest request = new TaskRequest();

        Task expectedTask = new Task();

        when(service.create(any(Task.class))).thenReturn(ResponseEntity.ok(expectedTask));

        ResponseEntity<Task> response = controller.create(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedTask, response.getBody());
    }

    @Test
    public void updateShouldUpdateTask() {
        Long id = 1L;
        Task updateTask = new Task();
        when(service.update(id, updateTask)).thenReturn(ResponseEntity.ok(updateTask));

        ResponseEntity<Task> response = controller.update(id, updateTask);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updateTask, response.getBody());
    }

    @Test
    public void completeShouldCompleteTask(){
        Long id = 1L;
        Task expectedTask = new Task();

        when(service.complete(id)).thenReturn(ResponseEntity.ok(expectedTask));

        ResponseEntity<Task> response = controller.complete(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedTask, response.getBody());
    }

    @Test
    public void undoShouldUndoTask(){
        Long id = 1L;
        Task expectedTask = new Task();

        when(service.complete(id)).thenReturn(ResponseEntity.ok(expectedTask));

        ResponseEntity<Task> response = controller.complete(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedTask, response.getBody());
    }

    @Test
    public void deleteShouldDeleteTask()  {
        Long id = 1L;

        when(service.delete(id)).thenReturn(ResponseEntity.noContent().build());

        ResponseEntity<Void> response = controller.delete(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}

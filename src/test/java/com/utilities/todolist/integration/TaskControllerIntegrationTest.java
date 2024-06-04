package com.utilities.todolist.integration;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

import com.utilities.todolist.TodolistApplication;
import com.utilities.todolist.enums.Priority;
import com.utilities.todolist.enums.TaskType;
import com.utilities.todolist.models.Task;
import com.utilities.todolist.vos.TaskRequest;
import io.restassured.RestAssured;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
@SpringBootTest(
    classes = {TodolistApplication.class},
    webEnvironment = DEFINED_PORT)
@ActiveProfiles("test")
public class TaskControllerIntegrationTest {

  private static final String URL = "/api/tasks";

  @BeforeEach
  public void setup() {
    RestAssured.baseURI = "http://localhost:8080";
    RestAssured.port = 8080;
  }

  @Test
  public void postAndGetTaskWithSuccess() {

    LocalDate now = LocalDate.now();

    TaskRequest taskRequest = new TaskRequest();
    taskRequest.setName("taskRequest");
    taskRequest.setType(TaskType.DATE);
    taskRequest.setPriority(Priority.HIGH);
    taskRequest.setFinalDate(LocalDate.now());

    given().contentType(JSON).body(taskRequest).post(URL).then().statusCode(201);

    given()
        .get(URL)
        .then()
        .statusCode(200)
        .body("last().name", equalTo("taskRequest"))
        .body("last().type", equalTo("DATE"))
        .body("last().status", equalTo("EXPECTED"))
        .body("last().completed", equalTo(false))
        .body("last().priority", equalTo("HIGH"))
        .body("last().finalDate", equalTo(now.toString()))
        .body("last().daysLate", nullValue());
  }

  @Test
  public void updateTaskWithSuccess() {
    Task taskToUpdate = new Task();
    taskToUpdate.setId(1L);
    taskToUpdate.setName("Updated Task");
    taskToUpdate.setCompleted(false);

    given()
        .contentType("application/json")
        .body(taskToUpdate)
        .put(URL + "/1")
        .then()
        .statusCode(200)
        .body("id", equalTo(1))
        .body("name", equalTo("Updated Task"));
  }

  @Test
  public void completeTaskWithSuccess() {
    given()
        .put(URL + "/1/complete")
        .then()
        .statusCode(200)
        .body("id", equalTo(1))
        .body("completed", equalTo(true));
  }

  @Test
  public void undoTaskWithSuccess() {
    given()
        .put(URL + "/1/undo")
        .then()
        .statusCode(200)
        .body("id", equalTo(1))
        .body("completed", equalTo(false));
  }

  @Test
  public void deleteTaskWithSuccess() {
    TaskRequest taskRequest = new TaskRequest();
    taskRequest.setName("taskRequest");
    taskRequest.setType(TaskType.FREE);
    taskRequest.setPriority(Priority.LOW);
    taskRequest.setFinalDate(LocalDate.now());

    given().contentType(JSON).body(taskRequest).post(URL).then().statusCode(201);

    given().delete(URL + "/2").then().statusCode(204);
  }
}

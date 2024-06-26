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
  private static Integer lastTaskId;

  @BeforeEach
  public void setup() {
    RestAssured.baseURI = "http://localhost:8080";
    RestAssured.port = 8080;
    lastTaskId = getLastTaskId();
  }

  @Test
  public void postAndGetTaskWithSuccess() {

    LocalDate now = LocalDate.now();

    TaskRequest taskRequest = new TaskRequest();
    taskRequest.setName("Fazer compras");
    taskRequest.setType(TaskType.DATE);
    taskRequest.setPriority(Priority.HIGH);
    taskRequest.setFinalDate(LocalDate.now());

    int id =
        given()
            .contentType(JSON)
            .body(taskRequest)
            .post(URL)
            .then()
            .statusCode(201)
            .extract()
            .path("id");

    given()
        .get(URL)
        .then()
        .statusCode(200)
        .body("last().name", equalTo("Fazer compras"))
        .body("last().type", equalTo("DATE"))
        .body("last().status", equalTo("EXPECTED"))
        .body("last().completed", equalTo(false))
        .body("last().priority", equalTo("HIGH"))
        .body("last().finalDate", equalTo(now.toString()))
        .body("last().daysLate", nullValue());

    deleteById(id);
  }

  @Test
  public void updateTaskWithSuccess() {
    Task taskToUpdate = new Task();
    taskToUpdate.setName("Passear com o cachorro");

    given()
        .contentType("application/json")
        .body(taskToUpdate)
        .put(URL + "/" + lastTaskId)
        .then()
        .statusCode(200)
        .body("id", equalTo(lastTaskId))
        .body("name", equalTo("Passear com o cachorro"));
  }

  @Test
  public void completeTaskWithSuccess() {
    given()
        .put(URL + "/" + lastTaskId + "/complete")
        .then()
        .statusCode(200)
        .body("id", equalTo(lastTaskId))
        .body("completed", equalTo(true));
  }

  @Test
  public void undoTaskWithSuccess() {
    given()
        .put(URL + "/" + lastTaskId + "/undo")
        .then()
        .statusCode(200)
        .body("id", equalTo(lastTaskId))
        .body("completed", equalTo(false));
  }

  @Test
  public void deleteTaskWithSuccess() {
    TaskRequest taskRequest = new TaskRequest();
    taskRequest.setName("Dobrar roupa");
    taskRequest.setType(TaskType.FREE);
    taskRequest.setPriority(Priority.LOW);
    taskRequest.setFinalDate(LocalDate.now());

    int id =
        given()
            .contentType(JSON)
            .body(taskRequest)
            .post(URL)
            .then()
            .statusCode(201)
            .extract()
            .path("id");

    given().delete(URL + "/" + id).then().statusCode(204);
  }

  private int getLastTaskId() {
    Task[] allTasks = given().get(URL).then().statusCode(200).extract().body().as(Task[].class);

    return allTasks[allTasks.length - 1].getId().intValue();
  }

  private void deleteById(int id) {

    given().delete(URL + "/" + id).then().statusCode(204);
  }
}

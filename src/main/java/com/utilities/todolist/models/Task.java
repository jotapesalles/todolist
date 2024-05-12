package com.utilities.todolist.models;

import com.utilities.todolist.enums.Priority;
import com.utilities.todolist.enums.Status;
import com.utilities.todolist.enums.TaskType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Schema(description = "Todos os detalhes sobre uma tarefa. ")
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Schema(name = "Nome da tarefa deve possuir pelo menos 10 caracteres")
  @Size(min = 10, message = "Nome da tarefa deve possuir pelo menos 10 caracteres")
  private String name;

  @Schema(name = "Verdadeiro, caso a tarefa tenha sido completada")
  private Boolean completed;

  @NotNull
  @Schema(name = "Tipo da tarefa: data, prazo ou livre")
  private TaskType type;

  @NotNull
  @Schema(name = "Prioridade da tarefa")
  private Priority priority;

  @Schema(name = "Data inicial da tarefa")
  private LocalDate initialDate;

  @Schema(name = "Caso tipo seja data ou prazo, data final da tarefa")
  private LocalDate finalDate;

  @Schema(name = "Status da tarefa: em andamento, atrasada e concluida")
  private Status status;

  @Schema(name = "Caso tipo seja data ou prazo, dias atrasados")
  private Integer daysLate;

  @Override
  public String toString() {
    return "Task{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", completed=" + completed +
            ", type=" + type +
            ", priority=" + priority +
            ", initialDate=" + initialDate +
            ", finalDate=" + finalDate +
            ", status=" + status +
            ", daysLate=" + daysLate +
            '}';
  }
}

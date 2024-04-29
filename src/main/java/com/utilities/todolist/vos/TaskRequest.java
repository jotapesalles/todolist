package com.utilities.todolist.vos;

import com.utilities.todolist.enums.Priority;
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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Todos os detalhes sobre uma tarefa. ")
public class TaskRequest {

    @NotBlank
    @Schema(name = "Nome da tarefa deve possuir pelo menos 10 caracteres")
    @Size(min = 10, message = "Nome da tarefa deve possuir pelo menos 10 caracteres")
    private String name;

    @NotNull
    @Schema(name = "Verdadeiro, caso a tarefa tenha sido completada")
    private Boolean completed;

    @NotNull
    @Schema(name = "Tipo da tarefa")
    private TaskType type;

    @NotNull
    @Schema(name = "Prioridade da tarefa")
    private Priority priority;

    @Schema(name = "Caso tipo seja data ou prazo, data final da tarefa")
    private LocalDate finalDate;


}

import React, {useEffect, useState} from 'react'
import {TodoList} from "./TodoList";
import {TodoForm} from "./TodoForm";
import axios from 'axios';

const url = 'https://todolist-z3oh.onrender.com/api/tasks';
const deleteTodo = (id) => {
    axios.delete(url + '/'+id)
        .then((response) => alert("Tarefa deletada com sucesso!")).finally(() => window.location.reload());
}

const editTodo = (id) => {
    let description = prompt("Digite para qual nova tarefa deseja alterar:");
    axios.put(url + '/'+id, {"name": description}).then((response) => alert("Tarefa editada com sucesso!")).finally(() => window.location.reload());
}

const addTask = (description) => {
    axios.post(url, {"name": description}).then((response) => alert("Tarefa criada com sucesso!")).finally(() => window.location.reload());
}

const completeTask = (id) => {
    axios.put(url+ '/'+id+'/complete').then((response) => {alert("Tarefa completada com sucesso!")}).finally(() => window.location.reload());
}

const undoTask = (id) => {
    axios.put(url+ '/'+id+'/undo').then((response) => {alert("Tarefa descompletada com sucesso!")}).finally(() => window.location.reload());
}
export const TodoWrapperService = () => {
    const [tasks, setTasks] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        axios.get(url)
            .then((response) => setTasks(response.data))
            .finally(() => setIsLoading(false));
    }, []);


    return (
        <div className="TodoWrapper">
            <h1>Lista de Tarefas!</h1>
            {isLoading ? <p>Carregando...</p> : null}
            {tasks.length > 0 ? (
                <>
                    <TodoForm addTodo={addTask}/>
                    {tasks.map((task) => (
                        <TodoList key={task.id} task={task}
                                  deleteTask={() => deleteTodo(task.id)}
                                  editTask={() => editTodo(task.id, task)}
                                  completeTask={() => completeTask(task.id)}
                                  undoTask={() => undoTask(task.id)}/>
                    ))}
                </>
            ) : (<p>Sua lista de tarefas está vazia!</p>)
            }
        </div>
    );
};
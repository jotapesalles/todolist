import React, {useEffect, useState} from 'react'
import {TodoList} from "./TodoList";
import {TodoForm} from "./TodoForm";
import axios from 'axios';

const url = 'https://todolist-z3oh.onrender.com/api/tasks';
export const TodoWrapperService = () => {
    const [todos, setTodos] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        axios.get(url)
            .then((response) => setTodos(response.data))
            .finally(() => setIsLoading(false));
    }, []);

    const addTodo = (task) => {
        setTodos([
            ...todos,
            task,
        ]);
    }

    return (
        <div className="TodoWrapper">
            <h1>Lista de Tarefas! (Service)</h1>
            {isLoading ? <p>Carregando...</p> : null}
            {todos.length > 0 ? (
                <>
                    <TodoForm addTodo={addTodo}/>
                    {todos.map((todo, index) => (
                        <TodoList task={todo} key={index}/>
                    ))}
                </>
            ) : (<p>Sua lista de tarefas está vazia!</p>)
            }
        </div>
    );
};
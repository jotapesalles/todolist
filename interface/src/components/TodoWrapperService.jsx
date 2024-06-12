import React, {useEffect, useState} from 'react'
import {TodoList} from "./TodoList";
import {TodoForm} from "./TodoForm";
import {v4 as uuidv4} from "uuid";

export const TodoWrapperService = () => {
    const [todos, setTodos] = useState([])
    useEffect(() => {
        const savedTodos = JSON.parse(localStorage.getItem('todos')) || [];
        setTodos(savedTodos);
    }, []);
    const addTodo = (todo) => {
        setTodos([
            ...todos,
            {id: uuidv4(), description: todo, completed: false},
        ]);
    }
    return (
        <div className='TodoWrapper'>
            <h1>Lista de Tarefas! (Service)</h1>
            <TodoForm addTodo={addTodo}/>
            {todos.map((todo, index) => (
                <TodoList task={todo} key={index}/>
            ))}
        </div>
    )
}
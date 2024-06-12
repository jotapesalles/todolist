import React, {useState} from 'react'

export const TodoForm = ({addTodo}) => {
    const [value, setValue] = useState('');
    const handleSubmit = (e) => {
        e.preventDefault();
        if (value) {
            addTodo(value);
            setValue('');
        }
    };
    const handleChange = (e) => {
        e.preventDefault();
        setValue(e.target.value);
    };

    return (
        <form className="TodoForm" onSubmit={handleSubmit}>
            <input
                type="text"
                value={value}
                onChange={handleChange} className="todo-input" placeholder='Descrição da
Tarefa'/>
            <button type="submit" className='todo-btn'>Adicionar Tarefa</button>
        </form>
    )
}
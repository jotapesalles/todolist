import React from 'react'
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPenToSquare, faTrash} from "@fortawesome/free-solid-svg-icons";

export const TodoList = ({task, deleteTask, editTask, completeTask, undoTask}) => {
    return (
        <div className="Todo">
            <p className={`${task.completed ? "completed" : "incompleted"}`}
               onClick={task.completed ? undoTask : completeTask}>
                {task.name}
            </p>
            <div>
                <FontAwesomeIcon className="delete-icon" icon={faTrash} onClick={deleteTask}/>
                <FontAwesomeIcon className="edit-icon" icon={faPenToSquare} onClick={editTask}/>
            </div>
        </div>
    )
}
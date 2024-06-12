import React from 'react';
import {TodoWrapper} from './components/TodoWrapper';
import {TodoWrapperService} from "./components/TodoWrapperService";

export function App(props) {
    return (
        <div className='App'>
            <TodoWrapperService/>
        </div>);
}
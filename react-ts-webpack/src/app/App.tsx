import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { Hello } from './components/Hello';
import RocketData from "./components/rocket-data";
import {RouteComponentProps} from "react-router";
import {applyMiddleware, combineReducers, createStore} from "redux";
import {createLogger} from "redux-logger";
import thunk from "redux-thunk";
import adjuster from "./DelayReducer";
import {Provider} from "react-redux";
// ReactDOM.render(<Hello compiler="Typescript" framework="React" bundler="Webpack" />,
// history={{location:{pathname:'', search:'', state:'', hash:'', },length:0, action:undefined }}

const middleWare = [];
middleWare.push(thunk);
const loggerMiddleware = createLogger({
    predicate: () => (true),
    diff:true
}) ;
middleWare.push(loggerMiddleware)


var store =  createStore(combineReducers({delay:adjuster}), /* preloadedState, */
    (window as any).__REDUX_DEVTOOLS_EXTENSION__ && (window as any).__REDUX_DEVTOOLS_EXTENSION__(), applyMiddleware(...middleWare));
ReactDOM.render(
    <Provider store={store}>
        <RocketData   />
    </Provider>
    ,
document.getElementById('root'));

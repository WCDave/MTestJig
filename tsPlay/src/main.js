import Content from './Content';
import Canvas from './Canvas';
import {Provider} from 'react-redux';
import { combineReducers, createStore, applyMiddleware } from 'redux';
import adjuster from './DelayReducer';
import greetReducer from './GreetReducer';
import canvasReducer from './CanvasReducer';
import {  Router, Route, browserHistory  } from 'react-router';
import thunk from 'redux-thunk';
import createLogger  from 'redux-logger';

var React = require('react');
var ReactDOM = require('react-dom');

const middleWare = [];

middleWare.push(thunk)

const loggerMiddleware = createLogger({
    predicate: () => (true),
    diff:true
});
middleWare.push(loggerMiddleware)

var store =  createStore(combineReducers({timers:adjuster, greet:greetReducer, canvas:canvasReducer}), /* preloadedState, */
   window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__(), applyMiddleware(...middleWare));

ReactDOM.render(
  <Provider store={store}>

      <Router history={browserHistory}>
      <div>
         <Route exact path='/' component={Content}/>
         <Route path='/canvas' component={Canvas}/>
       </div>
     </Router>
  
  </Provider>, document.getElementById('content')
);

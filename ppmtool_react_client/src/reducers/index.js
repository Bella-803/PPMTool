import { combineReducers } from "redux";
import errorReducer from "./errorReducer";
import projectReducer from "./projectReducer";
import backlogReducer from "./backlogReducer";
import securityReducer from "./SecurityReducer";

//all reducers meet here
//this combineReducer is the main reducer that goes into the store
export default combineReducers({
  //error reducer
  errors: errorReducer,
  project: projectReducer,
  backlog: backlogReducer,
  security: securityReducer,
});
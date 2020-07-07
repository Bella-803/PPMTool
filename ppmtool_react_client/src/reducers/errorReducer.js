//this is the reducer that we are going to use for the error coing from server

//import the action type that we are gonna use

import { GET_ERRORS } from "../actions/types";

const initialState = {};

export default function(state=initialState,action){
    switch(action.type){

        case GET_ERRORS:
            return action.payload;

        default:
            return state;
    }
}
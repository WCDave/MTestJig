import ActionCreatorBuilder from "./ActionCreatorBuilder";
import * as request from 'superagent';

let acb = new ActionCreatorBuilder();

export const rocketTick=(filter:string, id:string)=>{
    let acb = new ActionCreatorBuilder();
    let action = acb.asynch('http://localhost:8080/SpringWSProject-0.0.1-SNAPSHOT/rest/rocketData/rd',filter).path('timers.ROCKET').build();
    return action({id:id});
}

export const testAC=(item:any)=> {
    return {
        type:'TESTAC',
        item
    }
}

export const testingAC=(newItem:any)=> {
    return {
        type:'TESTINGAC',
        newItem
    }
}

export const X=()=>{
    return {
        type:'X'
    }
}

export const getWeather:any = (id:string)=> {
    const url = 'https://avwx.rest/api/metar/'+id;
    return request.get(url).then((data:any)=>{
        console.log(data.body);
        return data.body;
    })

}
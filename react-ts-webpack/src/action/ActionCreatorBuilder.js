'use strict';

import * as _ from 'lodash';

export default class ActionCreatorBuilder {

    constructor() {
        this._url='';
        this._actionObj;
        this._filter=null;
        this.output=null;
        this.state={data:null, error:{}, path:null};
        this._nextAction=null;
    }

    asynch=(url,filter)=> {
         this._url=url;
         this._filter=filter;
         this.actionObj = null;
         return this;
    }
    path=(path)=> {
        this.state.path = path.split('.')[0];
        this.state.type = path.split('.')[1];
        return this;
    }

    synch=()=> {
      this._url = null;
      return this;
    }

    after=(data)=>{
        Object.assign(this.state,{after:data});
       return this;
    }

    before=(data)=>{
        Object.assign(this.state,{before:data});
        return this;
    }

    build() {
        if(this._url) {
            return  this._buildAsynch();
        }
        else {
            return this._buildSynch();
        }
    }

    _buildSynch=()=> {
        return (data)=>{
            return(dispatch)=> {
                return dispatch({type:this.state.type,...data});
            }
       }
    }

    extractKeyValues=(obj)=>{
        let values=Object.values(obj);
        let key = Object.keys(obj)[0];
        let result = key+'=';
        if(_.isArray(values)) {
           values.map((item)=>{
               result+=item+',';
           }) ;
           result.substr(0,result.length-2);
        }
        else {
            result += values;
        }
        return result;
    }

    //[{option:v or [v]}]

    
    _buildAsynch=()=> {
        let urlAppend='';
        if(this._filter) {
            let data = this._filter;
            if(_.isArray(data)) {
                data.map((item)=> {
                    urlAppend = this.extractKeyValues(item);
                })
            }
            else {
                urlAppend = this.extractKeyValues(data);
            }
        }
        if(urlAppend.length) {
            urlAppend = '?'+urlAppend;
        }
        const fetchOptions = {...{type:this.state.type, reqTag:'_REQ',respTag:'_REC'}};
        return (urlParams={})=> {
            return (dispatch) => {
                dispatch({type:fetchOptions.type+fetchOptions.reqTag});
                let req = new Request(this._url+urlAppend);
                fetch(req, fetchOptions).then(result => {
                    if (result.ok) {
                        let _result = result.json();
                        return _result;
                    }
                    else {
                        this.error = result;
                    }
                }).then((item) => {
                    let action= {type:fetchOptions.type+fetchOptions.respTag, data:item};
                    action = Object.assign(action,urlParams);
                     dispatch(action);
                });
            }
        }
    }


    
}

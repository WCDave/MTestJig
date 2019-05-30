import * as _ from 'lodash';



const adjuster = (state:any, action:any) => {
    switch (action.type) {
        case 'XD' :{
            return {...state, result:action.result};
            break;
        }
        case  'ERROR':{
            return{...state, field:action.field}
        }
        default: return {
            ...state
        }
    }

}

export default adjuster

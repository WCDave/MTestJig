import * as React from 'react';
import { Dispatch } from 'redux';
import { connect } from 'react-redux';
import {
    Formik,
    Field,
    Form,
    ErrorMessage,
    FieldProps,
    FormikFormProps,
    FieldArray,
    FormikProps,
    FieldArrayRenderProps
} from "formik";
import * as Yup from 'yup';
import {getWeather} from '../../action/Actions';
import DatePicker from 'react-datepicker';
import * as moment from 'moment';
import {SyntheticEvent} from "react";
import 'react-datepicker/dist/react-datepicker.min.css';
import {Moment} from "moment";
import WrappedField from "./WrappedField";
import Select from "react-select";
import {ValueType} from "react-select/lib/types";


declare type ComponentProps = IProps;

interface IProps {
    compiler: string,
    framework: string,
    bundler: string,
    formikProps?:any,
    result?:any
}

// interface FunctionProps {
//     punt: (id:string)=>void;
// }

const aDate = (props:FieldProps) => (
    <DatePicker onChange={(date:Moment)=>{
        props.form.setFieldValue(props.field.name, date);
    }} selected={props.form.values[props.field.name]}/>
);

const faComp =(props:FieldArrayRenderProps)=> (
    <div>
        {props.form.values.fa.map((item:any,idx:any)=> {
            return (<div key={idx}>{item.value}</div>)
        })}
    </div>
)

const selectOptions:ValueType<{value:string,label:string}>[] = [
    {value:'a', label:'a'},
    {value:'b', label:'b'},
    {value:'c', label:'c'}
]

const customSelect =(props:FieldProps)=> (
    <Select onChange={(opt:ValueType<{value:string,label:string}>, at)=>{
        props.form.setFieldValue(props.field.name, opt);
    }
    } value={props.form.values[props.field.name]}
            options={selectOptions}
            onInputChange={(x)=>{
                let hh=0;
            }}
    />
)


export default class Hello extends React.Component<ComponentProps, {}> {

    constructor(props:ComponentProps) {
        super(props);
        this.state={name:'', password:''};
    }

    testIt=()=> {
        console.log('test');
    }

    render() {
        return (
            <div>
                <WrappedField name='aDate' component={aDate} {...this.props}/>
                <Field name='bDate' component={aDate}/>
                {/*<ErrorMessage name='bDate'/>*/}
                <Field name='email' type='email' />
                <Field name='password' />
                <Field name='chk' type='checkbox'/>
                <ErrorMessage name='password'/>
                <Field name='selectTest' component={customSelect}/>
                <ErrorMessage name='selectTest' render={(msg)=> { return (<div>{msg}</div>)}}/>
                HELLO
                <FieldArray name="fa"
                            component={faComp}/>
            </div>
        )
    }
}

// const mapStateToProps = (state:any,props:any)=> {
//     return {
//         result:state.delay.result
//     }
// }
//
//
// function mapDispatchToProps  (dispatch:Dispatch<any>,ownProps:IProps):FunctionProps {
//     return {
//         punt: (id:string) => {
//             dispatch({type: 'X', id});
//         }
//     }
// }
//
// export default connect<{}, FunctionProps,IProps>(mapStateToProps, mapDispatchToProps)(Hello);

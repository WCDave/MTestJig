import * as React from 'react'
import {AgGridReact} from "ag-grid-react";
import 'ag-grid-community/dist/styles/ag-grid.css';
import 'ag-grid-community/dist/styles/ag-theme-blue.css';
import {
    connect, ErrorMessage,
    FieldArray,
    FieldArrayRenderProps,
    FieldAttributes,
    FieldProps, FormikContext,
    FormikErrors,
    FormikProps, getIn
} from "formik";

// import { Field, getIn } from 'formik';



// This component renders an error message if a field has
// an error and it's already been touched.
// const ErrorMessage = (props:any) => {
//     // All FormikProps available on props.formik!
//     const error = getIn(props.formik.errors, props.name);
//     const touch = getIn(props.formik.touched, props.name);
//     return touch && error ? error : null;
// };

// const xx = connect(ErrorMessage);

// Usage
interface ComponentProps {
    value:boolean;
    api:any;
}
class CheckBoxEditor<Values = {}, Props = {}> extends React.Component<any & { formik: FormikContext<Values> },
    any> {
    constructor(props:ComponentProps){
        super(props);
        this.state = {value:this.props.value}
        //this.getValue = this.getValue.bind(this);
        this.isPopup = this.isPopup.bind(this);
    }

    isPopup() {
        return false ;
    }

    getValue() {
        return this.state.value;
    }

    handleChange=(event:any)=> {
        // const newValue = event.currentTarget.value == 'true';
        this.setState( {
            value:!this.state.value
        }, ()=> this.props.api.stopEditing());

    }
    render() {

        return (
            <input type='checkbox' checked={this.state.value} value={this.state.value} onChange={this.handleChange}/>
    )
    }
}

class CellRenderer<Values = {}, Props = {}> extends React.Component<FieldAttributes<Props> & { formik: FormikContext<Values> },
    {}> {

    constructor(props:any) {
        super(props);

    }
    render(){
        return (
            <div>{this.props.value}</div>
        )
    }
}

class CellEditor<Values = {}, Props = {}> extends React.Component<any & { formik: FormikContext<Values> },
    any> {
    constructor(props:any) {
        super(props);
        this.state = {
            value: this.props.value
        };
    }

    onKeyDown=(event:any)=> {
        console.log('keyDown');
        var x=6;
    }

    isPopup() {
        return false ;
    }

    getValue() {
        return this.state.value;
    }

    handleChange=(event:any)=> {
        this.setState({value: event.target.value.toUpperCase()});
        const rowIdx = this.props.rowIndex;
        this.props.column.colDef.cellEditorParams.formik.setFieldValue(`fa2[${this.props.rowIndex}].price`, event.target.value,true);
        this.props.column.colDef.cellEditorParams.formik.validateForm(this.props.formik.values);
        console.log(event.target.value);
    }

    render() {
        return (
            <div>
                {/*<Field formik={this.props.column.colDef.cellEditorParams.formikProps()} name={`fa2[${this.props.rowIndex}].price`} label="QPC" />*/}
            <input ref="input"
        type={'text'}
        value={this.state.value}
        onKeyDown={this.onKeyDown}
        onChange={this.handleChange}
        />
        <ErrorMessage name={`fa2[${this.props.rowIndex}].price`} />
        </div>
    );
    }
}

// const columnDefs = [
//     {headerName: "Make", field: "make"},
//     {headerName: "Model", field: "model"},
//     {headerName: "Price", field: "price", editable:true, cellEditorFramework: connect(CellEditor,), cellEditorParams:this.getCellEditorParams}
//
//     ]
//
// const faComp =(props:FieldArrayRenderProps)=> {
//     console.log(props + " ****");
//     const change =(props:any)=>(x:any)=> {
//         console.log(x + "^^^^^");
//     }
//     return (
//
//         <AgGridReact
//             columnDefs={columnDefs}
//             rowData={props.form.values.fa2}
//             onCellValueChanged={change(props)}
//         />
//     )
//
// }


export default class AgGridPanel extends React.Component<any,any> {

    constructor(props:any){
        super(props);
        this.state = {
            columnDefs: [
                {headerName: "Make", field: "make"},
                {headerName: "Model", field: "model"},
                {headerName: "Price", field: "price"}

            ],
            rowData: [
                {make: "Toyota", model: "Celica", price: 35000, x:true},
                {make: "Ford", model: "Mondeo", price: 32000, x:false},
                {make: "Porsche", model: "Boxter", price: 72000, x:false}
            ]
        }
    }

    cellchanged=(props:any)=>(ev:any)=>{
        console.log(ev+" +++");
    }

    getCellEditorParams=()=> {
        return this.props;
    }


    render() {
        // const columnDefs = [
        //     {headerName: "Make", field: "make"},
        //     {headerName: "Model", field: "model"},
        //     {headerName: "Price", field: "price", editable:true,
        //         cellEditorFramework: connect(CellEditor),
        //         cellRendererFramework: connect(CellRenderer)}
        //
        // ]
        return (
            <div style={{ height: '150px', width: '600px' }} className="ag-theme-blue">
        <FieldArray name="fa2" render={(props:FieldArrayRenderProps) => (
            <div style={{ height: '350px', width: '600px' }}>
        <AgGridReact
            columnDefs={[
                {headerName: "Make", field: "make"},
        {headerName: "Model", field: "model"},
        {headerName: "Price", field: "price", editable:true,
            cellEditorFramework: connect(CellEditor),
            cellEditorParams: {
            formik: props.form
        },
            cellRendererFramework: CellRenderer,
                cellRendererParams: {
            formik: props.form
        }
        },
        {headerName:'X', field:'x', editable:true,
            cellEditorFramework:connect(CheckBoxEditor),
            valueSetter:(data:any)=>{
            return true;
        }
        }
    ]}
        rowData={props.form.values.fa2}
        // onCellValueChanged={(y:any)=> {
        //     console.log(y+" $$$$");
        // }}
        onCellValueChanged={this.cellchanged(props)}
        />
        {JSON.stringify(props.form.errors.fa2)}
        {/*<ErrorMessage name='fa2' render={(msg)=> {*/}
        {/*return (<div>{msg}</div>)*/}
        {/*}}/>*/}

        </div>


    )} />
        </div>
    )
    }
}


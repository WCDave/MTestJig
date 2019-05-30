import * as React from 'react';
import {AgGridReact} from 'ag-grid-react';
import {GridApi, ColDef, GridReadyEvent, ICellRendererParams, RowNode, IFilterComp, TextFilter} from 'ag-grid-community';
import {RowSpanParams} from '../../../node_modules/ag-grid-community/dist/lib/entities/colDef';
import 'ag-grid-community/dist/styles/ag-grid.css';
import 'ag-grid-community/dist/styles/ag-theme-blue.css';
import {StatelessComponent, SyntheticEvent} from "react";
import {Link, NavLink, RouteComponentProps, withRouter} from "react-router-dom";
import 'bootstrap/dist/css/bootstrap.min.css';
import Select from 'react-select'
import {connect} from "formik";

interface StateProps {
    rocketData?: object;
    imageData?: any;
    Id?: string;
    text?:string;
}

// interface ComponentProps extends StateProps, RouteComponentProps<{ Id?: string }> {
//     xxx?: string;
// }
interface ComponentProps extends StateProps {
    xxx?: string;
}



class RocketData extends React.Component<ComponentProps, StateProps> {

    rocketCountDown: number = null;
    wxCountDown: number = null;
    api: GridApi;
    colApi: any;
    identifier: string = 'mqs';
    filter?:IFilterComp;

    constructor(props: ComponentProps) {
        super(props);
        this.state = {
            rocketData: undefined,
            imageData: undefined,
            Id: 'mqs'
        }
    }

    componentDidMount() {
        this.rocketData();
        this.rocketCountDown = setInterval(this.rocketData, 10000);

        this.wxData();
        this.wxCountDown = setInterval(this.wxData, 10000 * 6);
    }

    componentDidUpdate(prevProps: Readonly<ComponentProps>, prevState: Readonly<StateProps>, snapshot?: any): void {
        // if (prevProps.match.params !== this.props.match.params) {
        //     const {Id} = this.props.match.params;
        //     if (Id) {
        //         this.setState({Id}, () => this.wxData());
        //     } else {
        //         this.setState({Id: 'mqs'}, () => this.wxData());
        //     }
        // }

        //if (prevProps.match.params !== this.props.match.params) {
            const {Id} = {Id:'mqs'};
        // this.wxData();
            // if (Id) {
            //     this.setState({Id}, () => this.wxData());
            // } else {
            //     this.setState({Id: 'mqs'}, () => this.wxData());
            // }
       // }
    }

    componentWillUnmount(): void {
        this.rocketCountDown = null;
        this.wxCountDown = null;
    }

    wxData = () => {
        const xmhr = new XMLHttpRequest();
        xmhr.addEventListener("load", (evt: ProgressEvent) => {
            const imageUrl = URL.createObjectURL((evt.target as XMLHttpRequest).response);
            this.setState({imageData: imageUrl});
        });
        const time = new Date().getTime();
        xmhr.open('GET', `http://localhost:8080/FT2-0.0.1-SNAPSHOT/TestServlet?identifier=${this.state.Id}&x='${time + ''}`, true);
        xmhr.setRequestHeader('method', 'GET');
        xmhr.setRequestHeader('Access-Control-Allow-Origin', '*');
        // xmhr.setRequestHeader('mode','no-cors');
        xmhr.responseType = "blob";
        xmhr.send(null);
    }

    rocketData = () => {
        fetch('http://localhost:8080/SpringWSProject-0.0.1-SNAPSHOT/rest/rocketData/rd?x=REACT', {method: 'GET'})
            .then(result => {
                return result.json();
            })
            .then((item: any) => {
                this.setState({rocketData: item})
            });
    }

    onGridReady = (ev: GridReadyEvent) => {
        this.api = ev.api;
        // this.api.forEachNode((node:any,idx:number)=>{
        //     console.log(node);
        //     node.gridApi.startEditingCell({
        //         rowIndex: node.rowIndex,
        //         colKey: 'price'
        //     });
        // })
        this.colApi = ev.columnApi;
        this.api.redrawRows();
        this.api.setAlwaysShowVerticalScroll(true);
        this.filter = this.api.getFilterInstance('parameter');
    }

    onRowClicked = (x: any) => {

    }
    getIdent = (ev: React.SyntheticEvent) => {
        this.identifier = (ev.currentTarget as HTMLInputElement).value;
        this.setState({Id: this.identifier}, () => this.wxData());
    }

    filterChange = (ev:React.ChangeEvent<HTMLInputElement>) =>{
          this.setState({text:ev.currentTarget.value.length >0  ? ev.currentTarget.value : undefined},()=>{
              this.api.onFilterChanged();
        })
    }


    render() {
        const customStyles = (width = 100, height = 15) => {
            return {
                container: (base:any) => ({
                    ...base,
                    display:'inline-block',
                    width: width,
                    maxControlHeight: height,
                    minHeight:height
                }),
                valueContainer: (base:any) => ({
                    ...base,
                    maxHeight: height,
                    minHeight: height
                })
            }
        }

        console.log(this.props);
        let keys = this.state.rocketData ? Object.keys(this.state.rocketData) : [];
        let display: any = keys.map((item: string) => {
            return {parameter: item, value: this.state.rocketData[item as keyof object]}
        });
        // display.push({parameter: 'refName', value: 'Moon1'});
        const coldefs = [
            {
                headerName: "Parameter", field: "parameter", resizable: true, sortable: true, filter: true
            },
            {headerName: "Value", field: "value", resizable: true}
        ];
        const imgSrc: string = this.state.imageData;
        // '<img src="data:image/gif;base64,' + xmlhttp.responseText + '"/>';
        const imgURL = 'http://localhost:8080/FT2-0.0.1-SNAPSHOT/TestServlet?identifier=mqs&x=' + new Date().getTime() + '';
        const myComponent:React.ComponentType<ICellRendererParams> = connect((data:ICellRendererParams, ...props)=>{
            const isPopup=()=> {
                return true;
            }
            //data.node.setRowHeight(50);
            return(
                    <div className="row" >
                        <div className="col-sm-6" >
                            <Select
                                options={[{label:'1', value:1}, {label:'2', value:2}, {label:'3', value:3}]} value={{label:'1', value:1}}
                                styles={{ control: (base, _state) => ({...base, minHeight: '25px', height: '25px'}),
                                    valueContainer:(base, _state) => ({...base, minHeight: '25px', height: '25px'}),
                                    dropdownIndicator: (base) => ({minHeight: '25px', height: '25px'})}}
                            />
                        </div>
                        <div className="col-sm-6"/>
                    </div>
            )
        });
        return (
            <div className=" ag-theme-blue dave">
                <div className="row">
                    <div className="col-sm-6">
                        <div className="row">
                            <div className="col-sm-12">
                                <input onChange={this.filterChange}/>
                                <AgGridReact
                                    alwaysShowVerticalScroll
                                    pagination
                                    onGridReady={this.onGridReady}
                                    // onRowClicked={this.onRowClicked}
                                    rowData={display}
                                    isFullWidthCell={(node:RowNode)=>{
                                        return node.data.parameter==='refName';
                                    }}
                                    fullWidthCellRenderer='refCellRenderer'
                                    columnDefs={coldefs}
                                    frameworkComponents={{
                                        refCellRenderer: myComponent
                                    }
                                    }
                                    suppressMenuHide
                                    rowHeight={30}
                                    gridOptions = {{isExternalFilterPresent:()=>(true),
                                        doesExternalFilterPass:(node)=>{
                                        if(!this.state.text || this.state.text.length ===0) {
                                        return true;
                                    }
                                        return node.data.parameter && node.data.parameter.startsWith(this.state.text);
                                    }
                                    }}
                                    // isExternalFilterPresent={()=>(true)}
                                    // doesExternalFilterPass={(node)=>{
                                    //     if(!this.state.text || this.state.text.length ===0) {
                                    //         return true;
                                    //     }
                                    //     return node.data.parameter && node.data.parameter.startsWith(this.state.text);
                                    // }}
                                   // suppressRowTransform
                                   //  rowSelection={'single'}
                                />
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <input type="text" onBlur={this.getIdent}/>
                                {/*<NavLink className="btn-info btn" to={'/test'}>test</NavLink>*/}
                            </div>
                            <div className="col-sm-6" >
                                {/*<NavLink className="btn-info btn" to={'/formikStuff'}>formik</NavLink>*/}
                            </div>
                        </div>
                    </div>
                    <div className="col-sm-6">
                        <img src={imgSrc} width={700} height={700}/>
                    </div>
                </div>
            </div>
        )
    }
}

export default (RocketData)
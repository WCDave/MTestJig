import * as React from 'react';

export default class Child extends React.Component<any> {
    constructor(props:any){
        super(props);
    }
    render() {
        return (<div>Hello Child{this.props.x}</div>)
    }
}
import * as React from 'react';
import {ErrorMessage, Field} from "formik";
import WrappedField from './WrappedField';


export default  class TestComponent extends React.Component<any,{},{}> {
      render() {
          return (
              <div>
                  <WrappedField name='testText' {...this.props} />
                  {/*<ErrorMessage name='testText' render={(msg) => <div className={'fieldError'} title={msg}>{'*'}</div>}  />*/}
              </div>);
      }
}
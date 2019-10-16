import React, {Component} from 'react'
import {View, TextInput, Button} from 'react-native'

export default class LoginComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            account: '1017',
            password: '123456'
        }
    }

    render() {
        return (
                <View style={{
                    flex: 1,
                    padding: 20,
                    height: '100%',
                    backgroundColor: 'green',
                    alignItems: 'center'
                }}>
                    <TextInput
                            style={{width: '100%', backgroundColor: 'white'}}
                            onChangeText={text => this.state.account = text}
                            value={'1017'}
                            placeholder={'Account'}/>
                    <TextInput
                            style={{width: '100%', backgroundColor: 'white', marginVertical: 10}}
                            onChangeText={text => this.state.password = text}
                            value={'123456'}
                            placeholder={'Password'}/>
                    <Button
                            title={'Connect'}
                            onPress={this._onConnect}/>
                </View>
        );
    }

    _onConnect = () => {
        const {account, password} = this.state;
        const {onConnect} = this.props;
        onConnect(account, password)
    }
}

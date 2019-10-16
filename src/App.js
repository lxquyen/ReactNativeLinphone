import React, {Component} from 'react'
import {Text, Alert, View} from 'react-native'
import ToastModule from './ToastExample'
import {LinphoneEvents, LinphoneModule} from "./LinphoneManager";
import LoginComponent from "./LoginComponent";
import MainComponent from "./MainComponent";
import {IncomingReceived, Released} from "./Constant";

export default class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isConnected: false,
            incomingReceived: false
        }
    }


    render() {
        const {isConnected, incomingReceived} = this.state;
        return (
                <View style={{flex: 1, backgroundColor: 'red'}}>
                    {isConnected ?
                            <MainComponent
                                    incomingReceived={incomingReceived}
                                    onAnswer={this._onAnswer}
                                    onDecline={this._onDecline}
                                    onCallVoice={this._onCallVoice}
                                    onCallVideo={this._onCallVideo}
                            /> :
                            <LoginComponent
                                    onConnect={this._onConnect}/>}
                </View>
        )
    }

    _onConnect = (account, password) => {
        ToastModule.show(`${account} - ${password}`, ToastModule.SHORT);
        LinphoneModule.connect(account, password);
    };

    _onCallVoice = () => {
        LinphoneModule.call("1015");
    };

    _onCallVideo = () => {
        LinphoneModule.call("1015");
    };

    _onAnswer = () => {
        LinphoneModule.answer();
    };

    _onDecline = () => {
        LinphoneModule.decline();
    };

    componentDidMount() {
        LinphoneEvents
                .addListener('ON_CALL_STATE_CHANGED', event => {
                    ToastModule.show(event.state, ToastModule.SHORT);
                    if (event.state === IncomingReceived) {
                        this.setState({incomingReceived: true})
                    } else if (event.state === Released) {
                        this.setState({incomingReceived: false})
                    }
                });
        LinphoneEvents
                .addListener('ON_REGISTRATION_STATE_CHANGED', event => {
                    ToastModule.show(event.state, ToastModule.SHORT);
                    this.setState({isConnected: event.state === 'Ok'})
                })
    }
}

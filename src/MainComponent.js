import React, {Component} from 'react'
import {Button, View} from 'react-native'

export default class MainComponent extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        const {incomingReceived,onCallVoice, onCallVideo, onAnswer, onDecline} = this.props;
        return (
                <View style={{flex: 1, backgroundColor: 'white', justifyContent: 'center', alignItems: 'center'}}>
                    <Button
                            onPress={onCallVoice}
                            style={{marginVertical: 10}}
                            title={'Call Voice'}/>
                    <Button
                            onPress={onCallVideo}
                            style={{marginVertical: 10}}
                            title={'Call Video'}/>
                    {incomingReceived && (
                            <View>
                                <Button
                                        onPress={onAnswer}
                                        style={{marginVertical: 10}}
                                        title={'Call Answer'}/>
                                <Button
                                        onPress={onDecline}
                                        style={{marginVertical: 10}}
                                        title={'Call Decline'}/>
                            </View>
                    )}
                </View>
        )
    }
}

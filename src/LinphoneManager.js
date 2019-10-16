import {NativeModules, NativeEventEmitter} from 'react-native';

export const {LinphoneModule} = NativeModules;

export const LinphoneEvents = new NativeEventEmitter(LinphoneModule);

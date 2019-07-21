/**
 * @format
 */

import {AppRegistry} from 'react-native';
import App from './App';
import {name as appName} from './app.json';
import rnBridge from './src/bridge/rn.bridge.listener'

AppRegistry.registerComponent(appName, () => App);

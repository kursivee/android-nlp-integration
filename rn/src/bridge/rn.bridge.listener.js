import { DeviceEventEmitter } from 'react-native'
import RnBridge from './rn.bridge'
import nlp from '../nlp/nlp.service'

DeviceEventEmitter.addListener('text', async (evt) => {
    const response = await nlp.process(evt)
    RnBridge.publish("NAV", JSON.stringify(response))
})

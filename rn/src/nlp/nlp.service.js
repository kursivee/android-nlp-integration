import { RnNlpManager } from 'node-nlp'

const manager = new RnNlpManager({ languages: ['en'] })
// Need to find out why this is necessary.
// Not calling .process initially leads to load time on first call
manager.process('en','')

module.exports = manager

# android-nlp-integration
POC for ntegrating nodenlp into android with react-native
This is a POC project. I know I'm missing a lot of best practices. No MVVM, DI isn't properly setup, etc. Feel free to write issues calling these out. Especially if it's JS related.

### /android
Actual android app to run (Still in progress)

### /bridge
Contains logic for bridge between RN and Android

### /rn
Contains react native logic for running modified node-nlp. Also where android app is being tested.

### /trainer
Node lib responsible for creating the model.nlp to be consumed by nlp. 
Unforuntatly RN nlp does not support loading by file so this is on pause.
I do have ideas how to get this to work though

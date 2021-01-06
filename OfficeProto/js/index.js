import React, { Component, useState } from 'react';
import { StyleSheet, NativeModules, Alert, AppRegistry, Button, Text, TextInput, View } from 'react-native';
import codePush from "react-native-code-push";
const { LensCameraModule } = NativeModules;

export default function PizzaTranslator() {
  const [text, setText] = useState('');
  return (
    <View style={styles.container}>
      
      <View style={styles.container2}>

        <View style={styles.buttonContainer}>
          
          <Button 
            title="Scan to Document"
            onPress= { () => {LensCameraModule.launchScanToPDF();}} />

        </View>

        <View style={styles.buttonContainer}>
          
          <Button 
            title="Picture to Document"
            onPress= { () => {LensCameraModule.launchImageToPDF();}} />

        </View>

      </View>

      <View style={styles.buttonContainer}>
        
        <Button 
          title="&#8634;"
          onPress= { () => {codePush.sync();}} />

      </View>

    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'space-between',
  },
  container2: {
    flex: 1,
    justifyContent: 'flex-start',
    flexDirection: 'row'
  },
  buttonContainer: {
    margin: 10
  },
  multiButtonContainer: {
    margin: 20,
    flexDirection: 'row',
    justifyContent: 'space-between'
  }
});


  

// This is a workaround to disable BLob module which is currently not working (need to investigate the root cause) with the V8 Javascript engine used in Office Android apps.
global.Blob=undefined;
AppRegistry.registerComponent("ReactAction2", () => PizzaTranslator);


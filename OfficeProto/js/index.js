import React, { Component, useState } from 'react';
import { Alert, AppRegistry, Button, Text, TextInput, View } from 'react-native';
import codePush from "react-native-code-push";

export default function PizzaTranslator() {
  const [text, setText] = useState('');
  return (
    <View style={{padding: 10}}>
      { <TextInput
        style={{height: 40}}

        placeholder="Hello ... Type here to translate!  ORIGINAL !"
        onChangeText={text => setText(text)}
        defaultValue={text}
      /> }
      <Text style={{padding: 10, fontSize: 42}}>
        {text.split(' ').map((word) => word && '🍕').join(' ')}
      </Text>
      <Button 
        title="Codepush Sync"
        onPress= { () => {codePush.sync();}} />
    </View>

  );
}

// This is a workaround to disable BLob module which is currently not working (need to investigate the root cause) with the V8 Javascript engine used in Office Android apps.
global.Blob=undefined;
AppRegistry.registerComponent("ReactAction2", () => PizzaTranslator);


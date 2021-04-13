import React, {
    useState,
} from 'react';
import { 
    View,
    StyleSheet,
    Text,
    FlatList,
    Button,
    KeyboardAvoidingView,
    TextInput,
} from 'react-native';
import MessageDto from '../models/message';
import Message from '../components/Message';
import { MaterialIcons } from '@expo/vector-icons';

export interface ChatProps{
    messages: MessageDto[],
    isGroup: boolean,
    name: string,
    sendMessage: Function,
    goBack: Function,
}

export default function Chat(props: ChatProps){
    const [messageInput, setMessageInput] = useState(null);

    return(
        <KeyboardAvoidingView style={styles.container}>
            <View style={styles.viewStyle}>
                <Button title="Go Back" onPress={() => props.goBack()} />
                <Text>{props.name}</Text>
            </View>
            <View style={styles.history}>
                <FlatList 
                    data={props.messages}
                    renderItem={item => <Message message={item.item}/>}
                    keyExtractor={item => item.name + " " + item.timestamp}
                />
            </View>
            <View style={styles.textFieldContainer}>
                <TextInput style={styles.textField} placeholder="Enter your message here" keyboardType="default" value={messageInput} onChangeText={setMessageInput}/>
                <MaterialIcons name="send" size={24} color="black" onPress={() => {
                    let messageDto: MessageDto = { 
                        timestamp: new Date(Date.now()),
                        isGroup: props.isGroup,
                        message: messageInput,
                        name: props.name,
                        username: ""
                    };

                    props.sendMessage(messageDto)
                }} />
            </View>
        </KeyboardAvoidingView>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        paddingTop: 20,
    },
    history: {
        flex:1,
        borderColor: '#ABC4F2',
        borderRadius: 5,
        margin: 3
    },
    textFieldContainer: {
        width: '100%',
        flexDirection: 'row',
        alignItems: 'flex-start',
        alignContent: 'stretch',
    },
    textField: {
        width: '90%',
    },
    goBackButton: {
        width: '100%',
    },
    viewStyle:{
        flexDirection: 'row',
        alignItems: 'flex-start',
        justifyContent: 'space-around',
        width: '100%'
    }
})
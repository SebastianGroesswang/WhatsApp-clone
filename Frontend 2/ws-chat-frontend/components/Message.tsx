import React, {

} from 'react';
import {
    View,
    StyleSheet,
    Text,
} from 'react-native';
import MessageDto from '../models/message';

export interface MessageProps {
    message: MessageDto;
}

export default function Message({ message }: MessageProps) {
    return (
        <View style={styles.container}>
            <Text style={styles.infoText}>{message.username}, {message.timestamp}</Text>
            <Text style={styles.messageText}>{message.message}</Text>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
    },
    infoContainer: {
        flexDirection: 'row',
    },
    infoText: {
        fontSize: 10,
        fontWeight: '100',
    },
    messageText: {
        fontSize: 20
    }
})
import React, {

} from 'react';
import {
    StyleSheet,
    FlatList,
    TouchableOpacity,
    Text,
} from 'react-native'

import MemberhsipDto from "../models/membership";


export default function ChatPreview (props) {
    return (
        <TouchableOpacity style={styles.container} onPress={props.onPress}>
            <Text style={styles.text}>{props.name}</Text>
        </TouchableOpacity>
    )
}

const styles = StyleSheet.create({
    container: {
        height: '20%',
        borderRadius: 10,
        borderColor: 'gray',
        flex: 1,
        margin: 10,
    },
    text: {
        fontSize: 20,
        flex: 1,
    }
})
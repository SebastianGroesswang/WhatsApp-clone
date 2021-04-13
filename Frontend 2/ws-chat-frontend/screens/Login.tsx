import React, {
    useState,
} from 'react';
import {
    KeyboardAvoidingView,
    StyleSheet,
    Text,
    TextInput,
    SafeAreaView,
    Button,
    View,
    ToastAndroid,
    Platform
} from 'react-native';

export interface LoginProps {
    loginSuccessfull: Function,
}

export default function Login({ loginSuccessfull }: LoginProps) {
    const [url, setUrl] = useState("10.0.2.2:8079");
    const [username, setUsername] = useState("test");
    const [password, setPassword] = useState("test");

    const connect = async (type: "register" | "login") => {
        let token
        
        try {
            const res = await fetch("http://" + url + "/whatsapp/request/" + type, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    username: username,
                    password: password,
                })
            });
            token = await res.text();
            //console.log(res)
            console.log(username + " : " + token);
        }
        catch (error) {
            if (Platform.OS === 'android') 
                ToastAndroid.show("invalid credentials", ToastAndroid.LONG)             
            else if (Platform.OS === 'web') 
                alert("invalid credentials")
        }

        if (token)
            loginSuccessfull({
                server: url,
                token: token,
                username: username,
            })
            
    }

    return (
        <SafeAreaView style={styles.container}>
            <View>
                <KeyboardAvoidingView>
                    <View>
                        <TextInput placeholder="server address" keyboardType="url" value={url} onChangeText={setUrl} />
                        <TextInput placeholder="username" keyboardType="default" value={username} onChangeText={setUsername} />
                        <TextInput placeholder="password" keyboardType="visible-password" value={password} onChangeText={setPassword} />
                    </View>
                    <View style={styles.buttonContainer}>
                        <Button title="login" onPress={() => connect('login')} />
                        <Button title="register" onPress={() => connect('register')} />
                    </View>
                </KeyboardAvoidingView>
            </View>
        </SafeAreaView>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        paddingTop: 20,
        justifyContent: "space-around",
    },
    buttonContainer: {
        flexDirection: 'row',
        justifyContent: 'space-around'
    },
})
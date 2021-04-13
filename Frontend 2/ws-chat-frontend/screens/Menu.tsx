import React, {
    useState
} from 'react';
import { 
    View,
    StyleSheet,
    Text,
    FlatList,
    Button,
    TextInput,
} from 'react-native';
import ChatPreview from '../components/ChatPreview';
import MemberhsipDto from '../models/membership';


export default function Chat(props){

    const [isCreateVisible, setCreate] = useState(false);
    const [isJoinVisible, setJoin] = useState(false);
    const [groupName, setGroupName] = useState(null)
    const [groupList, setGroupList] = useState(null)

    //helper methods to enable after every button press a get call to get all groups
    const loadGroups = (bool: boolean) =>{
        setJoin(bool);
        props.getAllGroups()
        setGroupList(props.allGroups)
        
    }


    return(
        <View style={styles.container}>

            <View style={styles.viewStyle}>
                <Button title="create Group" onPress={() => { setCreate(!isCreateVisible)}} />
                <Button title="join Group" onPress={() => { loadGroups(!isJoinVisible )}} />
                <Button title="refresh" onPress={() => { 
                    props.getAllPublicChats()
                    props.getAllPrivateChats()
                    props.getAllGroups()
                    }}></Button>
            </View>

            { (isCreateVisible) ? <View>

                {isJoinVisible ? setJoin(false) : null}

                <Text>Group Name</Text>
                <TextInput placeholder="name" keyboardType="default" value={groupName} onChangeText={setGroupName} ></TextInput>
                <Button title="Save" onPress={() => {
                    props.createGroup(groupName)
                    setCreate(false)
                    }}></Button>
            </View> : null}

            { (isJoinVisible) ? <View>

                {isCreateVisible ? setCreate(false) : null}

                { groupList === null ? 
                    null : (groupList.length != 0 ? 
                        <FlatList
                        data={groupList}
                        keyExtractor={item => item.roomName}
                        renderItem={ item => <View><Text>{item.item.roomName}</Text><Button title="subscribe" onPress={ () => {props.createMembership(item.item.roomName)}}></Button></View>}/>
                      : <Text>No Groups created yet</Text>)
                }

            </View> : null}

            <Text>User Messages</Text>

            <FlatList
                data={props.privateChats}
                keyExtractor={item => item.username}
                renderItem={item => <ChatPreview name={item.item.username} onPress={() => props.switchToChat(item.item.username, false)} />}
            />

            <Text>Group Messages</Text>

            <FlatList
                data={props.publicChats}
                keyExtractor={item => item.roomName}
                renderItem={item => <ChatPreview name={item.item.roomName} onPress={() => props.switchToChat(item.item.roomName, true)} ></ChatPreview>}
                ></FlatList>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        height: '100%',
        paddingTop: 20,
        width:'100%'
    },
    sadLife:{
        paddingTop: 20
    },
    viewStyle:{
        flexDirection: "row",
        justifyContent:'space-around',
        alignItems:'flex-start',
        alignContent: 'space-around',
        width:'100%'
    },
    buttonStyle:{
        marginHorizontal:20
    }
})
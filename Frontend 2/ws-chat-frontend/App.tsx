import React, {
  useState
} from 'react';
import Chat from './screens/Chat';
import Login from './screens/Login';
import Menu from './screens/Menu';
import MessageDto from './models/message';
import UserDto from  './models/user';
import RoomDto from './models/room';
import MembershipDto from './models/membership';

let socket: WebSocket = null;

export default function App() {
  const [url, setUrl] = useState(null)
  const [nav, setNav] = useState('l')
  const [username, setUsername] = useState('')
  const [token, setToken] = useState(null)
  const [memberGroups, setMemberGroups] = useState([])
  const [messages, setMessages] = useState([]);
  const [currentChat, setCurrentChat] = useState(null);
  const [users, setUsers] = useState(null);
  const [groups, setGroups] = useState(null);
  const [chatSettings, setChatSettings] = useState(null);


  const sendMessage = (msg: MessageDto) => {
    msg.username = username

    let temp = JSON.stringify(msg)

    socket.send(temp)

    console.log(temp)
  }

  const createGroup = async (name:string) => {
    const res = await fetch("http://" + url + "/whatsapp/request/createGroup", {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: name
    });
  }

  const createMembership = async (groupName:string) => {
    const res = await fetch("http://" + url + "/whatsapp/request/createMembership", {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        username: username,
        groupName: groupName
      })
    });
  }

  const getAllGroups = async () => {
    const existingRoomResponse = await fetch("http://" + url + "/whatsapp/request/getAllGroups");
    const res: RoomDto[] = await existingRoomResponse.json();
    setGroups(res)
    return res
  }

  const getAllMemberGroups = async () => {
    const existingRoomResponse = await fetch("http://" + url + "/whatsapp/request/getAllMemberships/" + username);
    const res: RoomDto[] = await existingRoomResponse.json();
    setMemberGroups(res)
    return res
  }

  const getAllUsers = async () => {
    const existingUsersResponse = await fetch("http://" + url + "/whatsapp/request/getAllUsers");
    const existingUsers: UserDto[] = await existingUsersResponse.json();
    setUsers(existingUsers);
    return existingUsers
  }

  const loginSuccessfull = async ({server, token, username}: {server: string, token: string, username: string}) => {
    setUsername(username);
    setUrl(server)
    setToken(token)


    
    socket = new WebSocket("ws://" + server + "/chat/" + username)

    const existingUsersResponse = await fetch("http://" + url + "/whatsapp/request/getAllUsers");
    const existingUsers: UserDto[] = await existingUsersResponse.json();
    setUsers(existingUsers);

    const existingRoomResponse = await fetch("http://" + url + "/whatsapp/request/getAllMemberships/" + username);
    const res: RoomDto[] = await existingRoomResponse.json();
    setMemberGroups(res)

    

    socket.onmessage = event => {
      
      const msg: MessageDto = JSON.parse(event.data)

      console.log('~~~~~~~~~~')
      console.log(messages)
      setMessages(messages.concat(msg))

      console.log(messages)
      console.log('~~~~~~~~~~') 
    }
    
    setNav('m')
  }

  switch (nav) {
    case 'l': {
      return <Login loginSuccessfull={loginSuccessfull} />
    }
    case 'm': {
      return <Menu 
        privateChats={users.filter(user => user.username != username)} 
        getAllPrivateChats={getAllUsers}
        publicChats={memberGroups}
        getAllPublicChats={getAllMemberGroups} 
        createMembership={createMembership} 
        createGroup={createGroup} 
        allGroups={groups} 
        getAllGroups={getAllGroups}         
        switchToChat={(name, isGroup) => {
          setChatSettings([name, isGroup])
          setNav('c')
      }}/>
    }
    case 'c': {
      return <Chat name={chatSettings[0]} isGroup={chatSettings[1]} messages={messages.filter(msg => (msg.name === chatSettings[0] && msg.isGroup === chatSettings[1]))} sendMessage={sendMessage} goBack={() => setNav('m')} />
    }
  }
}

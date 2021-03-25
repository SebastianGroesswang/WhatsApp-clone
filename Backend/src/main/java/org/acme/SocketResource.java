package org.acme;

import org.acme.controller.UserLogic;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/chat/{username}")
@ApplicationScoped
public class SocketResource {

    //transport json objects between client and server which contains group or other user to specify the chat

    //map containing as key the username and as value the session of the user

    Map<String, Session> onlineSessions = new ConcurrentHashMap<>();
    @Inject
    UserLogic logic = new UserLogic();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        onlineSessions.put(username, session);
        broadcast("User " + username + " joined");
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        onlineSessions.remove(username);
        broadcast("User " + username + " left");
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, Throwable throwable) {
        onlineSessions.remove(username);
        broadcast("User " + username + " left on error: " + throwable);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("username") String username) {

        if(message.startsWith("--")){
            command(username,message);
        } else {
            broadcast(">> " + username + ": " + message);
            new Thread( () -> {
                //mr.create(new Message(                       username,                        message               ));
            }).start();
        }


    }

    private void broadcast(String message) {
        onlineSessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(message, result ->  {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        });
    }

    private void command(String user, String command) {
        var session = onlineSessions.get(user);

        var result = "";

        if(command.equals("--getOnlineUsers")){
            var list = onlineSessions.keySet();

            result += "These Users are online: \n";
            for (var s:
                    list) {
                result += "\t-" + s + "\n";
            }
        } /*else if(command.startsWith("//getMessageID:")){

            isOwnThread = true;

            new Thread(() -> {
                String resultText = "";
                var arr = command.split(":");
                long id;
                try{
                    id = Long.getLong(arr[1]);
                } catch (Exception e){
                    id = 0;
                }

                var mes = mr.find(id);

                resultText = "Here is the wanted message: \n" + mes.getText();

                session.getAsyncRemote().sendObject(resultText,  er ->  {
                    if (er.getException() != null) {
                        System.out.println("Unable to send message: " + er.getException());
                    }
                });
            }).start();



        }*/
        else if(command.equals("--help")){
            result += "These commands are supported: \n" +
                    "\t--getOnlineUsers -> get all online users \n"+
                    "\t--getMessageID:id -> get message with id -> not working, should be in rest service\n";

        }
        else {
            result = "not supported command";

        }


        session.getAsyncRemote().sendObject(result,  er ->  {
            if (er.getException() != null)
            {
                System.out.println("Unable to send message: " + er.getException());
            }
        });


    }

}

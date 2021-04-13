package org.acme;

import org.acme.coder.MessageDecoder;
import org.acme.coder.MessageEncoder;
import org.acme.controller.UserLogic;
import org.acme.dto.MessageDto;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.context.ThreadContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.Consumes;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/chat/{username}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
@ApplicationScoped
public class SocketResource {

    //transport json objects between client and server which contains group or other user to specify the chat

    //map containing as key the username and as value the session of the user

    Map<String, Session> onlineSessions = new ConcurrentHashMap<>();
    @Inject
    UserLogic logic = new UserLogic();

    ManagedExecutor executor = ManagedExecutor.builder().maxAsync(10).propagated(ThreadContext.CDI, ThreadContext.TRANSACTION).build();
    ThreadContext threadContext = ThreadContext.builder().propagated(ThreadContext.CDI, ThreadContext.TRANSACTION).build();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {

        executor.runAsync( () -> {
            MessageDto fd = new MessageDto();
            onlineSessions.put(username, session);

            fd.setName(username);
            fd.setMessage("HeelloThere");
            fd.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
            fd.setGroup(true);
            fd.setUsername("usergfdsgfnafdsaafdsafme");

            try {
                session.getBasicRemote().sendObject(fd);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (EncodeException e) {
                e.printStackTrace();
            }
        });

        /*


        //broadcast("User " + username + " joined");
        System.out.println(username + "joined");

        new Thread( () ->{
            MessageDto fd = new MessageDto();

            session.getAsyncRemote().sendObject(fd, result ->  {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        }).start();

*/

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
    public void onMessage(MessageDto message, @PathParam("username") String username) {


        executor.runAsync( () -> {

            logic.persistMessage(message);

            /*
            MessageDto fd = new MessageDto();

            fd.setName(username);
            fd.setMessage("HeelloThere");
            fd.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
            fd.setGroup(true);
            fd.setUsername("usergfdsgfnafdsaafdsafme");


            System.out.println("fdafdsafdsafdsafdsaa" + message.getName());

               */
            try {
                onlineSessions.get(username).getBasicRemote().sendObject(message);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (EncodeException e) {
                e.printStackTrace();
            }

        });




        /*
        broadcast(">> " + username + ": " + message);
        new Thread( () -> {
                //mr.create(new Message(                       username,                        message               ));
        }).start();


        new Thread(() -> {
            System.out.println(message.getMessage());
        });
        */


    }
    //todo ^^change to messageDto
    private void broadcast(String message) {
        onlineSessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(message, result ->  {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        });
    }
}

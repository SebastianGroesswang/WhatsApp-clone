package org.acme.Persistance;

import org.acme.models.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
public class ChatRepository {

    @Inject
    private EntityManager em;

    @Transactional
    public boolean persistMessage(Message mes){
        if(mes != null){
            //if(mes.getClass() == RoomMessage.class){       }

            em.persist(mes);

            return true;
        }
        return false;
    }

    @Transactional
    public boolean persistRoom(Room room){
        if(room != null){
            em.persist(room);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean persistUserJoinRoom(Membership ms){
        if(ms != null){
            em.persist(ms);
            return true;
        }
        return false;
    }

    public List<RoomMessage> loadMessagesFromChat(AppUser sender, Room room){
        List<RoomMessage> res = null;

        res = em.createQuery("select a from RoomMessage a where a.user = " + sender.getId() + ";", RoomMessage.class).getResultList();

        return res;
    }

    public List<UserMessage> loadMessagesFromChat(AppUser sender, AppUser getter){
        List<UserMessage> res = null;

        res = em.createQuery("select a from UserMessage a where a.user = " + sender.getId() + ";", UserMessage.class).getResultList();

        return res;
    }

    public List<Room> getAllGroups(){
        var result = em.createQuery("select g from Room g", Room.class).getResultList();
        return result;
    }

    public List<Room> getAllGroupsFromUser(AppUser user){
        var result = em.createQuery("select r from Room r join fetch Membership m where m.id.user.id = " + user.getId() + ";", Room.class).getResultList();
        return result;
    }




}

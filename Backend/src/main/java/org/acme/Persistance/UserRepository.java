package org.acme.Persistance;

import org.acme.models.AppUser;
import org.acme.models.Room;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class UserRepository {

    @Inject
    EntityManager em;

    @Transactional
    public void deleteAll(){
        //em.createNativeQuery("truncate table Membership").executeUpdate();
        //em.createNativeQuery("truncate table Room").executeUpdate();
        em.createNativeQuery("truncate table AppUser cascade ").executeUpdate();
    }

    @Transactional
    public void InsertUser(AppUser user){

        em.persist(user);

    }

    public AppUser GetUserByName(String name){
        try{
            var temp = em.createQuery("select a from AppUser a where a.username = '" + name + "'", AppUser.class).getResultList();
            if (temp.size() == 0)
                return null;
            return temp.get(0);
        } catch (NoResultException e){
            return null;
        }
    }


    public List<AppUser> getUsers() {

        return em.createQuery("select a from AppUser a", AppUser.class).getResultList();
    }

    public Room getRoomByName(String groupName) {
        try{
            Room temp = em.createQuery("select a from Room a where a.roomName = '" + groupName + "'", Room.class).getSingleResult();
            return temp;
        } catch (NoResultException e){
            return null;
        }
    }
}

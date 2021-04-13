package org.acme.controller;

import org.acme.Persistance.ChatRepository;
import org.acme.Persistance.UserRepository;
import org.acme.dto.MembershipDto;
import org.acme.dto.MessageDto;
import org.acme.dto.UserDto;
import org.acme.dto.UserOutDto;
import org.acme.models.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

@ApplicationScoped
public class UserLogic {


    @Inject
    private UserRepository userRepository;

    @Inject
    private ChatRepository chatRepository;

    public void insertDummyData(){
        var user = new AppUser();
        user.setUsername("test");
        user.setPassword("test");

        userRepository.InsertUser(user);

        var group = new Room();
        group.setRoomName("testGroup");

        chatRepository.persistRoom(group);

        var membershipId = new MembershipID();
        membershipId.setRoom(group);
        membershipId.setUser(user);

        var m = new Membership();
        m.setId(membershipId);

        chatRepository.persistUserJoinRoom(m);
    }

    public boolean isUserNameInRepo(String name){
        var res = userRepository.GetUserByName(name);

        if (res == null) {
            return false;
        }
        return true;

    }

    public boolean persistUser(UserDto user){
        var result = new AppUser();
        result.setUsername(user.getUsername());
        result.setPassword(user.getPassword());

        userRepository.InsertUser(result);

        return true;
    }

    public boolean isValidUser(UserDto user){
        var result = userRepository.GetUserByName(user.getUsername());

        if (result == null) {
            return false;
        }

        if(result.getPassword().equals(user.getPassword())){
            return true;
        }

        return false;
    }

    public void reset() {
        userRepository.deleteAll();
    }

    public UserOutDto[] getAllUsers() {

        var result = userRepository.getUsers();
        var list = new ArrayList<UserOutDto>();

        for (var item:
             result) {
            list.add(new UserOutDto(item.getId(), item.getUsername()));
        }

        return list.toArray(UserOutDto[]::new);
    }

    public Room[] getAllRooms() {
        var result = chatRepository.getAllGroups();

        return result.toArray(Room[]::new);
    }

    public void createRoom(String groupName) {
        Room temp = new Room(groupName);

        chatRepository.persistRoom(temp);
    }

    public void createMembership(MembershipDto dto) {

        var user = userRepository.GetUserByName(dto.getUsername());
        var room = userRepository.getRoomByName(dto.getGroupName());

        if (user != null && room != null){

            MembershipID temp = new MembershipID();

            temp.setUser(user);
            temp.setRoom(room);

            Membership result = new Membership();
            result.setId(temp);

            chatRepository.persistUserJoinRoom(result);
        }

    }

    public Room[] getAllRoomMemberships(String username) {



        var user = userRepository.GetUserByName(username);



        var result = chatRepository.getAllGroupsFromUser(user);

        if(result == null)
            return new Room[] {};
        return result.toArray(Room[]::new);

    }

    public void persistMessage(MessageDto msg){

        var sender = userRepository.GetUserByName(msg.getUsername());

        if (msg.isGroup()){
            RoomMessage res = new RoomMessage();
            var receiver = chatRepository.getRoomByName(msg.getName());
            res.setUser(sender);
            res.setRoom(receiver);
            res.setTimestamp(msg.getTimestamp().toLocalDateTime());
            res.setValue(msg.getMessage());

            chatRepository.persistMessage(res);

        } else {
            UserMessage res = new UserMessage();
            var receiver = userRepository.GetUserByName(msg.getName());
            res.setUser(sender);
            res.setRoom(receiver);
            res.setTimestamp(msg.getTimestamp().toLocalDateTime());
            res.setValue(msg.getMessage());

            chatRepository.persistMessage(res);
        }
    }

    public MessageDto[] getAllMessages() {
        var user = userRepository.GetUserByName("test");

        var res = chatRepository.loadMessagesFromUser(user);

        return res.toArray(MessageDto[]::new);
    }
}

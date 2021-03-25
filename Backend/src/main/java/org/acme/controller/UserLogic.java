package org.acme.controller;

import org.acme.Persistance.ChatRepository;
import org.acme.Persistance.UserRepository;
import org.acme.dto.UserDto;
import org.acme.models.AppUser;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class UserLogic {


    @Inject
    private UserRepository userRepository;

    @Inject
    private ChatRepository chatRepository;

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

    public AppUser[] getAllUsers() {

        var result = userRepository.getUsers();

        return result.toArray(AppUser[]::new);
    }
}

package org.acme;

import org.acme.controller.UserLogic;
import org.acme.dto.MembershipDto;
import org.acme.dto.MessageDto;
import org.acme.dto.UserDto;
import org.acme.dto.UserOutDto;
import org.acme.models.AppUser;
import org.acme.models.Room;
import org.acme.util.TokenUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

@Path("/whatsapp/request")
public class RequestResource {

    static long TOKEN_TIME = 360;

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String iss;

    @Inject
    JsonWebToken jwt;

    @Inject
    UserLogic logic;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "test";
    }

    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    //@Produces(MediaType.APPLICATION_JSON)
    public Response register(UserDto dto){
        try {
            var isUserNameInRepo = logic.isUserNameInRepo(dto.getUsername());

            if(!isUserNameInRepo){
                logic.persistUser(dto);
                return Response.ok(generateToken(dto.getUsername())).build();
            }
        } catch (Exception e) {
            Logger.getAnonymousLogger().info(e.toString());
            e.printStackTrace();
        }
        return Response.status(401).build();
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    //@Produces(MediaType.APPLICATION_JSON)
    public Response login(UserDto dto){
        try {

            if(logic.isValidUser(dto)){
                return Response.ok(generateToken(dto.getUsername())).build();
            }
        } catch (Exception e) {
            Logger.getAnonymousLogger().info(e.toString());
            e.printStackTrace();
        }
        return Response.status(401).build();
    }

    @GET
    @Path("reset")
    public Response reset(){
        logic.reset();
        return Response.ok().build();
    }

    @GET
    @Path("init")
    public void init(){
        logic.insertDummyData();
    }

    @GET
    @Path("info")
    @PermitAll
    public Response getInfo(@Context SecurityContext ctx) {
        Principal caller = ctx.getUserPrincipal();
        String name = caller == null ? "anonymous" : caller.getName();
        boolean hasJWT = jwt.getClaimNames() != null;
        String result = String.format("user: %s, isSecure: %s, hasJWT: %s", name, ctx.isSecure(), hasJWT);
        return Response.ok(result).build();
    }

    //todo create call for get all groups

    @POST
    @Path("createGroup")
    @Consumes(MediaType.APPLICATION_JSON)
    //@Produces(MediaType.APPLICATION_JSON)
    public Response createGroup(String groupName){

        System.out.println("test");

        logic.createRoom(groupName);
        return Response.ok().build();
    }

    @POST
    @Path("createMembership")
    @Consumes(MediaType.APPLICATION_JSON)
    //@Produces(MediaType.APPLICATION_JSON)
    public Response createMembership(MembershipDto dto){

        //System.out.println("hey there");
        logic.createMembership(dto);
        return Response.ok().build();
    }

    @GET
    @Path("getAllUsers")
    @Produces(MediaType.APPLICATION_JSON)
    public UserOutDto[] getAllUsers(){

        var users = logic.getAllUsers();

        return users;
    }

    @GET
    @Path("getAllGroups")
    @Produces(MediaType.APPLICATION_JSON)
    public Room[] getAllRooms(){
        var rooms = logic.getAllRooms();

        return rooms;
    }

    @GET
    @Path("getAllMemberships/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Room[] getAllMembershipRooms(@PathParam("username")String username){
        var rooms = logic.getAllRoomMemberships(username);
        return rooms;
    }

    @GET
    @Path("getAllMessages")
    @Produces(MediaType.APPLICATION_JSON)
    public MessageDto[] getAllMsg(){
        var res = logic.getAllMessages();
        return res;
    }

    //todo create call for getting all users

    //

    //region private Methods

    private String generateToken(String username) throws Exception {
        Map<String, Long> timeClaims = new HashMap<>();
        timeClaims.put(Claims.exp.name(), TokenUtils.currentTimeInSecs() + TOKEN_TIME);

        Map<String, Object> claims = new HashMap<>();
        claims.put(Claims.iss.name(), iss);
        claims.put(Claims.upn.name(), username);
        claims.put(Claims.groups.name(), Set.of(new String[]{"user"}));

        return TokenUtils.generateTokenString(claims, timeClaims);
    }



    //endregion

}

package org.acme;

import org.acme.controller.UserLogic;
import org.acme.dto.UserDto;
import org.acme.models.AppUser;
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

    @GET
    @Path("getAllUsers")
    @Produces(MediaType.APPLICATION_JSON)
    public AppUser[] getAllUsers(){

        var users = logic.getAllUsers();

        return users;
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

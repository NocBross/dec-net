package main.java.abstraction;

import java.io.ByteArrayInputStream;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class ProfileRDF {

    private static ProfileRDF rdfModel = null;

    private Model profileModel;
    private Property hasDirect;
    private Property hasFriend;
    private Property hasMember;
    private final String resourcePrefix = "http://www.myRDF.de/";


    private ProfileRDF() {
        profileModel = ModelFactory.createDefaultModel();
        hasDirect = profileModel.createProperty(resourcePrefix + "hasDirect");
        hasFriend = profileModel.createProperty(resourcePrefix + "hasFriend");
        hasMember = profileModel.createProperty(resourcePrefix + "hasMember");
    }


    public static ProfileRDF getInstance() {
        if(rdfModel == null) {
            rdfModel = new ProfileRDF();
        }

        return rdfModel;
    }


    public void addFriendToFriendshipGroup(String nickname, String friendshipGroup) {
        Resource friendshipGroupRes = profileModel.createResource(resourcePrefix + friendshipGroup);
        Resource friend = profileModel.createResource(resourcePrefix + nickname);
        friendshipGroupRes.addProperty(hasMember, friend);
    }


    public void addModel(ByteArrayInputStream stringReader) {
        profileModel.read(stringReader, null);
    }


    public void addNewDirectMessage(String userID, String nickname) {
        Resource user = profileModel.createResource(resourcePrefix + userID);
        Resource newDirectMessage = profileModel.createResource(resourcePrefix + nickname);
        user.addProperty(hasDirect, newDirectMessage);

        if(profileModel.contains(user, hasFriend, newDirectMessage)) {
            profileModel.remove(user, hasFriend, newDirectMessage);
        }
    }


    public void addNewFriend(String userID, String nickname) {
        Resource user = profileModel.createResource(resourcePrefix + userID);
        Resource newFriend = profileModel.createResource(resourcePrefix + nickname);
        user.addProperty(hasFriend, newFriend);

        if(profileModel.contains(user, hasDirect, newFriend)) {
            profileModel.remove(user, hasDirect, newFriend);
        }
    }


    public ResultSet getDirectMessages() {
        try {
            final String queryStringDirectMessages = "PREFIX dec-net: <" + resourcePrefix
                    + "> SELECT ?name ?nickname WHERE { ?name dec-net:hasDirect ?nickname . }";
            Query queryDirectMessages = QueryFactory.create(queryStringDirectMessages);
            QueryExecution qexec = QueryExecutionFactory.create(queryDirectMessages, profileModel);

            return qexec.execSelect();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public ResultSet getFriends() {
        try {
            final String queryStringFriends = "PREFIX dec-net: <" + resourcePrefix
                    + "> SELECT ?name ?nickname WHERE { ?name dec-net:hasFriend ?nickname . }";
            Query queryFriend = QueryFactory.create(queryStringFriends);
            QueryExecution qexec = QueryExecutionFactory.create(queryFriend, profileModel);

            return qexec.execSelect();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public ResultSet getFriendshipGroupsWithMembers() {
        try {
            final String queryStringFriendshipGroups = "PREFIX dec-net: <" + resourcePrefix
                    + "> SELECT ?friendshipGroup ?nickname WHERE { ?friendshipGroup dec-net:hasMember ?nickname . }";
            Query queryFriendshipGroups = QueryFactory.create(queryStringFriendshipGroups);
            QueryExecution qexec = QueryExecutionFactory.create(queryFriendshipGroups, profileModel);

            return qexec.execSelect();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void deleteDirectMessage(String userID, String nickname) {
        Resource user = profileModel.createResource(resourcePrefix + userID);
        Resource newDirectMessage = profileModel.createResource(resourcePrefix + nickname);
        profileModel.remove(user, hasDirect, newDirectMessage);
    }


    public void deleteFriend(String userID, String nickname) {
        Resource user = profileModel.createResource(resourcePrefix + userID);
        Resource friend = profileModel.createResource(resourcePrefix + nickname);
        profileModel.remove(user, hasFriend, friend);
    }


    public Model getModel() {
        return profileModel;
    }

}

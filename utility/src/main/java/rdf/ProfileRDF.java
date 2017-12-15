package main.java.rdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;

public class ProfileRDF {

    private Model profileModel;

    public ProfileRDF() {
        profileModel = ModelFactory.createDefaultModel();
        profileModel.setNsPrefix("foaf", FOAF.NS);
    }

    public void addFriend(String userID, String newFriend) {
        Resource user = generateUserResource(userID);
        String[] nicknameArray = newFriend.split("@");

        user.addProperty(FOAF.knows, "http://" + nicknameArray[1] + "/" + nicknameArray[0]);
    }

    public void addPost(String userID, String postID) {
        Resource user = generateUserResource(userID);
        user.addProperty(FOAF.publications, postID);
    }

    public void deleteFriend(String userID, String friendToDelete) {
        Resource user = generateUserResource(userID);
        String[] nicknameArray = friendToDelete.split("@");
        Literal friend = profileModel.createLiteral("http://" + nicknameArray[1] + "/" + nicknameArray[0]);

        profileModel.remove(user, FOAF.knows, friend);
    }

    public void deletePost(String userID, String postID) {
        Resource user = generateUserResource(userID);
        Literal post = profileModel.createLiteral(postID);

        profileModel.remove(user, FOAF.publications, post);
    }

    public List<String> getFriends() {
        LinkedList<String> friends = new LinkedList<String>();
        final String queryStringFriends = "PREFIX foaf: <" + FOAF.NS + "> SELECT ?name ?nickname WHERE { ?name foaf:"
                + FOAF.knows.getLocalName() + " ?nickname . }";
        Query queryFriend = QueryFactory.create(queryStringFriends);
        QueryExecution qexec = QueryExecutionFactory.create(queryFriend, profileModel);

        ResultSet result = qexec.execSelect();
        if (result != null) {
            while (result.hasNext()) {
                QuerySolution solution = result.next();
                String nickname = solution.getLiteral("nickname").toString().replace(FOAF.NS, "");
                String[] splittedNickname = nickname.split("/");
                nickname = splittedNickname[splittedNickname.length - 1] + "@"
                        + splittedNickname[splittedNickname.length - 2];
                friends.add(nickname);
            }
        }

        return friends;
    }

    public String getModel() {
        ByteArrayOutputStream stringWriter = new ByteArrayOutputStream();
        profileModel.write(stringWriter);
        return stringWriter.toString();
    }

    public String getName(String userID) {
        Resource user = generateUserResource(userID);
        return user.getProperty(FOAF.name).getLiteral().getString();
    }

    public List<String> getPosts() {
        LinkedList<String> posts = new LinkedList<String>();
        final String queryStringFriends = "PREFIX foaf: <" + FOAF.NS + "> SELECT ?name ?post WHERE { ?name foaf:"
                + FOAF.publications.getLocalName() + " ?post . }";
        Query queryFriend = QueryFactory.create(queryStringFriends);
        QueryExecution qexec = QueryExecutionFactory.create(queryFriend, profileModel);

        ResultSet result = qexec.execSelect();
        if (result != null) {
            while (result.hasNext()) {
                QuerySolution solution = result.next();
                String post = solution.getLiteral("post").toString().replace(FOAF.NS, "");
                posts.add(post);
            }
        }

        return posts;
    }

    public void setModel(String serializedModel) {
        ByteArrayInputStream stringReader = new ByteArrayInputStream(serializedModel.getBytes());
        profileModel.read(stringReader, null);
    }

    public void setName(String userID, String name) {
        Resource user = generateUserResource(userID);
        user.removeAll(FOAF.name);
        user.addProperty(FOAF.name, name);
    }

    private Resource generateUserResource(String userID) {
        String[] userArray = userID.split("@");
        return profileModel.createResource("http://" + userArray[1] + "/" + userArray[0], FOAF.Person);
    }

}

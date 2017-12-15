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
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;

public class PostRDF {

    public final String siocPrefix;
    private Model postModel;
    private Property account;
    private Property content;
    private Property creator;
    private Property hasReply;
    private Resource post;

    public PostRDF() {
        siocPrefix = "http://rdfs.org/sioc/spec/";
        postModel = ModelFactory.createDefaultModel();
        postModel.setNsPrefix("foaf", FOAF.NS);
        postModel.setNsPrefix("sioc", siocPrefix);

        account = postModel.createProperty(FOAF.NS + "account");
        content = postModel.createProperty(siocPrefix + "content");
        creator = postModel.createProperty(siocPrefix + "has_creator");
        hasReply = postModel.createProperty(siocPrefix + "has_reply");
        post = postModel.createResource(siocPrefix + "Post");
    }

    public void addReply(String commentURI) {
        post.addLiteral(hasReply, commentURI);
    }

    public String getContent() {
        return post.getProperty(content).getLiteral().getString();
    }

    public String getCreator() {
        return post.getProperty(creator).getResource().getProperty(account).getLiteral().getString();
    }

    public String getModel() {
        ByteArrayOutputStream stringWriter = new ByteArrayOutputStream();
        postModel.write(stringWriter);
        return stringWriter.toString();
    }

    public List<String> getReplys() {
        LinkedList<String> replys = new LinkedList<String>();
        final String queryStringFriends = "PREFIX sioc: <" + siocPrefix + "> SELECT ?post ?reply WHERE { ?post sioc:"
                + hasReply.getLocalName() + " ?reply . }";
        Query queryFriend = QueryFactory.create(queryStringFriends);
        QueryExecution qexec = QueryExecutionFactory.create(queryFriend, postModel);

        ResultSet result = qexec.execSelect();
        if (result != null) {
            while (result.hasNext()) {
                QuerySolution solution = result.next();
                String reply = solution.getLiteral("reply").toString();
                replys.add(reply);
            }
        }

        return replys;
    }

    public void setContent(String newContent) {
        post.removeAll(content);
        post.addLiteral(content, newContent);
    }

    public void setCreator(String userID) {
        Resource userAccount = postModel.createResource(siocPrefix + "UserAccount");
        userAccount.addLiteral(account, userID);
        post.removeAll(creator);
        post.addProperty(creator, userAccount);
    }

    public void setModel(String serializedModel) {
        ByteArrayInputStream stringReader = new ByteArrayInputStream(serializedModel.getBytes());
        postModel.read(stringReader, null);
    }

}

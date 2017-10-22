package main.java.webserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.RiotException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import main.java.message.RDFMessage;

public class ContextHandler implements HttpHandler {

    private String contextRoot;


    public ContextHandler(String contextRoot) {
        this.contextRoot = contextRoot;
    }


    @Override
    public void handle(HttpExchange httpExchange) {
        try {
            int status = 400;
            String response = "";
            String request = httpExchange.getRequestURI().toString();
            File resource = new File(contextRoot + request);

            if(httpExchange.getRequestMethod().equals("GET")) {
                if(resource.exists()) {
                    status = 200;
                    response = readRDF(resource);
                } else {
                    status = 404;
                    response = "<b>NOT FOUND</b>";
                }
            } else {
                writeRDF(resource, new BufferedReader(new InputStreamReader(httpExchange.getRequestBody())));
                status = 200;
                response = "<b>RDF SAVED</b>";
            }

            httpExchange.getResponseHeaders().add("Content-type", "text/html");
            httpExchange.sendResponseHeaders(status, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    private String readRDF(File resource) throws IOException {
        String model = "";
        String line = "";
        BufferedReader reader = new BufferedReader(new FileReader(resource));

        while((line = reader.readLine()) != null) {
            model += line;
        }

        reader.close();
        return new RDFMessage(resource.getPath().substring(8), model).getMessage();
    }


    private void writeRDF(File resource, BufferedReader reader) throws IOException {
        boolean fileWasCreated = false;
        String message = "";
        String line = "";

        if( !resource.exists()) {
            resource.getParentFile().mkdirs();
            resource.createNewFile();
            fileWasCreated = true;
        }

        while((line = reader.readLine()) != null) {
            message += line;
        }
        RDFMessage rdf = RDFMessage.parse(message);

        ByteArrayInputStream stringReader = new ByteArrayInputStream(rdf.getModel().getBytes());
        Model updatedModel = ModelFactory.createDefaultModel();
        updatedModel.read(stringReader, null);

        if(resource.exists() && !fileWasCreated) {
            FileInputStream fileInputStream = new FileInputStream(resource);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

            try {
                Model storedModel = ModelFactory.createDefaultModel();
                storedModel.read(bufferedInputStream, null);

                Model result = mergeModels(storedModel, updatedModel);
                updatedModel = result;
            } catch(RiotException re) {
                re.printStackTrace();
            }

            fileInputStream.close();
            bufferedInputStream.close();
        }

        FileOutputStream fileOutputStream = new FileOutputStream(resource);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        updatedModel.write(bufferedOutputStream);

        reader.close();
        stringReader.close();
        fileOutputStream.close();
        bufferedOutputStream.close();
    }


    private Model mergeModels(Model m1, Model m2) {
        Model deletedStatements = m1.difference(m2);
        StmtIterator iterator = deletedStatements.listStatements();
        while(iterator.hasNext()) {
            Statement node = iterator.nextStatement();
            m1.remove(node);
        }
        return m1.union(m2);
    }

}

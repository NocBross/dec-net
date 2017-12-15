package main.java.client_agent.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import main.java.constants.Network;
import main.java.message.RDFMessage;

public class ResourceController {

    private String contextRoot;
    private Lock resourceLock;

    public ResourceController(String contextRoot) {
        if (contextRoot.indexOf("/") == -1) {
            this.contextRoot = contextRoot + "/";
        } else {
            this.contextRoot = contextRoot;
        }
        resourceLock = new ReentrantLock();
    }

    public synchronized RDFMessage getResource(String resourceID) {
        resourceLock.lock();

        try {
            String content = "";
            String line = "";
            File resource = new File(contextRoot + resourceID.replace(Network.NETWORK_PROTOCOL, ""));

            if (!resource.exists()) {
                resourceLock.unlock();
                return null;
            }

            BufferedReader reader = new BufferedReader(new FileReader(resource));
            while ((line = reader.readLine()) != null) {
                content += line;
            }
            reader.close();

            resourceLock.unlock();
            return new RDFMessage(resourceID, content);
        } catch (Exception e) {
            e.printStackTrace();
            resourceLock.unlock();
            return null;
        }
    }

    public synchronized void updateResource(RDFMessage message) {
        resourceLock.lock();

        if (message != null) {
            BufferedReader reader = null;
            BufferedOutputStream bufferedOutputStream = null;
            try {
                boolean fileWasCreated = false;
                File resource = new File(contextRoot + message.getResourceID().replace(Network.NETWORK_PROTOCOL, ""));

                ByteArrayInputStream stringReader = new ByteArrayInputStream(message.getModel().getBytes());
                Model updatedModel = ModelFactory.createDefaultModel();
                updatedModel.read(stringReader, null);

                if (!resource.exists()) {
                    resource.getParentFile().mkdirs();
                    resource.createNewFile();
                    fileWasCreated = true;
                }

                if (!fileWasCreated) {
                    Model storedModel = ModelFactory.createDefaultModel();
                    reader = new BufferedReader(new FileReader(resource));
                    storedModel.read(reader, null);
                    updatedModel = mergeModels(storedModel, updatedModel);
                }

                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(resource));
                updatedModel.write(bufferedOutputStream);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }

                if (bufferedOutputStream != null) {
                    try {
                        bufferedOutputStream.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
        }

        resourceLock.unlock();
    }

    private Model mergeModels(Model m1, Model m2) {
        Model deletedStatements = m1.difference(m2);
        StmtIterator iterator = deletedStatements.listStatements();
        while (iterator.hasNext()) {
            Statement node = iterator.nextStatement();
            m1.remove(node);
        }
        return m1.union(m2);
    }

}

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
import main.java.service.CustomLogger;

public class ResourceController {

    private String contextRoot;
    private String logID;
    private Lock resourceLock;
    private CustomLogger logger;


    public ResourceController(String contextRoot, CustomLogger logger) {
        if(contextRoot.indexOf("/") == -1) {
            this.contextRoot = contextRoot + "/";
        } else {
            this.contextRoot = contextRoot;
        }
        logID = "ResourceController";
        resourceLock = new ReentrantLock();
        this.logger = logger;
    }


    public synchronized RDFMessage getResource(String resourceID) {
        logger.writeLog(logID + " reading " + resourceID, null);
        resourceLock.lock();

        try {
            String content = "";
            String line = "";
            File resource = new File(contextRoot + resourceID.replace(Network.NETWORK_PROTOCOL, ""));

            if( !resource.exists()) {
                resourceLock.unlock();
                return null;
            }

            BufferedReader reader = new BufferedReader(new FileReader(resource));
            while((line = reader.readLine()) != null) {
                content += line;
            }
            reader.close();

            resourceLock.unlock();
            return new RDFMessage(resourceID, content);
        } catch(Exception e) {
            logger.writeLog(logID + " error while reading", e);
            resourceLock.unlock();
            return null;
        }
    }


    public synchronized void updateResource(RDFMessage message) {
        logger.writeLog(logID + " writing " + message.getResourceID(), null);
        resourceLock.lock();

        if(message != null) {
            BufferedReader reader = null;
            BufferedOutputStream bufferedOutputStream = null;
            try {
                boolean fileWasCreated = false;
                File resource = new File(contextRoot + message.getResourceID().replace(Network.NETWORK_PROTOCOL, ""));

                ByteArrayInputStream stringReader = new ByteArrayInputStream(message.getModel().getBytes());
                Model updatedModel = ModelFactory.createDefaultModel();
                updatedModel.read(stringReader, null);

                if( !resource.exists()) {
                    resource.getParentFile().mkdirs();
                    resource.createNewFile();
                    fileWasCreated = true;
                }

                if( !fileWasCreated) {
                    Model storedModel = ModelFactory.createDefaultModel();
                    reader = new BufferedReader(new FileReader(resource));
                    storedModel.read(reader, null);
                    updatedModel = mergeModels(storedModel, updatedModel);
                }

                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(resource));
                updatedModel.write(bufferedOutputStream);
            } catch(Exception e) {
                logger.writeLog(logID + " error while reading", e);
            } finally {
                if(reader != null) {
                    try {
                        reader.close();
                    } catch(IOException ioe) {
                        logger.writeLog(logID + " cannot close reader", ioe);
                    }
                }

                if(bufferedOutputStream != null) {
                    try {
                        bufferedOutputStream.close();
                    } catch(IOException ioe) {
                        logger.writeLog(logID + " cannot close output stream", ioe);
                    }
                }
            }
        }

        resourceLock.unlock();
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

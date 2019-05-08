package eu.hopu.activage;

import eu.hopu.activage.metadataStorageClient.MetadataOkHttpClient;
import eu.hopu.activage.metadataStorageClient.MetadataStorageClient;
import eu.hopu.activage.services.DeploymentManagerService;
import eu.hopu.activage.services.DeploymentManagerServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Initializer implements ServletContextListener {

    private static final Logger LOG = LogManager.getLogger(Initializer.class);


    public static DeploymentManagerService service;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
//        TODO dockerizar y poder cambiar log properties dentro de docker
        LOG.info("Initializing deployment manager");
        String metadataStorageServerUrl = System.getenv("METADATA_STORAGE_SERVER_URL") != null ? System.getenv("METADATA_STORAGE_SERVER_URL") : "http://localhost:8081";

        LOG.debug("METADATA_STORAGE_SERVER_URL → {}", metadataStorageServerUrl);

        MetadataStorageClient client = new MetadataOkHttpClient(metadataStorageServerUrl);
        service = new DeploymentManagerServiceImpl(client);

        LOG.info("Deployment manager initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOG.info("Stopping deployment manager");
        LOG.info("Deployment manager stopped");
    }

}

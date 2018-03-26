package server.configuration;

import org.apache.catalina.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.LinkedList;

@Configuration
public class TomcatConfiguration {
    private final Logger logger = LoggerFactory.getLogger(TomcatConfiguration.class);

    @Value("${http.servers}")
    private String httpServers;

    @Value("${server.port}")
    private int localHostPort;


    @Bean
    public TomcatServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        Connector[] additionalConnectors = this.additionalConnector();
        if (additionalConnectors != null && additionalConnectors.length > 0) {
            tomcat.addAdditionalTomcatConnectors(additionalConnectors);
        }
        return tomcat;
    }

    private Connector[] additionalConnector() {

        if(StringUtils.isEmpty(httpServers)) {
            return null;
        }

        String[] serverConfigsSplit = httpServers.split(",");
        LinkedList<Connector> connctors = new LinkedList<>();

        for(String serverConfigRaw : serverConfigsSplit) {
            String[] serverConfig = serverConfigRaw.split(";");
            if(serverConfig.length != 3) {
                continue;
            }
            Connector connector = new Connector();
            connector.setPort(Integer.valueOf(serverConfig[2]));
            connector.setScheme("http");
            connctors.add(connector);
            logger.info("Listening for HTTP connections on port " + serverConfig[2]);
        }

        return connctors.toArray((new Connector[]{}));
    }
}

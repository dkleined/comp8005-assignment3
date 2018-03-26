package server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;
import server.client.ssh.SshClient;
import server.configuration.TomcatConfiguration;
import server.service.SocketService;

@SpringBootApplication
@Import(TomcatConfiguration.class)
public class Main {

    @Autowired
    private SshClient sshClient;

    @Autowired
    private SocketService socketService;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

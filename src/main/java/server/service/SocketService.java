package server.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import server.client.ssh.SocketClient;
import server.model.ConnectionInformation;

@Service
public class SocketService {

    public SocketService(@Value("${socket.servers}") String serverConfigs) {
        String[] serverConfigsSplit = serverConfigs.split(",");

        for(String serverConfigRaw : serverConfigsSplit) {
            String[] serverConfig = serverConfigRaw.split(";");
            SocketClient socketClient = new SocketClient(
                    Integer.valueOf(serverConfig[2]),
                    new ConnectionInformation(serverConfig[0], Integer.valueOf(serverConfig[1]))
            );
            socketClient.connect();

        }
    }
}

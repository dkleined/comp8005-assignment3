package server.client.ssh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.model.ConnectionInformation;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketClient  {
    private final Logger logger = LoggerFactory.getLogger(SocketClient.class);
    private int listenPort;
    private ConnectionInformation connectionInformation;
    private ServerSocket serverSocket;

    public SocketClient(int listenPort, ConnectionInformation connectionInformation) {
        this.listenPort = listenPort;
        this.connectionInformation = connectionInformation;
    }

    public void connect() {

        try {
            this.serverSocket = new ServerSocket(this.listenPort);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Socket serverWorkerSocket;
        Socket outgoingSocket;

        while (true) {
            try {
                logger.info("Listening for socket connections on port " + listenPort);
                serverWorkerSocket = serverSocket.accept();
                outgoingSocket = new Socket(connectionInformation.getHost(), connectionInformation.getPort());
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            new Thread(new SocketWorkerRunable(serverWorkerSocket, outgoingSocket)).run();
        }
    }


    private class SocketWorkerRunable implements Runnable {

        private Socket outgoingSocket;
        private Socket serverSocket;

        SocketWorkerRunable(Socket serverSocket, Socket outgoingSocket) {
            this.outgoingSocket = outgoingSocket;
            this.serverSocket = serverSocket;
        }

        @Override
        public void run() {
            try {
                if(serverSocket.getInputStream().available() > 0) {
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int count;
                        try {
                            while ((count = serverSocket.getInputStream().read(buffer)) > 0) {
                                outgoingSocket.getOutputStream().write(buffer, 0, count);
                            }
                            outgoingSocket.getOutputStream().flush();
                        } catch (Exception e) {
                            outgoingSocket.close();
                            serverSocket.close();
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}



package server.client.ssh;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class SshClient {
    private final Logger logger = LoggerFactory.getLogger(SshClient.class);

    public SshClient(@Value("${ssh.dst.host}") String dstHost,
                     @Value("${ssh.dst.port}") int dstPort,
                     @Value("${ssh.dst.username}") String username,
                     @Value("${ssh.dst.password}") String password,
                     @Value("${ssh.src.port}") int srcPort) throws JSchException {

        logger.info("Attempting to establish SSH forwarding with host " + dstHost + "...");
        UserInfo userInfo = new CustomUserInfo(password);
        JSch jSch = new JSch();
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        Session session = jSch.getSession(username, dstHost, dstPort);
        session.setUserInfo(userInfo);
        session.setConfig(config);
        session.connect();
        session.setPortForwardingL(srcPort, dstHost, dstPort);
        logger.info("Listening for SSH on port " + srcPort);
    }


}

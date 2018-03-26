package server.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import server.model.ConnectionInformation;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HttpController {

    private final Map<Integer, ConnectionInformation> connectionMap = new HashMap<>();
    private final RestTemplate restTemplate;

    public HttpController(@Value("${http.servers}") String serverConfigs,
                          RestTemplate restTemplate) {

        this.restTemplate = restTemplate;

        String[] serverConfigsSplit = serverConfigs.split(",");

        for(String serverConfigRaw : serverConfigsSplit) {
            String[] serverConfig = serverConfigRaw.split(";");
            if(serverConfig.length != 3) {
                continue;
            }
            ConnectionInformation connectionInformation = new ConnectionInformation(serverConfig[0], Integer.valueOf(serverConfig[1]));
            connectionMap.put(Integer.valueOf(serverConfig[2]), connectionInformation);
        }
    }

    @RequestMapping("/*")
    private ResponseEntity<Object> forward(HttpServletRequest request) {
        int requestPort = request.getServerPort();
        HttpHeaders headers = new HttpHeaders();

        // this is so other servers don't think we're a bot
        headers.put("user-agent", Collections.singletonList("Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.0.7) Gecko/2009021910 Firefox/3.0.7"));

        URI uri = UriComponentsBuilder
                .fromHttpUrl("http://" + connectionMap.get(requestPort).getHost() + request.getRequestURI() + "?" + request.getQueryString())
                .build()
                .toUri();
        HttpEntity entity = new HttpEntity<String>(headers);
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
    }

}

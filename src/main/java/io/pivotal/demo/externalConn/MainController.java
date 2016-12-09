package io.pivotal.demo.externalConn;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * Created by jaguilar on 12/9/16.
 */
@Controller
public class MainController {

    @RequestMapping("/")
    public String index(Model model) {
        RestTemplate template = new RestTemplate();
        ResponseErrorHandler responseErrorHandler = new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response)
                    throws IOException {
                System.out.println("Error - code <" + response.getRawStatusCode() + "> message <" + response.getStatusText() + ">");
            }
        };
        template.setErrorHandler(responseErrorHandler);
        ResponseEntity<String> content = template.getForEntity("https://httpbin.org/get",String.class);
        if (content.getStatusCodeValue() > 208) {
            model.addAttribute("content", "AN ERROR HAS OCCURRED CALLING THE EXTERNAL SERVICE: - code <" + content.getStatusCode() + "> message <" + content.getBody() + ">");
        }
        else {
            model.addAttribute("content", content.getBody());
        }
        return "index";
    }
}

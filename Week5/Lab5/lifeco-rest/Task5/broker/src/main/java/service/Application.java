package service;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

    final static CloseableHttpClient httpClient = HttpClients.createDefault();
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args); 
    }

    @Override
    public void run(String... args) throws Exception {
        HttpPost request = new HttpPost("http://localhost:8083/services");
        for (String string : args) {
            request.setEntity((HttpEntity) new StringEntity(string, ContentType.TEXT_PLAIN));
            CloseableHttpResponse response = httpClient.execute(request);
            if (response.getCode() != 201) {
                System.out.println("Error: " + response.getCode() + " " + response.getReasonPhrase());
            } 
        }
    }
}
package service.controllers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import service.core.Application;
import service.core.ClientInfo;
import service.core.Quotation;

@RestController
public class BrokerController {
    @Value("${server.port}")
    private int port;
    private ArrayList<Application> applications = new ArrayList<>();
    private Application application;

    String endpoints[] = { 
        "http://auldfellas:8080/quotations",
        "http://girlsallowed:8081/quotations",
        "http://dodgygeezers:8082/quotations"
    };

    private List<String> newEndpoints = new ArrayList<>(); // list of endpoints added from command line

    private String getHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress() + ":" + port; 
        } catch (UnknownHostException e) {
            return "localhost:" + port;
        }
    }

    @PostMapping(value="/applications", consumes = "application/json")
    public ResponseEntity<Application> getQuotations(@RequestBody ClientInfo info) {
        RestTemplate template = new RestTemplate();
        application = new Application(info);

        for(String endpoint : newEndpoints) {
            try {
                System.out.println("Sending request to: " + endpoint);
                ResponseEntity<String> response = template.postForEntity(endpoint, info, String.class);
                String location = response.getHeaders().getLocation().toString();
                ResponseEntity<Quotation> quotation = template.getForEntity(location, Quotation.class);
                application.quotations.add(quotation.getBody());
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
        applications.add(application); // add to list of applications
        return ResponseEntity.status(HttpStatus.CREATED)
        .header("Location", newEndpoints.toString())
        .header("Content-Location", newEndpoints.toString())
        .body(application);
    }

    @GetMapping(value="/applications", produces = "application/json")
    public ResponseEntity<ArrayList<String>> getApplications() {
        ArrayList<String> list = new ArrayList<>();
        for(Application application : applications) {
            list.add("http://" + getHost() + "/applications/" + application.id);
        }
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping(value="/applications/{id}", produces = "application/json")
    public ResponseEntity<Application> getApplication(@PathVariable int id) {
        for(Application application : applications) {
            if(application.id == id) {
                return ResponseEntity.status(HttpStatus.OK).body(application);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping(value="/services", consumes = "text/plain")
    public ResponseEntity<String> addService(@RequestBody String endpoint) {
        for(String service : newEndpoints) {
            if(service.equals(endpoint)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Service already exists");
            }
        }
        newEndpoints.add(endpoint);
        return ResponseEntity.status(HttpStatus.CREATED).body("Service added: " + endpoint);
    }

    @GetMapping(value="/services", produces = "application/json")
    public ResponseEntity<ArrayList<String>> getServices() {
        ArrayList<String> list = new ArrayList<>();
        for(String service : newEndpoints) {
            list.add(service);
        }
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

}

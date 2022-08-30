package me;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.lang.Integer.parseInt;
import static org.springframework.http.MediaType.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class MapController {

    @Autowired
    ForgetfulMap forgetfulMap;

    @GetMapping(value = "/status", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> status() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(APPLICATION_JSON);
        ResponseEntity<String> responseEntity = new ResponseEntity("{\"status\":\"OK\"}",
                httpHeaders,
                HttpStatus.OK);

        return responseEntity;
    }

    @GetMapping(value = "/get-whole-map", produces = TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getMap() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(APPLICATION_JSON);
        StringBuilder sb = new StringBuilder(forgetfulMap.fractionFilled())
                .append("\n")
                .append(forgetfulMap.getMapOfContent());
        ResponseEntity<String> responseEntity = new ResponseEntity(sb.toString(),
                httpHeaders,
                HttpStatus.OK);

        return responseEntity;
    }

    @PostMapping(value = "/create-map", produces = TEXT_PLAIN_VALUE)
    public ResponseEntity<String> createMap(@RequestParam String size) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(TEXT_PLAIN);

        ResponseEntity<String> responseEntity;
        try {
            Integer mapSize = parseInt(size);
            forgetfulMap.createForgetfulMap(mapSize);

            responseEntity = new ResponseEntity("previous map was reset\nnew map was created",
                    httpHeaders,
                    HttpStatus.CREATED);
        } catch (NumberFormatException e) {
            responseEntity = new ResponseEntity("invalid map size",
                    httpHeaders,
                    HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

    @GetMapping(value = "/find-in-map", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getFromMap(@RequestParam(required = false) String search,
                                             @RequestParam(required = false) String key) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(APPLICATION_JSON);
        ResponseEntity<String> responseEntity;

        if (search == "full") {
            responseEntity = new ResponseEntity(
                    forgetfulMap.getMapOfContent(),
                    httpHeaders,
                    HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity(
                    forgetfulMap.find(key),
                    httpHeaders,
                    HttpStatus.OK);
        }

        return responseEntity;
    }

    @PutMapping(value = "/put-in-map", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> putInMap(@RequestParam String key, @RequestParam String content) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(APPLICATION_JSON);

        forgetfulMap.add(key, content);

        ResponseEntity<String> responseEntity = new ResponseEntity(String.format("{\"%s\":\"%s\"}", key, content),
                httpHeaders,
                HttpStatus.CREATED);

        return responseEntity;
    }

}

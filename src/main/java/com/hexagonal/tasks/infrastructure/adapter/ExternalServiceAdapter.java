package com.hexagonal.tasks.infrastructure.adapter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.hexagonal.tasks.domain.model.AdditionalTaskInfo;
import com.hexagonal.tasks.domain.port.out.ExternalServicePort;

import lombok.Getter;
import lombok.Setter;

public class ExternalServiceAdapter implements ExternalServicePort {


    private final RestTemplate restTemplate;

    

    public ExternalServiceAdapter() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public AdditionalTaskInfo getAdditionalTaskInfo(Long id) {
        String apiUrl = "http://jsonplaceholder.typicode.com/todos/" + id;
        ResponseEntity<JsonPlaceHolderTodo> response= restTemplate.getForEntity(apiUrl, JsonPlaceHolderTodo.class);
        JsonPlaceHolderTodo todo= response.getBody();

        if(todo == null){
            return null;
        }

        apiUrl = "http://jsonplaceholder.typicode.com/users/"  + todo.getUserId();
        ResponseEntity<JsonPlaceHolderUser> userResponse = restTemplate.getForEntity(apiUrl, JsonPlaceHolderUser.class);
        JsonPlaceHolderUser user = userResponse.getBody();
        
        if(user == null){
            return null;
        }

        return new AdditionalTaskInfo(user.getId(), user.getName(), user.getEmail());
    }


    @Getter
    @Setter
    private static class JsonPlaceHolderTodo{
        private Long id;
        private Long userId;

    }

    @Getter
    @Setter
    private static class JsonPlaceHolderUser{
        private Long id;
        private String name;
        private String email;

    }

    
}

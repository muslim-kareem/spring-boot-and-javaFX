package de.fronted.frontendjavafxspringboot.todo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;

@Service
public class TodoService {

    private final WebClient webClient =  WebClient.create("http://localhost:8080/api/todo");

    public List<Todo> getTodos() {
        return Objects.requireNonNull(webClient.get()
                .retrieve()
                .toEntityList(Todo.class)
                .block()).getBody();
    }

}

package de.fronted.frontendjavafxspringboot.todo;

import org.springframework.http.HttpMethod;
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
    public void deleteTodoById(String id) {
        webClient.delete()
                .uri("/"+id)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe(response -> {
                    System.out.println("Delete request successful");
                }, error -> {
                    System.err.println("Error occurred during delete request: " + error.getMessage());
                });
    }


    public void createTodo(Todo theTodo) {
         Objects.requireNonNull(webClient.post()
                 .bodyValue(theTodo)
                 .retrieve()
                 .toEntityList(Todo.class)
                 .block());
    }

}

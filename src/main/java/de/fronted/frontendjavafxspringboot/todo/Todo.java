package de.fronted.frontendjavafxspringboot.todo;

import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    private String id;
    private String description;
    private TodoStatus status;

    public void setStatusAsString(String status ){
        if(status.equals(TodoStatus.OPEN.toString())){
            this.setStatus(TodoStatus.OPEN);
        } else if (status.equals(TodoStatus.DONE.toString())){
            this.setStatus(TodoStatus.DONE);
        }else {
            this.setStatus(TodoStatus.IN_PROGRESS);
        }

    }
}

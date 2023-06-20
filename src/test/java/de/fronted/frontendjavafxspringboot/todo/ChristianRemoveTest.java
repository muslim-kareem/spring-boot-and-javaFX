package de.fronted.frontendjavafxspringboot.todo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChristianRemoveTest {

    @Test
    void removeItem() {
        Todo[] theTodos = {new Todo("1","test", TodoStatus.DONE)};
        ChristianRemove testToRemove = new ChristianRemove(theTodos);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {testToRemove.removeItem(new Todo());});
    }
}
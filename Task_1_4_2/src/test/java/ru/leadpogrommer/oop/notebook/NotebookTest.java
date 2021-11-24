package ru.leadpogrommer.oop.notebook;

import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class NotebookTest {
    private static Notebook generateNotebook() {
        var nb = new Notebook();
        nb.add("1", "trtgy");
        nb.add("2 mark", "wregthy");
        nb.add("3", "waeerh");
        nb.add("4 mark", "wrethe5y");
        nb.add("5", "wrtg4wergth");
        nb.add("6", "waretwhth");
        nb.add("7", "qwreht64");
        nb.add("8 mark", "wrethy57");
        nb.add("9", "Lorem ipsum");

        return nb;
    }


    @Test()
    void load_save() {
        var nb = generateNotebook();

        var writer = new StringWriter();
        nb.save(writer);
        var reader = new StringReader(writer.toString());

        var new_nb = new Notebook();
        new_nb.load(reader);

        assertThat(new_nb.getNotes()).containsExactlyElementsOf(nb.getNotes());
    }


    @Test
    void remove() {
        var nb = generateNotebook();
        assertThat(nb.getNotes()).anyMatch(t -> t.title().equals("9"));
        nb.remove("9");
        assertThat(nb.getNotes()).noneMatch(t -> t.title().equals("9"));
        assertThat(nb.getNotes().size()).isEqualTo(8);
    }

    @Test
    void getNotes() {
        var nb = generateNotebook();
        assertThat(nb.getNotes().size()).isEqualTo(9);
        assertThat(nb.getNotes(Instant.MIN, Instant.MAX, new String[]{"1"}).size()).isEqualTo(1);
        assertThat(nb.getNotes(Instant.MIN, Instant.MAX, new String[]{"mark"}).size()).isEqualTo(3);
        assertThat(nb.getNotes(Instant.MIN, Instant.MAX, new String[]{"1", "mark"}).size()).isEqualTo(4);
    }
}
package ru.leadpogrommer.oop.notebook;

import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class NotebookCLITest {
    @Test
    void cli() {
        var nb = new Notebook();
        var cli = new NotebookCLI(nb);
        var executor = new CommandLine(cli)
                .registerConverter(Date.class, s -> new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(s));
        executor.execute("add", "First m", "textbewr5trebtt5");
        executor.execute("add", "Bad", "textbrebttewthyw5");
        executor.execute("add", "Second m", "textbrebtt5e5h4");
        executor.execute("add", "Third k", "textbrebtt5erh5r6");
        executor.execute("rm", "Bad");

        assertThat(nb.getNotes().size()).isEqualTo(3);

        var stream = new ByteArrayOutputStream();

        System.setOut(new PrintStream(stream));
        executor.execute("show");
        assertThat(stream.toString()).contains("First");
        assertThat(stream.toString()).contains("Second");
        assertThat(stream.toString()).contains("Third");
    }
}
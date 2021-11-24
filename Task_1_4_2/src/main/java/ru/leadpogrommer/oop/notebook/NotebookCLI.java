package ru.leadpogrommer.oop.notebook;


import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;


@Command()
public class NotebookCLI implements Callable<Integer> {
    private final Notebook notebook;
    private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    NotebookCLI(Notebook nb) {
        notebook = nb;
    }

    public static void main(String[] args) {
        var notebook = new Notebook();
        try (var reader = new FileReader("notebook.json")) {
            notebook.load(reader);
        } catch (IOException ignored) {
        }


        new CommandLine(new NotebookCLI(notebook))
                .registerConverter(Date.class, s -> new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(s))
                .execute(args);


        try {
            var writer = new FileWriter("notebook.json");
            notebook.save(writer);
            writer.close();
        } catch (IOException e) {
            System.out.println("Unable to save notebook: " + e.getMessage());
        }
    }

    @Override
    public Integer call() {
        System.out.println("Fuck");
        return 0;
    }

    @Command(name = "show")
    public Integer show() {
        notebook.getNotes().forEach(this::printNote);
        return 0;
    }

    @Command(name = "add")
    public Integer add(@Parameters(paramLabel = "TITLE") String title, @Parameters(paramLabel = "TEXT") String text) {
        notebook.add(title, text);
        return 0;
    }

    @Command(name = "rm")
    public Integer rm(@Parameters(paramLabel = "TITLE") String title) {
        notebook.remove(title);
        return 0;
    }

    @Command(name = "filter")
    public Integer filter(
            @Parameters(paramLabel = "FROM", index = "0") Date after,
            @Parameters(paramLabel = "TO", index = "1") Date before,
            @Parameters(paramLabel = "KEYWORDS", index = "2..*") String[] keywords
    ) {
        if (keywords == null) keywords = new String[]{""};
        notebook.getNotes(after.toInstant(), before.toInstant(), keywords).forEach(this::printNote);
        return 0;
    }

    private void printNote(Note note) {
        System.out.println("Title: " + note.title());
        System.out.println("Date: " + dateFormat.format(Date.from(note.timestamp())));
        System.out.println(note.text());
        System.out.println();
    }


}

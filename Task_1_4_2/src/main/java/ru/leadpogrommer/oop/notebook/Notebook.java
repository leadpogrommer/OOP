package ru.leadpogrommer.oop.notebook;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;
import java.io.Writer;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Notebook {
    private final Gson gson;
    private List<Note> notes = new ArrayList<>();

    Notebook() {
        gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, (JsonDeserializer<Instant>) (json, typeOfT, context) -> Instant.ofEpochMilli(json.getAsLong()))
                .registerTypeAdapter(Instant.class, (JsonSerializer<Instant>) (src, typeOfSrc, context) -> new JsonPrimitive(src.toEpochMilli()))
                .create();
    }

    public void load(Reader reader) {
        ArrayList<Note> loaded = gson.fromJson(reader, new TypeToken<ArrayList<Note>>() {
        }.getType());
        if (loaded != null) notes = loaded;
    }

    public void save(Writer writer) {
        gson.toJson(notes, writer);
    }

    public void add(String title, String text) {
        notes.add(new Note(Instant.now().truncatedTo(ChronoUnit.MILLIS), title, text));
    }

    public void remove(String title) {
        notes.removeIf(note -> note.title().equals(title));
    }

    public List<Note> getNotes() {
        return notes;
    }

    public List<Note> getNotes(Instant minDate, Instant maxDate, String[] keywords) {
        return notes.stream().filter((note) ->
                note.timestamp().isAfter(minDate) &&
                        note.timestamp().isBefore(maxDate) &&
                        Arrays.stream(keywords).anyMatch((keyword) -> note.title().toLowerCase().contains(keyword.toLowerCase()))
        ).collect(Collectors.toList());
    }

}

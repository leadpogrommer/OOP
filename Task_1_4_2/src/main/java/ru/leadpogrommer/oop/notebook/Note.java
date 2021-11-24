package ru.leadpogrommer.oop.notebook;

import java.time.Instant;


public final class Note {
    private final Instant timestamp;
    private final String title;
    private final String text;

    public Note(Instant timestamp, String title, String text) {
        this.timestamp = timestamp;
        this.title = title;
        this.text = text;
    }

    public Instant timestamp() {
        return timestamp;
    }

    public String title() {
        return title;
    }

    public String text() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return timestamp.compareTo(note.timestamp) == 0 && title.equals(note.title) && text.equals(note.text);
    }
}

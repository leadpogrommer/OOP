package ru.leadpogrommer.oop.notebook;

import java.util.Date;
import java.util.Objects;

public final class Note {
    public Date timestamp;
    public String title;
    public String text;

    public Note(Date timestamp, String title, String text) {
        this.timestamp = timestamp;
        this.title = title;
        this.text = text;
    }
}

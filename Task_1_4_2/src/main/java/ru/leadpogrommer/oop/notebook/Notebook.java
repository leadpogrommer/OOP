package ru.leadpogrommer.oop.notebook;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;

public class Notebook {
    private ArrayList<Note> notes = new ArrayList<>();

    public void load(Reader reader){
        var gson = new Gson();
        ArrayList<Note> loaded = gson.fromJson(reader, new TypeToken<ArrayList<Note>>(){}.getType());
        if(loaded != null)notes = loaded;
    }

    public String save(){
        var gson = new Gson();
        return gson.toJson(notes);
    }

    public void add(Note note){
        notes.add(note);
    }

    public void remove(String title){
        notes.removeIf(note -> note.text.equals(title));
    }

    public Collection<Note> getNotes(){
        return notes;
    }

    public Collection<Note> getNotes(Date minDate, Date maxDate, String[] keywords){
//        System.out.println(minDate);
//        for(var note: notes){
//            System.out.println(note.timestamp);
//        }
//        System.out.println(maxDate);
        return notes.stream().filter((note) ->
                        note.timestamp.after(minDate) &&
                        note.timestamp.before(maxDate) &&
                        Arrays.stream(keywords).anyMatch((keyword) -> note.title.toLowerCase().contains(keyword.toLowerCase()))
        ).collect(Collectors.toList());
    }

}

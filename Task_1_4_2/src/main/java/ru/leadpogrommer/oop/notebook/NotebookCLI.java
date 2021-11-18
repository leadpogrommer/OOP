package ru.leadpogrommer.oop.notebook;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


public class NotebookCLI {
    public static void main(String[] args) {
//        for(var arg: args){
//            System.out.println(arg);
//        }
        var notebook = new Notebook();
        try (var reader = new FileReader("notebook.json")){
            notebook.load(reader);
        } catch (IOException ignored){}


        if(args.length == 1 && args[0].equals("show")){
            notebook.getNotes().forEach((note -> {
                System.out.println("Title: " + note.title);
                System.out.println("Date: "+ note.timestamp.toString());
                System.out.println(note.text);
                System.out.println();
            }));
        }else if(args.length == 2 && args[0].equals("rm")){
            notebook.remove(args[1]);
        }else if(args.length == 3 && args[0].equals("add")){
            notebook.add(new Note(new Date(), args[1], args[2]));
        }else if(args.length >= 4 && args[0].equals("show")){
            var format = new SimpleDateFormat("dd:MM:yyyy HH:mm");
            try {
                var after = format.parse(args[1]);
                var before = format.parse(args[2]);
                notebook.getNotes(after, before, Arrays.copyOfRange(args, 3, args.length)).forEach((note -> {
                    System.out.println("Title: " + note.title);
                    System.out.println("Date: "+ note.timestamp.toString());
                    System.out.println(note.text);
                    System.out.println();
                }));
            }catch (ParseException e){
                System.out.println("Invalid args: "+e.getMessage());
            }
        }else {
            System.out.println("Invalid args");
        }


        try {
//            System.out.println(notebook.save());
            var writer = new FileWriter("notebook.json");
            writer.write(notebook.save());
            writer.close();
        } catch (IOException e) {
            System.out.println("Unable to save notebook: " + e.getMessage());
        }


    }


}

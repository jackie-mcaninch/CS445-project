package edu.iit.cs445.spring2022.buynothing;

import java.util.ArrayList;
import java.util.List;

public class Conversation {
    private String with_uid;
    private List<Note> notes;

    public Conversation(String with_uid) {
        this.with_uid = with_uid;
        this.notes = new ArrayList<Note>();
    }

    public void addNote(Note n) {
        this.notes.add(n);
    }

    public String getWithUID() {
        return this.with_uid;
    }

    public List<Note> getNotes() {
        return this.notes;
    }
}

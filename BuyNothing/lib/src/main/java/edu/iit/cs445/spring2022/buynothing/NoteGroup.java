package edu.iit.cs445.spring2022.buynothing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NoteGroup {
    private String uid;
    private String source_id;
    private List<Conversation> conversations;

    public NoteGroup() {} // for null note group

    public NoteGroup(String uid, String source_id) {
        this.uid = uid;
        this.source_id = source_id;
        this.conversations = new ArrayList<Conversation>();
    }

    public void addConversation(Conversation c) {
        this.conversations.add(c);
    }

    public String getUID() {
        return this.uid;
    }

    public String getSourceID() {
        return this.source_id;
    }

    public List<Conversation> getConversations() {
        return this.conversations;
    }

    public boolean userInNoteGroup(String uid) {
        Iterator<Conversation> convo_iter = conversations.listIterator();
        while (convo_iter.hasNext()) {
            Conversation c = convo_iter.next();
            if (c.getWithUID().equals(uid)) {
                return true;
            }
        }
        return false;
    }

    public boolean isNil() {
        return false;
    }
}

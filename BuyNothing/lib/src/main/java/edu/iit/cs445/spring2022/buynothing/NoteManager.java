package edu.iit.cs445.spring2022.buynothing;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class NoteManager {
	private static final String[] TO_TYPES = {"give", "ask", "note"};
	private static List<Note> allNotes = new ArrayList<Note>();
	
	public Note createNote(Note n) {
		checkMissingInfo(n);
		checkToType(n);
		Note newNote = new Note(n);
		allNotes.add(newNote);
		return newNote;
	}
	
	public void activateNote(String nid) {
		Note n = findByID(nid);
		if (n.isNil()) throw new NoSuchElementException();
		checkMissingInfo(n);
		n.activate();
	}
	
	public Note deactivateNote(String nid) {
		Note n = findByID(nid);
		if (n.isNil()) throw new NoSuchElementException();
		n.deactivate();
    	allNotes.remove(n);
    	return n;
	}
	
    public void updateNote(String old_id, Note nnew) {
    	Note nold = findByID(old_id);
    	if (nold.isNil() || !nold.getActiveStatus()) throw new NoSuchElementException();
    	checkMissingInfo(nnew);
    	checkToType(nnew);
    	nold.updateAccountID(nnew.getAccountID());
    	nold.updateToType(nnew.getToType());
    	nold.updateToUserID(nnew.getToUserID());
    	nold.updateToID(nnew.getToID());
    	nold.updateDescription(nnew.getDescription());
    }
    
    public void deleteNote(String nid) {
    	Note n = findByID(nid);
    	if (n.isNil()) throw new NoSuchElementException();
    	allNotes.remove(n);
    }
    
    public List<String> deleteByUID(String uid) {
    	Iterator<Note> note_iter = allNotes.listIterator();
    	List<String> deleted = new ArrayList<String>();
    	while (note_iter.hasNext()) {
    		Note n = note_iter.next();
    		if (n.getAccountID().equals(uid)) {
    			deleteNote(n.getID());
        		deleted.add(n.getID());
    		}
    	}
    	return deleted;
    }
    
    public void deleteByToID(String toid) {
    	// recursively delete all subordinated resources
    	Iterator<Note> note_iter = allNotes.listIterator();
    	List<String> deleted = new ArrayList<String>();
    	while (note_iter.hasNext()) {
    		Note n = note_iter.next();
    		if (n.getToID().equals(toid)) {
    			deactivateNote(n.getID());
        		deleted.add(n.getID());
    		}
    	}
    	Iterator<String> del_iter = deleted.listIterator();
    	while (del_iter.hasNext()) {
    		deleteByToID(del_iter.next());
    	}
    }
    
    public List<Note> viewAllNotes() {
    	return allNotes;
    }
    
    public List<Note> viewMyNotes(String uid, boolean isActive) {
    	List<Note> myNotes = new ArrayList<Note>();
    	Iterator<Note> note_iter = allNotes.listIterator();
    	while (note_iter.hasNext()) {
    		Note n = note_iter.next();
    		if (n.getAccountID().equals(uid) && n.getActiveStatus()==isActive) {
    			myNotes.add(n);
    		}
    	}
    	return myNotes;
    }
    
    public Note viewNote(String nid) {
    	Note n = findByID(nid);
    	if (n.isNil()) throw new NoSuchElementException();
    	return n;
    }
    
    public void clearAllNotes() {
    	allNotes.clear();
    }
    
    public List<Note> searchNotes(String key, String start_date, String end_date) {
    	if (key==null) return allNotes;
    	List<Note> filteredNotes = new ArrayList<Note>();
    	try {
    		Date start = new SimpleDateFormat("DD-MM-YYYY").parse(start_date);
    		Date end = new SimpleDateFormat("DD-MM-YYYY").parse(end_date);
    		Iterator<Note> note_iter = allNotes.listIterator();
        	while (note_iter.hasNext()) {
        		Note n = note_iter.next();
        		if (n.checkForKeyword(key)) {
        			Date created = n.getDateCreated();
        			if (!created.after(end) && !created.before(start)) {
        				filteredNotes.add(n);
        			}
        		}
        	}
        	return filteredNotes;
    	}
    	// Date cannot be parsed
    	catch (Exception e) {
    		throw new IllegalArgumentException();
    	}
    }
    
    public Note findByID(String nid) {
    	Iterator<Note> note_iter = allNotes.listIterator();
    	while (note_iter.hasNext()) {
    		Note n = note_iter.next();
    		if (n.matchesID(nid)) return n;
    	}
    	return (new NullNote());
    }
    
    public void checkToType(Note n) {
    	String myType = n.getToType();
    	for (String type : TO_TYPES) {
    		if (myType.equals(type)) return;
    	}
    	// specified type is not a valid identifier
    	throw new IllegalArgumentException();
    }
    
    public void checkMissingInfo(Note n) {
    	if (n.getAccountID()==null || 
    		n.getToType()==null ||
    		n.getToUserID()==null ||
    		n.getToID()==null ||
    		n.getDescription()==null) {
    			throw new AssertionError();
    	}
    }
    
    public String assessMissingInfo(String nid) {
    	Note n = findByID(nid);
		if (n.getAccountID()==null) return "Account ID is missing!";
		if (n.getToType()==null) return "Type is missing!";
		if (n.getToUserID()==null) return "Recipient account is missing!";
		if (n.getToID()==null) return "To ID field is missing!";
		if (n.getDescription()==null) return "Description is missing!";
		return "Something went wrong.";
    }
}

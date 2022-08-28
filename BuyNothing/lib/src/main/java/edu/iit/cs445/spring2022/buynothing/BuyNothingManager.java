package edu.iit.cs445.spring2022.buynothing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class BuyNothingManager implements BoundaryInterface {
	private static List<Account> allAccounts;
	private static List<Ask> allAsks;
	private static List<Give> allGives;
	private static List<Thank> allThanks;
	private static List<Note> allNotes;
	
	private static final String[] ASK_TYPES = {"gift", "borrow", "help"};
	private static final String[] GIVE_TYPES = {"gift", "service", "lend", "share"};
	private static final String[] NOTE_TO_TYPES = {"give", "ask", "note"};
	

	// ACCOUNT METHODS
	public void createDefaultAccounts() {
		allAccounts = new ArrayList<Account>();
		
		// set 3 default accounts
		Account default_account1 = new Account();
		default_account1.updateName("Virgil Bistriceanu");
		default_account1.updateAddress("10 West 31st ST", "60616");
		default_account1.updatePhone("123-456-7890");
		default_account1.updatePicture("<link to Virgil's picture>");
		
		Account default_account2 = new Account();
		default_account2.updateName("Jane Smith");
		default_account2.updateAddress("123 2nd ST", "60607");
		default_account2.updatePhone("987-654-3210");
		default_account2.updatePicture("<link to Jane's picture>");
		
		Account default_account3 = new Account();
		default_account3.updateName("CSR #1");
		default_account3.updateAddress("101 W Main St.", "60010");
		default_account3.updatePhone("123-867-5309");
		default_account3.updatePicture("<link to CSR's picture>");
		
		// add default accounts to collection and set virgil/csr1 is_active to true
		createAccount(default_account1).forceActivate();
		createAccount(default_account2);
		createAccount(default_account3).forceActivate();
	}
	
	public Account createAccount(Account a) {
		if (allAccounts == null) {
			createDefaultAccounts();
		}
		checkMissingAccountInfo(a);
		Account newAccount = new Account(a);
		allAccounts.add(newAccount);
		return newAccount;
	}
	
	public void activateAccount(String uid) {
		Account a = findAccountByID(uid);
		if (a.isNil()) throw new NoSuchElementException();
		checkMissingAccountInfo(a);
		a.activate();
	}
	
    public void updateAccount(String old_id, Account anew) {
    	Account aold = findAccountByID(old_id);
    	if (aold.isNil()) throw new NoSuchElementException();
    	checkMissingAccountInfo(anew);
    	aold.updateName(anew.getName());
    	aold.updateAddress(anew.getStreet(), anew.getZip());
    	aold.updatePhone(anew.getPhone());
    	aold.updatePicture(anew.getPicture());
    }
    
    public void deleteAccount(String uid) {
    	Account a = findAccountByID(uid);
    	if (a.isNil()) throw new NoSuchElementException();
		a.deactivate();
		allAccounts.remove(a);
		// delete all subordinate resources
		deleteAskByAccountID(a.getID());
		deleteGiveByAccountID(a.getID());
		deleteThankByAccountID(a.getID());
		deleteNoteByAccountID(a.getID());
    }
    
    public List<Account> viewAllAccounts() {
    	if (allAccounts == null) {
    		createDefaultAccounts();
    	}
    	return allAccounts;
    }
    
    public Account viewAccount(String uid) {
    	Account a = findAccountByID(uid);
    	if (a.isNil()) throw new NoSuchElementException();
    	return a;
    }
    
    public void clearAllAccounts() {
    	if (allAccounts == null) {
    		createDefaultAccounts();
    	}
        allAccounts.clear();
    }
    
    public List<Account> searchAccounts(String key, String start_date, String end_date) {
    	if (allAccounts == null) {
    		createDefaultAccounts();
    	}
    	if (key==null) return allAccounts;
    	List<Account> filteredAccounts = new ArrayList<Account>();
    	try {
    		Date start = new SimpleDateFormat("DD-MM-YYYY").parse(start_date);
    		Date end = new SimpleDateFormat("DD-MM-YYYY").parse(end_date);
    		if (!start.before(end)) throw new AssertionError();
    		Iterator<Account> acc_iter = allAccounts.listIterator();
        	while (acc_iter.hasNext()) {
        		Account a = acc_iter.next();
        		if (a.checkForKeyword(key)) {
        			Date created = new SimpleDateFormat("DD-MM-YYYY").parse(a.getdate_created());
        			if (!created.after(end) && !created.before(start)) {
        				filteredAccounts.add(a);
        			}
        		}
        	}
        	return filteredAccounts;
    	}
    	catch (ParseException e) {
    		throw new IllegalArgumentException();
    	}
    }
    
    public Account findAccountByID(String uid) {
    	if (allAccounts == null) {
    		createDefaultAccounts();
    	}
    	Iterator<Account> acc_iter = allAccounts.listIterator();
    	while (acc_iter.hasNext()) {
    		Account a = acc_iter.next();
    		if (a.matchesID(uid)) return a;
    	}
    	return (new NullAccount());
    }
    
    public void checkMissingAccountInfo(Account a) {
    	if (a.getName()==null || 
    		a.getStreet()==null ||
    		a.getZip()==null || 
    		a.getPhone()==null || 
    		a.getPicture()==null) {
    			throw new AssertionError();
    		}
    }
    
    public String assessMissingAccountInfo(Account a) {
		if (a.getName()==null) return "Name is missing!";
		if (a.getStreet()==null) return "Street is missing!";
		if (a.getZip()==null) return "Zip code is missing!";
		if (a.getPhone()==null) return "Phone number is missing!";
		if (a.getPicture()==null) return "Picture is missing!";
		return "Something went wrong.";
    }
    
    
    // ASK METHODS
    public Ask createAsk(Ask a) {
    	if (allAsks == null) {
			allAsks = new ArrayList<Ask>();
		}
		checkMissingAskInfo(a);
		checkAskType(a);
		Ask newAsk = new Ask(a);
		allAsks.add(newAsk);
		newAsk.activate();
		return newAsk;
	}
	
	public Ask deactivateAsk(String uid) {
		Ask a = findAskByID(uid);
		if (a.isNil()) throw new NoSuchElementException();
		a.deactivate();
    	allAsks.remove(a);
    	return a;
	}
	
    public void updateAsk(String old_id, Ask anew) {
    	Ask aold = findAskByID(old_id);
    	if (aold.isNil() || !aold.getActiveStatus()) throw new NoSuchElementException();
    	checkMissingAskInfo(anew);
    	checkAskType(anew);
    	aold.updateType(anew.getType());
    	aold.updateDescription(anew.getDescription());
    	SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
    	aold.updateStartDate(fmt.format(anew.getStartDate()));
    	aold.updateEndDate(fmt.format(anew.getEndDate()));
    	aold.updateExtraZip(anew.getExtraZip());
    }
    
    public void deleteAsk(String uid) {
    	Ask a = findAskByID(uid);
    	if (a.isNil()) throw new NoSuchElementException();
    	allAsks.remove(a);
    }
    
    public List<String> deleteAskByAccountID(String aid) {
    	if (allAsks == null) {
			allAsks = new ArrayList<Ask>();
		}
    	Iterator<Ask> ask_iter = allAsks.listIterator();
    	List<String> deleted = new ArrayList<String>();
    	while (ask_iter.hasNext()) {
    		Ask a = ask_iter.next();
    		if (a.getAccountID().equals(aid)) {
    			deleteAsk(a.getID());
        		deleted.add(a.getID());
    		}
    	}
    	return deleted;
    }
    
    public List<Ask> viewAllAsks() {
    	if (allAsks == null) {
			allAsks = new ArrayList<Ask>();
		}
    	return allAsks;
    }
    
    public List<Ask> viewMyAsks(String uid, boolean is_active) {
    	if (allAsks == null) {
			allAsks = new ArrayList<Ask>();
		}
    	List<Ask> myAsks = new ArrayList<Ask>();
    	Iterator<Ask> ask_iter = allAsks.listIterator();
    	while (ask_iter.hasNext()) {
    		Ask a = ask_iter.next();
    		if (a.getAccountID().equals(uid) && a.getActiveStatus()==is_active) {
    			myAsks.add(a);
    		}
    	}
    	return myAsks;
    }
    
    public Ask viewAsk(String uid) {
    	Ask a = findAskByID(uid);
    	if (a.isNil()) throw new NoSuchElementException();
    	return a;
    }
    
    public void clearAllAsks() {
    	if (allAsks == null) {
			allAsks = new ArrayList<Ask>();
		}
    	allAsks.clear();
    }
    
    public List<Ask> searchAsks(String key, String start_date, String end_date) {
    	if (allAsks == null) {
			allAsks = new ArrayList<Ask>();
		}
    	if (key==null) return allAsks;
    	List<Ask> filteredAsks = new ArrayList<Ask>();
    	try {
    		Date start = new SimpleDateFormat("DD-MM-YYYY").parse(start_date);
    		Date end = new SimpleDateFormat("DD-MM-YYYY").parse(end_date);
    		if (!start.before(end)) throw new AssertionError();
    		Iterator<Ask> ask_iter = allAsks.listIterator();
        	while (ask_iter.hasNext()) {
        		Ask a = ask_iter.next();
        		if (a.checkForKeyword(key)) {
        			Date created = new SimpleDateFormat("DD-MM-YYYY").parse(a.getdate_created());
        			if (!created.after(end) && !created.before(start)) {
        				filteredAsks.add(a);
        			}
        		}
        	}
        	return filteredAsks;
    	}
    	// Date cannot be parsed
    	catch (Exception e) {
    		throw new IllegalArgumentException();
    	}
    }
    
    public Ask findAskByID(String uid) {
    	if (allAsks == null) {
			allAsks = new ArrayList<Ask>();
		}
    	Iterator<Ask> ask_iter = allAsks.listIterator();
    	while (ask_iter.hasNext()) {
    		Ask a = ask_iter.next();
    		if (a.matchesID(uid)) return (a);
    	}
    	return (new NullAsk());
    }
    
    public void checkAskType(Ask a) {
    	String myType = a.getType();
    	for (String type : ASK_TYPES) {
    		if (myType.equals(type)) return;
    	}
    	// specified type is not a valid identifier
    	throw new IllegalArgumentException();
    }
    
    public void checkMissingAskInfo(Ask a) {
    	if (a.getAccountID()==null || 
    		a.getType()==null ||
    		a.getDescription()==null || 
    		a.getStartDate()==null) {
    			throw new AssertionError();
    	}
    }
    
    public String assessMissingAskInfo(Ask a) {
		if (a.getAccountID()==null) return "Account ID is missing!";
		if (a.getType()==null) return "Type is missing!";
		if (a.getDescription()==null) return "Description is missing!";
		if (a.getStartDate()==null) return "Start date is missing!";
		return "Something went wrong.";
    }
    
    
    // GIVE METHODS
    public Give createGive(Give g) {
    	if (allGives == null) {
			allGives = new ArrayList<Give>();
		}
		checkMissingGiveInfo(g);
		checkGiveType(g);
		Give newGive = new Give(g);
		allGives.add(newGive);
		g.activate();
		return newGive;
	}
	
	public Give deactivateGive(String gid) {
		Give g = findGiveByID(gid);
		if (g.isNil()) throw new NoSuchElementException();
		g.deactivate();
    	allGives.remove(g);
    	return g;
	}
	
    public void updateGive(String old_id, Give gnew) {
    	Give gold = findGiveByID(old_id);
    	if (gold.isNil() || !gold.getActiveStatus()) throw new NoSuchElementException();
    	checkMissingGiveInfo(gnew);
    	checkGiveType(gnew);
    	gold.updateAccountID(gnew.getAccountID());
    	gold.updateType(gnew.getType());
    	gold.updateDescription(gnew.getDescription());
    	SimpleDateFormat fmt = new SimpleDateFormat("YYYY-MM-DD");
    	gold.updateStartDate(fmt.format(gnew.getStartDate()));
    	gold.updateEndDate(fmt.format(gnew.getEndDate()));
    	gold.updateExtraZip(gnew.getExtraZip());
    }
    
    public void deleteGive(String gid) {
    	Give g = findGiveByID(gid);
    	if (g.isNil()) throw new NoSuchElementException();
    	allGives.remove(g);
    }
    
    public List<String> deleteGiveByAccountID(String aid) {
    	if (allGives == null) {
			allGives = new ArrayList<Give>();
		}
    	Iterator<Give> give_iter = allGives.listIterator();
    	List<String> deleted = new ArrayList<String>();
    	while (give_iter.hasNext()) {
    		Give g = give_iter.next();
    		if (g.getAccountID().equals(aid)) {
    			deleteGive(g.getID());
        		deleted.add(g.getID());
    		}
    	}
    	return deleted;
    }
    
    public List<Give> viewAllGives() {
    	if (allGives == null) {
			allGives = new ArrayList<Give>();
		}
    	return allGives;
    }
    
    public List<Give> viewMyGives(String uid, boolean is_active) {
    	if (allGives == null) {
			allGives = new ArrayList<Give>();
		}
    	List<Give> myGives = new ArrayList<Give>();
    	Iterator<Give> give_iter = allGives.listIterator();
    	while (give_iter.hasNext()) {
    		Give g = give_iter.next();
    		if (g.getAccountID().equals(uid) && g.getActiveStatus()==is_active) {
    			myGives.add(g);
    		}
    	}
    	return myGives;
    }
    
    public Give viewGive(String uid) {
    	Give g = findGiveByID(uid);
    	if (g.isNil()) throw new NoSuchElementException();
    	return g;
    }
    
    public void clearAllGives() {
    	if (allGives == null) {
			allGives = new ArrayList<Give>();
		}
    	allGives.clear();
    }
    
    public List<Give> searchGives(String key, String start_date, String end_date) {
    	if (allGives == null) {
			allGives = new ArrayList<Give>();
		}
    	if (key==null) return allGives;
    	List<Give> filteredGives = new ArrayList<Give>();
    	try {
    		Date start = new SimpleDateFormat("DD-MM-YYYY").parse(start_date);
    		Date end = new SimpleDateFormat("DD-MM-YYYY").parse(end_date);
    		Iterator<Give> give_iter = allGives.listIterator();
        	while (give_iter.hasNext()) {
        		Give g = give_iter.next();
        		if (g.checkForKeyword(key)) {
        			Date created = new SimpleDateFormat("DD-MM-YYYY").parse(g.getdate_created());
        			if (!created.after(end) && !created.before(start)) {
        				filteredGives.add(g);
        			}
        		}
        	}
        	return filteredGives;
    	}
    	// Date cannot be parsed
    	catch (ParseException e) {
    		throw new IllegalArgumentException();
    	}
    }
    
    public Give findGiveByID(String uid) {
    	if (allGives == null) {
			allGives = new ArrayList<Give>();
		}
    	Iterator<Give> give_iter = allGives.listIterator();
    	while (give_iter.hasNext()) {
    		Give g = give_iter.next();
    		if (g.matchesID(uid)) return g;
    	}
    	return (new NullGive());
    }
    
    public void checkGiveType(Give g) {
    	String myType = g.getType();
    	for (String type : GIVE_TYPES) {
    		if (myType.equals(type)) return;
    	}
    	// specified type is not a valid identifier
    	throw new IllegalArgumentException();
    }
    
    public void checkMissingGiveInfo(Give g) {
    	if (g.getAccountID()==null || 
    		g.getType()==null ||
    		g.getDescription()==null || 
    		g.getStartDate()==null) {
    			throw new AssertionError();
    	}
    }
    
    public String assessMissingGiveInfo(Give g) {
		if (g.getAccountID()==null) return "Account ID is missing!";
		if (g.getType()==null) return "Type is missing!";
		if (g.getDescription()==null) return "Description is missing!";
		if (g.getStartDate()==null) return "Start date is missing!";
		return "Something went wrong.";
    }
    
    
    // THANK METHODS
    public Thank createThank(Thank t) {
    	if (allThanks == null) {
			allThanks = new ArrayList<Thank>();
		}
		checkMissingThankInfo(t);
		Thank newThank = new Thank(t);
		allThanks.add(newThank);
		t.activate();
		return newThank;
	}
	
    public void updateThank(String old_id, Thank tnew) {
    	Thank told = findThankByID(old_id);
    	if (told.isNil() || !told.getActiveStatus()) throw new NoSuchElementException();
    	checkMissingThankInfo(tnew);
    	told.updateAccountID(tnew.getAccountID());
    	told.updateThankTo(tnew.getThankTo());
    	told.updateDescription(tnew.getDescription());
    }
    
    public void deleteThank(String uid) {
    	Thank t = findThankByID(uid);
    	if (t.isNil()) throw new NoSuchElementException();
    	allThanks.remove(t);
    }
    
    public List<String> deleteThankByAccountID(String aid) {
    	if (allThanks == null) {
			allThanks = new ArrayList<Thank>();
		}
    	Iterator<Thank> thank_iter = allThanks.listIterator();
    	List<String> deleted = new ArrayList<String>();
    	while (thank_iter.hasNext()) {
    		Thank t = thank_iter.next();
    		if (t.getAccountID().equals(aid)) {
    			deleteThank(t.getID());
        		deleted.add(t.getID());
    		}
    	}
    	return deleted;
    }
    
    public List<Thank> viewAllThanks() {
    	if (allThanks == null) {
			allThanks = new ArrayList<Thank>();
		}
    	return allThanks;
    }
    
    public List<Thank> viewMyThanks(String uid, boolean is_active) {
    	if (allThanks == null) {
			allThanks = new ArrayList<Thank>();
		}
    	List<Thank> myThanks = new ArrayList<Thank>();
    	Iterator<Thank> thank_iter = allThanks.listIterator();
    	while (thank_iter.hasNext()) {
    		Thank t = thank_iter.next();
    		if (t.getAccountID().equals(uid) && t.getActiveStatus()==is_active) {
    			myThanks.add(t);
    		}
    	}
    	return myThanks;
    }
    
    public Thank viewThank(String uid) {
    	Thank t = findThankByID(uid);
    	if (t.isNil()) throw new NoSuchElementException();
    	return t;
    }
    
    public void clearAllThanks() {
    	if (allThanks == null) {
			allThanks = new ArrayList<Thank>();
		}
    	allThanks.clear();
    }
    
    public List<Thank> searchThanks(String key, String start_date, String end_date) {
    	if (allThanks == null) {
			allThanks = new ArrayList<Thank>();
		}
    	if (key==null) return allThanks;
    	List<Thank> filteredThanks = new ArrayList<Thank>();
    	try {
    		Date start = new SimpleDateFormat("DD-MM-YYYY").parse(start_date);
    		Date end = new SimpleDateFormat("DD-MM-YYYY").parse(end_date);
    		Iterator<Thank> thank_iter = allThanks.listIterator();
        	while (thank_iter.hasNext()) {
        		Thank t = thank_iter.next();
        		if (t.checkForKeyword(key)) {
        			Date created = new SimpleDateFormat("DD-MM-YYYY").parse(t.getdate_created());
        			if (!created.after(end) && !created.before(start)) {
        				filteredThanks.add(t);
        			}
        		}
        	}
        	return filteredThanks;
    	}
    	// Date cannot be parsed
    	catch (Exception e) {
    		throw new IllegalArgumentException();
    	}
    }
    
    public Thank findThankByID(String uid) {
    	if (allThanks == null) {
			allThanks = new ArrayList<Thank>();
		}
    	Iterator<Thank> thank_iter = allThanks.listIterator();
    	while (thank_iter.hasNext()) {
    		Thank t = thank_iter.next();
    		if (t.matchesID(uid)) return (t);
    	}
    	return (new NullThank());
    }
    
    public void checkMissingThankInfo(Thank t) {
    	if (t.getAccountID()==null || 
    		t.getThankTo()==null ||
    		t.getDescription()==null) {
    			throw new AssertionError();
    	}
    }
    
    public String assessMissingThankInfo(Thank t) {
		if (t.getAccountID()==null) return "Account ID is missing!";
		if (t.getThankTo()==null) return "Thank To field is missing!";
		if (t.getDescription()==null) return "Description is missing!";
		return "Something went wrong.";
    }
    
    
    // NOTE METHODS
    public Note createNote(Note n) {
    	if (allNotes == null) {
    		allNotes = new ArrayList<Note>();
		}
		checkMissingNoteInfo(n);
		checkNoteToType(n);
		Note newNote = new Note(n);
		allNotes.add(newNote);
		return newNote;
	}
	
    public void updateNote(String old_id, Note nnew) {
    	Note nold = findNoteByID(old_id);
    	if (nold.isNil() || !nold.getActiveStatus()) throw new NoSuchElementException();
    	checkMissingNoteInfo(nnew);
    	checkNoteToType(nnew);
    	nold.updateAccountID(nnew.getAccountID());
    	nold.updateToType(nnew.getToType());
    	nold.updateToUserID(nnew.getToUserID());
    	nold.updateToID(nnew.getToID());
    	nold.updateDescription(nnew.getDescription());
    }
    
    public void deleteNote(String uid) {
    	Note n = findNoteByID(uid);
    	if (n.isNil()) throw new NoSuchElementException();
    	allNotes.remove(n);
    }
    
    public List<String> deleteNoteByAccountID(String aid) {
    	if (allNotes == null) {
    		allNotes = new ArrayList<Note>();
		}
    	Iterator<Note> note_iter = allNotes.listIterator();
    	List<String> deleted = new ArrayList<String>();
    	while (note_iter.hasNext()) {
    		Note n = note_iter.next();
    		if (n.getAccountID().equals(aid)) {
    			deleteNote(n.getID());
        		deleted.add(n.getID());
    		}
    	}
    	return deleted;
    }
    
    public void deleteNoteByToID(String toid) {
    	// TODO: straighten out recursive references
    	
    	if (allNotes == null) {
    		allNotes = new ArrayList<Note>();
		}
    	Iterator<Note> note_iter = allNotes.listIterator();
    	List<String> deleted = new ArrayList<String>();
    	while (note_iter.hasNext()) {
    		Note n = note_iter.next();
    		if (n.getToID().equals(toid)) {
    			deleteNote(n.getID());
        		deleted.add(n.getID());
    		}
    	}
    	// recursively delete all subordinated resources
    	Iterator<String> del_iter = deleted.listIterator();
    	while (del_iter.hasNext()) {
    		deleteNoteByToID(del_iter.next());
    	}
    }
    
    public List<Note> viewAllNotes() {
    	if (allNotes == null) {
    		allNotes = new ArrayList<Note>();
		}
    	return allNotes;
    }
    
    public List<Note> viewMyNotes(String uid, boolean is_active) {
    	if (allNotes == null) {
    		allNotes = new ArrayList<Note>();
		}
    	List<Note> myNotes = new ArrayList<Note>();
    	Iterator<Note> note_iter = allNotes.listIterator();
    	while (note_iter.hasNext()) {
    		Note n = note_iter.next();
    		if (n.getAccountID().equals(uid) && n.getActiveStatus()==is_active) {
    			myNotes.add(n);
    		}
    	}
    	return myNotes;
    }
    
    public Note viewNote(String uid) {
    	Note n = findNoteByID(uid);
    	if (n.isNil()) throw new NoSuchElementException();
    	return n;
    }
    
    public void clearAllNotes() {
    	if (allNotes != null) {
    		allNotes.clear();
    	}
    }
    
    public List<Note> searchNotes(String key, String start_date, String end_date) {
    	if (allNotes == null) {
    		allNotes = new ArrayList<Note>();
		}
    	if (key==null) return allNotes;
    	List<Note> filteredNotes = new ArrayList<Note>();
    	try {
    		Date start = new SimpleDateFormat("DD-MM-YYYY").parse(start_date);
    		Date end = new SimpleDateFormat("DD-MM-YYYY").parse(end_date);
    		Iterator<Note> note_iter = allNotes.listIterator();
        	while (note_iter.hasNext()) {
        		Note n = note_iter.next();
        		if (n.checkForKeyword(key)) {
        			Date created = new SimpleDateFormat("DD-MM-YYYY").parse(n.getdate_created());
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
    
    public Note findNoteByID(String uid) {
    	if (allNotes == null) {
    		allNotes = new ArrayList<Note>();
		}
    	Iterator<Note> note_iter = allNotes.listIterator();
    	while (note_iter.hasNext()) {
    		Note n = note_iter.next();
    		if (n.matchesID(uid)) return n;
    	}
    	return (new NullNote());
    }
    
    public void checkNoteToType(Note n) {
    	String myType = n.getToType();
    	for (String type : NOTE_TO_TYPES) {
    		if (myType.equals(type)) return;
    	}
    	// specified type is not a valid identifier
    	throw new IllegalArgumentException();
    }
    
    public void checkMissingNoteInfo(Note n) {
    	if (n.getAccountID()==null || 
    		n.getToType()==null ||
    		n.getToUserID()==null ||
    		n.getToID()==null ||
    		n.getDescription()==null) {
    			throw new AssertionError();
    	}
    }
    
    public String assessMissingNoteInfo(Note n) {
		if (n.getAccountID()==null) return "Account ID is missing!";
		if (n.getToType()==null) return "Type is missing!";
		if (n.getToUserID()==null) return "Recipient account is missing!";
		if (n.getToID()==null) return "To ID field is missing!";
		if (n.getDescription()==null) return "Description is missing!";
		return "Something went wrong.";
    }
    
}

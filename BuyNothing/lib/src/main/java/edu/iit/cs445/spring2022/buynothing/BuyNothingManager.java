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
		createAccount(default_account1).activate();
		createAccount(default_account2);
		createAccount(default_account3).activate();
	}
	
	public Account createAccount(Account a) {
		if (allAccounts == null) {
			createDefaultAccounts();
		}
		checkMissingAccountInfo(a);
		if (a.getActiveStatus()) throw new AssertionError("You may not use PUT to activate an account, use GET /accounts/"+a.getID()+"/activate instead");
		Account newAccount = new Account(a);
		newAccount.create();
		allAccounts.add(newAccount);
		return newAccount;
	}
	
	public void activateAccount(String uid) {
		Account a = findAccountByID(uid);
		if (a.isNil()) throw new NoSuchElementException("No account found for ID: "+uid);
		checkMissingAccountInfo(a);
		a.activate();
	}
	
    public void updateAccount(String old_id, Account anew) {
    	Account aold = findAccountByID(old_id);
    	if (aold.isNil()) throw new NoSuchElementException("No account found for ID: "+old_id);
    	checkMissingAccountInfo(anew);
    	if (anew.getActiveStatus()) throw new AssertionError("You may not use PUT to activate an account, use GET /accounts/"+anew.getID()+"/activate instead");
    	aold.updateName(anew.getName());
    	aold.updateAddress(anew.getStreet(), anew.getZip());
    	aold.updatePhone(anew.getPhone());
    	aold.updatePicture(anew.getPicture());
    }
    
    public void deleteAccount(String uid) {
    	Account a = findAccountByID(uid);
    	if (a.isNil()) throw new NoSuchElementException("No account found for ID: "+uid);
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
    	if (a.isNil()) throw new NoSuchElementException("No account found for ID: "+uid);
    	return a;
    }
    
    public void clearAllAccounts() {
    	if (allAccounts == null) {
    		createDefaultAccounts();
    	}
        allAccounts.clear();
    }
    
    public List<Account> searchAccounts(String key, String start_date, String end_date) {
    	if (allAccounts == null) createDefaultAccounts();
    	if (key == null) return viewAllAccounts();
    	List<Account> filteredAccounts = new ArrayList<Account>();
    	try {
    		Date start = new SimpleDateFormat("dd-MMM-YYYY").parse(start_date);
    		Date end = new SimpleDateFormat("dd-MMM-YYYY").parse(end_date);
    		if (!start.before(end)) throw new AssertionError("Start date cannot be before end date.");
    		Iterator<Account> acc_iter = allAccounts.listIterator();
        	while (acc_iter.hasNext()) {
        		Account a = acc_iter.next();
        		if (a.checkForKeyword(key)) {
        			if (a.getDateCreated() == "") {
        				filteredAccounts.add(a);
        			}
        			else {
        				Date created = new SimpleDateFormat("dd-MMM-YYYY").parse(a.getDateCreated());
            			if (!created.after(end) && !created.before(start)) {
            				filteredAccounts.add(a);
            			}
        			}
        		}
        	}
        	return filteredAccounts;
    	}
    	catch (ParseException e) {
    		throw new AssertionError("Date could not be parsed.");
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
    	if (a.getName()==null) throw new AssertionError("Name is missing!"); 
    	if (a.getStreet()==null) throw new AssertionError("Street is missing!");
    	if (a.getZip()==null) throw new AssertionError("Zip code is missing!");
    	if (a.getPhone()==null) throw new AssertionError("Phone number is missing!");
    	if (a.getPicture()==null) throw new AssertionError("Picture is missing!");
    }
    
    
    // ASK METHODS
    public Ask createAsk(String acc_id, Ask a) {
    	if (allAsks == null) {
			allAsks = new ArrayList<Ask>();
		}
		Account parent_account = findAccountByID(acc_id);
		if (parent_account.isNil()) throw new NoSuchElementException("No account found for ID: "+acc_id);
		if (!parent_account.getActiveStatus()) throw new AssertionError("This account "+parent_account.getID()+" is not active an may not create an ask.");
		if (!acc_id.equals(a.getAccountID())) throw new AssertionError("Account ID does not match URI.");
		checkMissingAskInfo(a);
		checkAskType(a);
		Ask newAsk = new Ask(a);
		newAsk.create();
		allAsks.add(newAsk);
		newAsk.activate();
		return newAsk;
	}
	
	public Ask deactivateAsk(String uid, String aid) {
		Account acc = findAccountByID(uid);
		Ask ask = findAskByID(aid);
		if (acc.isNil()) throw new NoSuchElementException("No account for ID: "+uid);
		if (ask.isNil()) throw new NoSuchElementException("No ask found for ID: "+aid);
		if (!ask.getAccountID().equals(uid)) throw new AssertionError("Account ID does not match URI.");
		ask.deactivate();
    	return ask;
	}
	
    public void updateAsk(String old_id, Ask anew) {
    	Ask aold = findAskByID(old_id);
    	if (aold.isNil() || !aold.getActiveStatus()) throw new NoSuchElementException("No ask found for ID: "+old_id);
    	if (!anew.getAccountID().equals(aold.getAccountID())) throw new AssertionError("Account ID cannot be altered.");
    	checkMissingAskInfo(anew);
    	checkAskType(anew);
    	aold.updateType(anew.getType());
    	aold.updateDescription(anew.getDescription());
    	aold.updateStartDate(anew.getStartDate());
    	aold.updateEndDate(anew.getEndDate());
    	aold.updateExtraZip(anew.getExtraZip());
    }
    
    public void deleteAsk(String uid, String aid) {
    	Ask a = findAskByID(aid);
    	if (a.isNil()) throw new NoSuchElementException("No ask found for ID: "+aid);
    	if (!a.getAccountID().equals(uid)) throw new AssertionError("Account ID does not match URI.");
    	allAsks.remove(a);
    }
    
    public List<String> deleteAskByAccountID(String uid) {
    	if (allAsks == null) {
			allAsks = new ArrayList<Ask>();
		}
    	Iterator<Ask> ask_iter = allAsks.listIterator();
    	List<String> deleted = new ArrayList<String>();
    	while (ask_iter.hasNext()) {
    		Ask a = ask_iter.next();
    		if (a.getAccountID().equals(uid)) {
    			deleteAsk(uid, a.getID());
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
    
    public List<Ask> viewAllMyAsks(String uid) {
    	if (allAsks == null) {
			allAsks = new ArrayList<Ask>();
		}
    	if (findAccountByID(uid).isNil()) throw new NoSuchElementException("No account found for ID: "+uid);
    	List<Ask> myAsks = new ArrayList<Ask>();
    	Iterator<Ask> ask_iter = allAsks.listIterator();
    	while (ask_iter.hasNext()) {
    		Ask a = ask_iter.next();
    		myAsks.add(a);
    		if (a.getAccountID().equals(uid)) {
    			myAsks.add(a);
    		}
    	}
    	return myAsks;
    }
    
    public Ask viewAsk(String aid) {
    	Ask a = findAskByID(aid);
    	if (a.isNil()) throw new NoSuchElementException("No ask found for ID: "+aid);
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
    		Date start = new SimpleDateFormat("dd-MMM-YYYY").parse(start_date);
    		Date end = new SimpleDateFormat("dd-MMM-YYYY").parse(end_date);
    		if (!start.before(end)) throw new AssertionError("Start date cannot be before end date.");
    		Iterator<Ask> ask_iter = allAsks.listIterator();
        	while (ask_iter.hasNext()) {
        		Ask a = ask_iter.next();
        		if (a.checkForKeyword(key)) {
        			if (a.getDateCreated() == "") {
        				filteredAsks.add(a);
        			}
        			else {
        				Date created = new SimpleDateFormat("dd-MMM-YYYY").parse(a.getDateCreated());
            			if (!created.after(end) && !created.before(start)) {
            				filteredAsks.add(a);
            			}
        			}
        		}
        	}
        	return filteredAsks;
    	}
    	// Date cannot be parsed
    	catch (Exception e) {
    		throw new AssertionError("Date could not be parsed.");
    	}
    }
    
    public Ask findAskByID(String aid) {
    	if (allAsks == null) {
			allAsks = new ArrayList<Ask>();
		}
    	Iterator<Ask> ask_iter = allAsks.listIterator();
    	while (ask_iter.hasNext()) {
    		Ask a = ask_iter.next();
    		if (a.matchesID(aid)) return (a);
    	}
    	return (new NullAsk());
    }
    
    public void checkAskType(Ask a) {
    	String myType = a.getType();
    	for (String type : ASK_TYPES) {
    		if (myType.equals(type)) return;
    	}
    	// specified type is not a valid identifier
    	throw new AssertionError("Ask has invalid type.");
    }
    
    public void checkMissingAskInfo(Ask a) {
    	if (a.getType()==null) throw new AssertionError("Type is missing!");
    	if (a.getDescription()==null) throw new AssertionError("Description is missing!");
    	if (a.getStartDate()==null) throw new AssertionError("Start date is missing!");
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
		if (g.isNil()) throw new NoSuchElementException("No give found for ID: "+gid);
		g.deactivate();
    	allGives.remove(g);
    	return g;
	}
	
    public void updateGive(String old_id, Give gnew) {
    	Give gold = findGiveByID(old_id);
    	if (gold.isNil() || !gold.getActiveStatus()) throw new NoSuchElementException("No give found for ID: "+old_id);
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
    	if (g.isNil()) throw new NoSuchElementException("No give found for ID: "+gid);
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
    
    public Give viewGive(String gid) {
    	Give g = findGiveByID(gid);
    	if (g.isNil()) throw new NoSuchElementException("No give found for ID: "+gid);
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
    		Date start = new SimpleDateFormat("dd-MMM-YYYY").parse(start_date);
    		Date end = new SimpleDateFormat("dd-MMM-YYYY").parse(end_date);
    		Iterator<Give> give_iter = allGives.listIterator();
        	while (give_iter.hasNext()) {
        		Give g = give_iter.next();
        		if (g.checkForKeyword(key)) {
        			Date created = new SimpleDateFormat("dd-MMM-YYYY").parse(g.getDateCreated());
        			if (!created.after(end) && !created.before(start)) {
        				filteredGives.add(g);
        			}
        		}
        	}
        	return filteredGives;
    	}
    	// Date cannot be parsed
    	catch (ParseException e) {
    		throw new AssertionError("Date could not be parsed.");
    	}
    }
    
    public Give findGiveByID(String gid) {
    	if (allGives == null) {
			allGives = new ArrayList<Give>();
		}
    	Iterator<Give> give_iter = allGives.listIterator();
    	while (give_iter.hasNext()) {
    		Give g = give_iter.next();
    		if (g.matchesID(gid)) return g;
    	}
    	return (new NullGive());
    }
    
    public void checkGiveType(Give g) {
    	String myType = g.getType();
    	for (String type : GIVE_TYPES) {
    		if (myType.equals(type)) return;
    	}
    	// specified type is not a valid identifier
    	throw new AssertionError("Give has invalid type.");
    }
    
    public void checkMissingGiveInfo(Give g) {
    	if (g.getAccountID()==null) throw new AssertionError("Account ID is missing!");
    	if (g.getType()==null) throw new AssertionError("Type is missing!");
    	if (g.getDescription()==null) throw new AssertionError("Description is missing!");
    	if (g.getStartDate()==null) throw new AssertionError("Start date is missing!");
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
    	if (told.isNil() || !told.getActiveStatus()) throw new NoSuchElementException("No thank found for ID: "+old_id);
    	checkMissingThankInfo(tnew);
    	told.updateAccountID(tnew.getAccountID());
    	told.updateThankTo(tnew.getThankTo());
    	told.updateDescription(tnew.getDescription());
    }
    
    public void deleteThank(String tid) {
    	Thank t = findThankByID(tid);
    	if (t.isNil()) throw new NoSuchElementException("No thank found for ID: "+tid);
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
    
    public Thank viewThank(String tid) {
    	Thank t = findThankByID(tid);
    	if (t.isNil()) throw new NoSuchElementException("No thank found for ID: "+tid);
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
    		Date start = new SimpleDateFormat("dd-MMM-YYYY").parse(start_date);
    		Date end = new SimpleDateFormat("dd-MMM-YYYY").parse(end_date);
    		Iterator<Thank> thank_iter = allThanks.listIterator();
        	while (thank_iter.hasNext()) {
        		Thank t = thank_iter.next();
        		if (t.checkForKeyword(key)) {
        			Date created = new SimpleDateFormat("dd-MMM-YYYY").parse(t.getDateCreated());
        			if (!created.after(end) && !created.before(start)) {
        				filteredThanks.add(t);
        			}
        		}
        	}
        	return filteredThanks;
    	}
    	// Date cannot be parsed
    	catch (Exception e) {
    		throw new AssertionError("Date could not be parsed.");
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
    	if (t.getAccountID()==null) throw new AssertionError("Account ID is missing!");
    	if (t.getThankTo()==null) throw new AssertionError("Thank To field is missing!");
    	if (t.getDescription()==null) throw new AssertionError("Description is missing!");
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
    	if (nold.isNil() || !nold.getActiveStatus()) throw new NoSuchElementException("No note found for ID: "+old_id);
    	checkMissingNoteInfo(nnew);
    	checkNoteToType(nnew);
    	nold.updateAccountID(nnew.getAccountID());
    	nold.updateToType(nnew.getToType());
    	nold.updateToUserID(nnew.getToUserID());
    	nold.updateToID(nnew.getToID());
    	nold.updateDescription(nnew.getDescription());
    }
    
    public void deleteNote(String nid) {
    	Note n = findNoteByID(nid);
    	if (n.isNil()) throw new NoSuchElementException("No note found for ID: "+nid);
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
    
    public Note viewNote(String nid) {
    	Note n = findNoteByID(nid);
    	if (n.isNil()) throw new NoSuchElementException("No note found for ID: "+nid);
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
    		Date start = new SimpleDateFormat("dd-MMM-YYYY").parse(start_date);
    		Date end = new SimpleDateFormat("dd-MMM-YYYY").parse(end_date);
    		Iterator<Note> note_iter = allNotes.listIterator();
        	while (note_iter.hasNext()) {
        		Note n = note_iter.next();
        		if (n.checkForKeyword(key)) {
        			Date created = new SimpleDateFormat("dd-MMM-YYYY").parse(n.getDateCreated());
        			if (!created.after(end) && !created.before(start)) {
        				filteredNotes.add(n);
        			}
        		}
        	}
        	return filteredNotes;
    	}
    	// Date cannot be parsed
    	catch (Exception e) {
    		throw new AssertionError("Date could not be parsed.");
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
    	throw new AssertionError("Note has invalid type.");
    }
    
    public void checkMissingNoteInfo(Note n) {
    	if (n.getAccountID()==null) throw new AssertionError("Account ID is missing!");
    	if (n.getToType()==null) throw new AssertionError("Type is missing!");
    	if (n.getToUserID()==null) throw new AssertionError("Recipient account is missing!");
    	if (n.getToID()==null) throw new AssertionError("To ID field is missing!");
    	if (n.getDescription()==null) throw new AssertionError("Description is missing!");
    }
}

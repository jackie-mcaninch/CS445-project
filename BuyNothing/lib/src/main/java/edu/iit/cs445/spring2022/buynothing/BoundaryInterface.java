package edu.iit.cs445.spring2022.buynothing;

import java.util.List;

public interface BoundaryInterface {
	// ACCOUNT METHODS
	Account createAccount(Account a);
	Account activateAccount(String uid);
	void updateAccount(String old_id, Account anew);
	void deleteAccount(String uid);
	List<Account> viewAllAccounts();
	Account viewAccount(String uid);
	void clearAllAccounts();
	List<Account> searchAccounts(String key, String start_date, String end_date);
	Account findAccountByID(String uid);
	void checkMissingAccountInfo(Account a);
	
	// ASK METHODS
	Ask createAsk(String uid, Ask a);
	Ask deactivateAsk(String uid, String aid);
	void updateAsk(String old_id, Ask anew);
	void deleteAsk(String uid, String aid);
	List<String> deleteAskByAccountID(String uid);
	List<Ask> viewAsks(String uid, String is_active);
	List<Ask> viewMyAsks(String uid, String is_active);
	Ask viewAsk(String uid);
	void clearAllAsks();
	List<Ask> searchAsks(String key, String start_date, String end_date);
	Ask findAskByID(String aid);
	void checkAskType(Ask a);
	void checkMissingAskInfo(Ask a);
	
	// GIVE METHODS
	Give createGive(String uid, Give g);
	Give deactivateGive(String uid, String gid);
	void updateGive(String old_id, Give gnew);
	void deleteGive(String uid, String gid);
	List<String> deleteGiveByAccountID(String uid);
	List<Give> viewGives(String uid, String is_active);
	List<Give> viewMyGives(String uid, String is_active);
	Give viewGive(String gid);
	void clearAllGives();
	List<Give> searchGives(String key, String start_date, String end_date);
	Give findGiveByID(String gid);
	void checkGiveType(Give g);
	void checkMissingGiveInfo(Give g);
	
	// THANK METHODS
	Thank createThank(String uid, Thank t);
	Thank deactivateThank(String uid, String tid);
	void updateThank(String old_id, Thank tnew);
	void deleteThank(String uid, String tid);
	List<String> deleteThankByAccountID(String uid);
	List<Thank> viewThanks(String uid, String is_active);
	List<Thank> viewMyThanks(String uid, String is_active);
	List<Thank> viewThanksForUser(String uid);
	Thank viewThank(String uid);
	void clearAllThanks();
	List<Thank> searchThanks(String key, String start_date, String end_date);
	Thank findThankByID(String tid);
	void checkMissingThankInfo(Thank t);
		
	// NOTE METHODS
	Note createNote(Note n);
	Note deactivateNote(String uid, String nid);
	void updateNote(String old_id, Note nnew);
	void deleteNote(String uid, String nid);
	List<String> deleteNoteByAccountID(String uid);
	void deleteNoteByToID(String to_id);
	List<Note> viewNotes(String uid, String is_active);
	List<Note> viewMyNotes(String uid, String is_active);
	Note viewNote(String uid);
	void clearAllNotes();
	List<Note> searchNotes(String key, String start_date, String end_date);
	Note findNoteByID(String nid);
	void checkNoteToType(Note n);
	void checkMissingNoteInfo(Note n);
}

package edu.iit.cs445.spring2022.buynothing;

import java.util.List;

public interface BoundaryInterface {
	// ACCOUNT METHODS
	Account createAccount(Account a);
	void activateAccount(String uid);
	void updateAccount(String old_id, Account anew);
	void deleteAccount(String uid);
	List<Account> viewAllAccounts();
	Account viewAccount(String uid);
	void clearAllAccounts();
	List<Account> searchAccounts(String key, String start_date, String end_date);
	Account findAccountByID(String uid);
	void checkMissingAccountInfo(Account a);
	
	// ASK METHODS
	Ask createAsk(String acc_id, Ask a);
	Ask deactivateAsk(String uid, String aid);
	void updateAsk(String old_id, Ask anew);
	void deleteAsk(String uid, String aid);
	List<String> deleteAskByAccountID(String uid);
	List<Ask> viewAllAsks();
	List<Ask> viewAllMyAsks(String aid);
	List<Ask> viewMyAsks(String uid, boolean is_active);
	Ask viewAsk(String uid);
	void clearAllAsks();
	List<Ask> searchAsks(String key, String start_date, String end_date);
	Ask findAskByID(String aid);
	void checkAskType(Ask a);
	void checkMissingAskInfo(Ask a);
	
	// GIVE METHODS
	Give createGive(Give g);
	Give deactivateGive(String uid);
	void updateGive(String old_id, Give gnew);
	void deleteGive(String uid);
	List<String> deleteGiveByAccountID(String aid);
	List<Give> viewAllGives();
	List<Give> viewMyGives(String uid, boolean is_active);
	Give viewGive(String uid);
	void clearAllGives();
	List<Give> searchGives(String key, String start_date, String end_date);
	Give findGiveByID(String uid);
	void checkGiveType(Give g);
	void checkMissingGiveInfo(Give g);
	
	// THANK METHODS
	Thank createThank(Thank t);
	void updateThank(String old_id, Thank tnew);
	void deleteThank(String uid);
	List<String> deleteThankByAccountID(String aid);
	List<Thank> viewAllThanks();
	List<Thank> viewMyThanks(String uid, boolean is_active);
	Thank viewThank(String uid);
	void clearAllThanks();
	List<Thank> searchThanks(String key, String start_date, String end_date);
	Thank findThankByID(String uid);
	void checkMissingThankInfo(Thank t);
		
	// NOTE METHODS
	Note createNote(Note n);
	void updateNote(String old_id, Note nnew);
	void deleteNote(String uid);
	List<String> deleteNoteByAccountID(String aid);
	void deleteNoteByToID(String toid);
	List<Note> viewMyNotes(String uid, boolean is_active);
	Note viewNote(String uid);
	void clearAllNotes();
	List<Note> searchNotes(String key, String start_date, String end_date);
	Note findNoteByID(String uid);
	void checkNoteToType(Note n);
	void checkMissingNoteInfo(Note n);
}

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
	List<Account> searchAccounts(String key, String start_date, String end_date);
	
	// ASK METHODS
	Ask createAsk(String uid, Ask a);
	Ask deactivateAsk(String uid, String aid);
	void updateAsk(String old_id, Ask anew);
	void deleteAsk(String uid, String aid);
	List<Ask> viewAsks(String uid, String is_active);
	List<Ask> viewMyAsks(String uid, String is_active);
	Ask viewAsk(String uid);
	List<Ask> searchAsks(String key, String start_date, String end_date);
	
	// GIVE METHODS
	Give createGive(String uid, Give g);
	Give deactivateGive(String uid, String gid);
	void updateGive(String old_id, Give gnew);
	void deleteGive(String uid, String gid);
	List<Give> viewGives(String uid, String is_active);
	List<Give> viewMyGives(String uid, String is_active);
	Give viewGive(String gid);
	List<Give> searchGives(String key, String start_date, String end_date);
	
	// THANK METHODS
	Thank createThank(String uid, Thank t);
	void updateThank(String old_id, Thank tnew);
	List<Thank> viewThanks(String uid, String is_active);
	List<Thank> viewMyThanks(String uid, String is_active);
	List<Thank> viewThanksForUser(String uid);
	Thank viewThank(String tid);
	List<Thank> searchThanks(String key, String start_date, String end_date);
		
	// NOTE METHODS
	Note createNote(Note n);
	void updateNote(String old_id, Note nnew);
	void deleteNote(String uid, String nid);
	String viewNotes(String c_by, String v_by, String type, String agid);
	Note viewNote(String nid);
	List<Note> searchNotes(String key, String start_date, String end_date);

	// REPORT METHODS
	List<Report> viewAllReports();
	String generateReport(String rid, String c_by, String v_by, String start, String end);
}

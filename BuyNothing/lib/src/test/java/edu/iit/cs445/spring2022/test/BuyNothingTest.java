package edu.iit.cs445.spring2022.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.iit.cs445.spring2022.buynothing.*;

public class BuyNothingTest {
	AccountManager  am = new AccountManager();
	AskManager 	    km = new AskManager();
	GiveManager		gm = new GiveManager();
	ThankManager	tm = new ThankManager();
	NoteManager		nm = new NoteManager();
    Account test_account = new Account();
    Account invalid_test_account = new Account();
    Ask test_ask = new Ask();
    Ask invalid_test_ask = new Ask();
    Give test_give = new Give();
    Give invalid_test_give = new Give();
    Thank test_thank = new Thank();
    Thank invalid_test_thank = new Thank();
    Note test_note = new Note();
    Note invalid_test_note = new Note();

    @BeforeEach
    public void setUp() throws Exception {
    	//initiate accounts
        test_account.updateName("Jackie McAninch");
        test_account.updateAddress("3201 S State St", "60616");
        test_account.updatePhone("517-358-1025");
        test_account.updatePicture("<link to my picture>");
        am.clearAllAccounts();
        
        //initiate asks
        test_ask.updateAccountID(test_account.getID());
        test_ask.updateType("gift");
        test_ask.updateDescription("I am looking for a homemade batch of cookies.");
        test_ask.updateStartDate("2022-04-25");
        test_ask.updateEndDate("2022-05-25");
        String[] zips = {"60605", "60653", "60608", "60607"};
        test_ask.updateExtraZip(zips);
        km.clearAllAsks();
        
        //initiate gives
        test_give.updateAccountID(test_account.getID());
        test_give.updateType("service");
        test_give.updateDescription("I will bake you a batch of chocolate chip cookies!");
        test_give.updateStartDate("2022-04-25");
        test_give.updateEndDate("2022-05-25");
        test_give.updateExtraZip(zips);
        gm.clearAllGives();
        
        //initiate thanks
        test_thank.updateAccountID(test_account.getID());
        // create account for ThankTo field in the unit test
        test_thank.updateDescription("Thank you for the cookies :)");
        tm.clearAllThanks();
        
        //initiate notes
        test_note.updateAccountID(test_account.getID());
        test_note.updateToType("give");
        // create account for toUserID and toID in the unit test
        test_note.updateDescription("I am looking for a set of headphones.");
        nm.clearAllNotes();
    }

    
    // ACCOUNT TESTS
    @Test void test_account_equals() {
    	Account a1 = am.createAccount(test_account);
    	Account a2 = am.createAccount(test_account);
    	assertTrue(a1.equals(a2));
    	a1.updatePicture("<new picture link>");
    	assertFalse(a1.equals(a2));
    	a1.updatePhone("123-456-7890");
    	assertFalse(a1.equals(a2));
    	a1.updateAddress("3201 S State St", "12345");
    	assertFalse(a1.equals(a2));
    	a1.updateAddress("3007 S Princeton Ave", "60616");
    	assertFalse(a1.equals(a2));
    	a1.updateName("New Name");
    	assertFalse(a1.equals(a2));
    	Account null_account = am.findByID("bad id");
    	boolean error_caught = false;
    	try {
    		a1.equals(null_account);
    	}
    	catch (NoSuchElementException e) {
    		error_caught = true;
    	}
    	assertTrue(error_caught);
    }
    @Test
    public void test_valid_account_create_activate_and_update() {
    	// create new valid account
        Account a = am.createAccount(test_account);
        am.activateAccount(a.getID());
        assertTrue(a.equals(test_account));
        
        // update account information
        Account new_account = new Account();
        new_account.updateName("Jacklyn McAninch");
        new_account.updateAddress("3007 S Princeton Ave", "60616");
        new_account.updatePhone("123-456-7890");
        new_account.updatePicture("<link to new picture>");
        am.updateAccount(a.getID(), new_account);
        assertTrue(a.equals(new_account));
    }
    
    @Test
    public void test_view_accounts() {
    	// create three valid accounts
        Account a1 = am.createAccount(test_account);
        Account a2 = am.createAccount(test_account);
        Account a3 = am.createAccount(test_account);
        a1.updateName("Person One");
        a2.updateName("Person Two");
        a3.updateName("Person Three");

        // activate all accounts
        am.activateAccount(a1.getID());
        am.activateAccount(a2.getID());
        am.activateAccount(a3.getID());
        
        // collect all active accounts
        List<Account> active_accounts = new ArrayList<Account>();
        active_accounts.add(am.viewAccount(a1.getID()));
        active_accounts.add(am.viewAccount(a2.getID()));
        active_accounts.add(am.viewAccount(a3.getID()));
        List<Account> all_accounts = am.viewAllAccounts();
        assertTrue(active_accounts.size()==3);
        assertTrue(all_accounts.size()==3);
        assertTrue(all_accounts.equals(active_accounts));
    }
    
    @Test
    public void test_valid_account_delete() {
    	Account a = am.createAccount(test_account);
    	am.deleteAccount(a.getID());
    	List<Account> allAccounts = am.viewAllAccounts();
    	Iterator<Account> acc_iter = allAccounts.listIterator();
    	assertFalse(acc_iter.hasNext());
    }
    
    @Test
    public void test_invalid_account_create_and_update() {    	
    	// test for missing name
    	String exp_error = "Name is missing!";
    	String actual_error = "";
    	try {
        	am.createAccount(invalid_test_account);
    	}
    	catch (AssertionError e) {
    		actual_error = am.assessMissingInfo(invalid_test_account);
    	}
    	assertTrue(exp_error.equals(actual_error));
		invalid_test_account.updateName("Jackie McAninch");
    	
    	// test for missing address
    	exp_error = "Street is missing!";
    	try {
        	am.createAccount(invalid_test_account);
    	}
    	catch (AssertionError e) {
    		actual_error = am.assessMissingInfo(invalid_test_account);
    	}
    	assertTrue(exp_error.equals(actual_error));
		invalid_test_account.updateAddress("3201 S State St", null);
    	exp_error = "Zip code is missing!";
    	try {
        	am.createAccount(invalid_test_account);
    	}
    	catch (AssertionError e) {
    		actual_error = am.assessMissingInfo(invalid_test_account);
    	}
    	assertTrue(exp_error.equals(actual_error));
		invalid_test_account.updateAddress("3201 S State St", "60616");
    	
    	// test for missing phone number
    	exp_error = "Phone number is missing!";
    	try {
    		am.createAccount(invalid_test_account);
    	}
    	catch (AssertionError e) {
    		actual_error = am.assessMissingInfo(invalid_test_account);
    	}
    	assertTrue(exp_error.equals(actual_error));
		invalid_test_account.updatePhone("517-358-1025");
    	
    	// test for missing picture
    	exp_error = "Picture is missing!";
    	try {
    		am.createAccount(invalid_test_account);
    	}
    	catch (AssertionError e) {
    		actual_error = am.assessMissingInfo(invalid_test_account);
    	}
    	assertTrue(exp_error.equals(actual_error));
		invalid_test_account.updatePicture("<link to my picture>");
    	
    	// test account is now valid
    	exp_error = "Something went wrong.";
		am.createAccount(invalid_test_account);
		actual_error = am.assessMissingInfo(invalid_test_account);
    	assertTrue(exp_error.equals(actual_error));
    }
    
    @Test
    public void test_activate_update_delete_and_view_null_account() {
        Account a = am.createAccount(test_account);
        boolean error_caught = false;
        String bad_uid = "asdf";
        
        // test activate null account
        try {
            am.activateAccount(bad_uid);        	
        }
        catch (NoSuchElementException e) {
        	error_caught = true;
        }
        assertTrue(error_caught);
        
        // test update null account
        error_caught = false;
        try {
        	am.updateAccount(bad_uid, a);
        }
        catch (NoSuchElementException e) {
        	error_caught = true;
        }
        assertTrue(error_caught);
        
        // test update null account
        error_caught = false;
        try {
        	am.deleteAccount(bad_uid);
        }
        catch (NoSuchElementException e) {
        	error_caught = true;
        }
        assertTrue(error_caught);
        
        // test view null account
        error_caught = false;
        try {
        	am.viewAccount(bad_uid);
        }
        catch (NoSuchElementException e) {
        	error_caught = true;
        }
        assertTrue(error_caught);
    }
    
    @Test
    public void test_search_accounts() {
    	Account a = am.createAccount(test_account);
    	am.activateAccount(a.getID());
    	
    	// test reject bad dates
    	boolean error_caught = false;
    	try {
    		am.searchAccounts("key", "bad date", "another bad date");
    	}
    	catch (IllegalArgumentException e) {
    		error_caught = true;
    	}
    	assertTrue(error_caught);

		String start = "01-01-2001";
		String end = "01-01-2023";
    	error_caught = false;
    	try {
    		am.searchAccounts("key", end, start);
    	}
    	catch (AssertionError e) {
    		error_caught = true;
    	}
    	assertTrue(error_caught);
    	
    	// test keywords
    	List<Account> result = new ArrayList<Account>();
    	result = am.searchAccounts("Jackie", start, end);
    	assertTrue(result.size()==1);
    	result = am.searchAccounts("McAninch", start, end);
    	assertTrue(result.size()==1);
    	result = am.searchAccounts("3201 S State St 60616", start, end);
    	assertTrue(result.size()==1);
    	result = am.searchAccounts("517-358-1025", start, end);
    	assertTrue(result.size()==1);
    	result = am.searchAccounts("bad keyword", start, end);
    	assertTrue(result.size()==0);
    	result = am.searchAccounts("Jackie", start, "01-01-2010");
    	assertTrue(result.size()==0);
    	result = am.searchAccounts("Jackie", "01-01-2023", "01-01-2025");
    	assertTrue(result.size()==0);
    	result = am.searchAccounts(null, start, end);
    	assertTrue(result.size()==1);
    }
    
    
    // ASK TESTS
    @Test void test_ask_equals() {
    	Ask a1 = km.createAsk(test_ask);
    	Ask a2 = km.createAsk(test_ask);
    	assertTrue(a1.equals(a2));
    	a1.updateType("borrow");
    	assertFalse(a1.equals(a2));
    	a1.updateDescription("new description");
    	assertFalse(a1.equals(a2));
    	a1.updateStartDate("2022-04-30");
    	assertFalse(a1.equals(a2));
    	a1.updateEndDate("2022-05-30");
    	assertFalse(a1.equals(a2));
    	String[] newzips = {"12345", "23456", "34567", "45678"};
    	a1.updateExtraZip(newzips);
    	assertFalse(a1.equals(a2));
    	Ask null_ask = km.findByID("bad id");
    	boolean error_caught = false;
    	try {
    		a1.equals(null_ask);
    	}
    	catch (NoSuchElementException e) {
    		error_caught = true;
    	}
    	assertTrue(error_caught);
    }
    
    @Test
    public void test_valid_ask_create_and_update() {
    	// create new valid ask
        Ask a = km.createAsk(test_ask);
        assertTrue(a.equals(test_ask));
        
        // update ask information
        Ask new_ask = new Ask();
        new_ask.updateAccountID(test_account.getID());
        new_ask.updateType("borrow");
        new_ask.updateDescription("new description");
        new_ask.updateStartDate("2022-04-30");
        new_ask.updateEndDate("2022-05-30");
    	String[] newzips = {"12345", "23456", "34567", "45678"};
        new_ask.updateExtraZip(newzips);
        km.updateAsk(a.getID(), new_ask);
        assertTrue(a.equals(new_ask));
    }
    
    @Test
    public void test_view_asks() {
    	// create three valid asks
        Ask a1 = km.createAsk(test_ask);
        Ask a2 = km.createAsk(test_ask);
        Ask a3 = km.createAsk(test_ask);
        
        // collect all active asks
        List<Ask> active_asks = new ArrayList<Ask>();
        active_asks.add(km.viewAsk(a1.getID()));
        active_asks.add(km.viewAsk(a2.getID()));
        active_asks.add(km.viewAsk(a3.getID()));
        List<Ask> all_asks = km.viewAllAsks();
        assertTrue(active_asks.size()==3);
        assertTrue(all_asks.size()==3);
        assertTrue(all_asks.equals(active_asks));
    }
    
    @Test
    public void test_valid_ask_delete() {
    	Ask a = km.createAsk(test_ask);
    	km.deleteAsk(a.getID());
    	List<Ask> allAsks = km.viewAllAsks();
    	Iterator<Ask> acc_iter = allAsks.listIterator();
    	assertFalse(acc_iter.hasNext());
    }
    
    @Test
    public void test_invalid_ask_create_and_update() {    	
    	// test for missing account id
    	String exp_error = "Account ID is missing!";
    	String actual_error = "";
    	try {
        	km.createAsk(invalid_test_ask);
    	}
    	catch (AssertionError e) {
    		actual_error = km.assessMissingInfo(invalid_test_ask);
    	}
    	assertTrue(exp_error.equals(actual_error));
		invalid_test_ask.updateAccountID(test_account.getID());
    	
    	// test for missing type
    	exp_error = "Type is missing!";
    	try {
        	km.createAsk(invalid_test_ask);
    	}
    	catch (AssertionError e) {
    		actual_error = km.assessMissingInfo(invalid_test_ask);
    	}
    	assertTrue(exp_error.equals(actual_error));
		invalid_test_ask.updateType("borrrow"); // will test this invalid type later
    	
    	// test for missing description
    	exp_error = "Description is missing!";
    	try {
    		km.createAsk(invalid_test_ask);
    	}
    	catch (AssertionError e) {
    		actual_error = km.assessMissingInfo(invalid_test_ask);
    	}
    	assertTrue(exp_error.equals(actual_error));
		invalid_test_ask.updateDescription("new description");
    	
    	// test for missing start date
    	exp_error = "Start date is missing!";
    	try {
    		km.createAsk(invalid_test_ask);
    	}
    	catch (AssertionError e) {
    		actual_error = km.assessMissingInfo(invalid_test_ask);
    	}
    	assertTrue(exp_error.equals(actual_error));
		invalid_test_ask.updateStartDate("2022-04-26");
		
		// test for invalid type
    	boolean error_caught = false;
    	try {
    		km.createAsk(invalid_test_ask);
    	}
    	catch (IllegalArgumentException e) {
    		error_caught = true;
    	}
    	assertTrue(error_caught);
		invalid_test_ask.updateType("borrow");
    	
    	// test ask is now valid
    	exp_error = "Something went wrong.";
		km.createAsk(invalid_test_ask);
		actual_error = km.assessMissingInfo(invalid_test_ask);
    	assertTrue(exp_error.equals(actual_error));
    }
    
    @Test
    public void test_update_delete_and_view_null_ask() {
        Ask a = km.createAsk(test_ask);
        boolean error_caught = false;
        String bad_uid = "asdf";
                
        // test update null ask
        error_caught = false;
        try {
        	km.updateAsk(bad_uid, a);
        }
        catch (NoSuchElementException e) {
        	error_caught = true;
        }
        assertTrue(error_caught);
        
        // test update null ask
        error_caught = false;
        try {
        	km.deleteAsk(bad_uid);
        }
        catch (NoSuchElementException e) {
        	error_caught = true;
        }
        assertTrue(error_caught);
        
        // test view null ask
        error_caught = false;
        try {
        	km.viewAsk(bad_uid);
        }
        catch (NoSuchElementException e) {
        	error_caught = true;
        }
        assertTrue(error_caught);
    }
    
    @Test
    public void test_search_asks() {
    	Ask a = km.createAsk(test_ask);
    	
    	// test reject bad dates
    	boolean error_caught = false;
    	try {
    		km.searchAsks("key", "bad date", "another bad date");
    	}
    	catch (IllegalArgumentException e) {
    		error_caught = true;
    	}
    	assertTrue(error_caught);

		String start = "01-01-2001";
		String end = "01-01-2023";
    	error_caught = false;
    	try {
    		km.searchAsks("key", end, start);
    	}
    	catch (AssertionError e) {
    		error_caught = true;
    	}
    	assertTrue(error_caught);
    	
    	// test keywords
    	List<Ask> result = new ArrayList<Ask>();
    	result = km.searchAsks("gift", start, end);
    	assertTrue(result.size()==1);
    	result = km.searchAsks("I am looking for a homemade batch of cookies.", start, end);
    	assertTrue(result.size()==1);
    	result = km.searchAsks("60608", start, end);
    	assertTrue(result.size()==1);
    	result = km.searchAsks("bad keyword", start, end);
    	assertTrue(result.size()==0);
    	result = km.searchAsks("gift", start, "01-01-2010");
    	assertTrue(result.size()==0);
    	result = km.searchAsks(null, start, end);
    	assertTrue(result.size()==1);
    }
}


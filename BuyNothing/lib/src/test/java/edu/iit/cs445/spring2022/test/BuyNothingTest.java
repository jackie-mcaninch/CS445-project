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
	BuyNothingManager bm = new BuyNothingManager();
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
        bm.clearAllAccounts();
        
        //initiate asks
        test_ask.updateAccountID(test_account.getID());
        test_ask.updateType("gift");
        test_ask.updateDescription("I am looking for a homemade batch of cookies.");
        test_ask.updateStartDate("25-Apr-2022");
        test_ask.updateEndDate("25-May-2022");
        String[] zips = {"60605", "60653", "60608", "60607"};
        test_ask.updateExtraZip(zips);
        bm.clearAllAsks();
        
        //initiate gives
        test_give.updateAccountID(test_account.getID());
        test_give.updateType("service");
        test_give.updateDescription("I will bake you a batch of chocolate chip cookies!");
        test_give.updateStartDate("25-Apr-2022");
        test_give.updateEndDate("25-May-2022");
        test_give.updateExtraZip(zips);
        bm.clearAllGives();
        
        //initiate thanks
        test_thank.updateAccountID(test_account.getID());
        // create account for ThankTo field in the unit test
        test_thank.updateDescription("Thank you for the cookies :)");
        bm.clearAllThanks();
        
        //initiate notes
        test_note.updateAccountID(test_account.getID());
        test_note.updateToType("ask");
        // create account for toUserID and toID in the unit test
        test_note.updateDescription("Cookies do sound good...");
        bm.clearAllNotes();
    }

    
    // ACCOUNT TESTS
    @Test void test_account_equals() {
    	Account a1 = bm.createAccount(test_account);
    	Account a2 = bm.createAccount(test_account);
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
    	Account null_account = bm.findAccountByID("bad id");
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
        Account a = bm.createAccount(test_account);
        bm.activateAccount(a.getID());
        assertTrue(a.equals(test_account));
        
        // update account information
        Account new_account = new Account();
        new_account.updateName("Jacklyn McAninch");
        new_account.updateAddress("3007 S Princeton Ave", "60616");
        new_account.updatePhone("123-456-7890");
        new_account.updatePicture("<link to new picture>");
        bm.updateAccount(a.getID(), new_account);
        assertTrue(a.equals(new_account));
    }
    
    @Test
    public void test_view_accounts() {
    	// create three valid accounts
        Account a1 = bm.createAccount(test_account);
        Account a2 = bm.createAccount(test_account);
        Account a3 = bm.createAccount(test_account);
        a1.updateName("Person One");
        a2.updateName("Person Two");
        a3.updateName("Person Three");

        // activate all accounts
        bm.activateAccount(a1.getID());
        bm.activateAccount(a2.getID());
        bm.activateAccount(a3.getID());
        
        // collect all active accounts
        List<Account> active_accounts = new ArrayList<Account>();
        active_accounts.add(bm.viewAccount(a1.getID()));
        active_accounts.add(bm.viewAccount(a2.getID()));
        active_accounts.add(bm.viewAccount(a3.getID()));
        List<Account> all_accounts = bm.viewAllAccounts();
        assertTrue(active_accounts.size()==3);
        assertTrue(all_accounts.size()==3);
        assertTrue(all_accounts.equals(active_accounts));
    }
    
    @Test
    public void test_valid_account_delete() {
    	Account a = bm.createAccount(test_account);
    	bm.deleteAccount(a.getID());
    	List<Account> allAccounts = bm.viewAllAccounts();
    	Iterator<Account> acc_iter = allAccounts.listIterator();
    	assertFalse(acc_iter.hasNext());
    }
    
    @Test
    public void test_invalid_account_create_and_update() {    	
    	// test for missing name
    	String exp_error = "Name is missing!";
    	String actual_error = "";
    	try {
        	bm.createAccount(invalid_test_account);
    	}
    	catch (AssertionError e) {
    		actual_error = e.getMessage();
    	}
    	assertEquals(exp_error, actual_error);
		invalid_test_account.updateName("Jackie McAninch");
    	
    	// test for missing address
    	exp_error = "Street is missing!";
    	try {
        	bm.createAccount(invalid_test_account);
    	}
    	catch (AssertionError e) {
    		actual_error = e.getMessage();
    	}
    	assertEquals(exp_error, actual_error);
		invalid_test_account.updateAddress("3201 S State St", null);
		
    	exp_error = "Zip code is missing!";
    	try {
        	bm.createAccount(invalid_test_account);
    	}
    	catch (AssertionError e) {
    		actual_error = e.getMessage();
    	}
    	assertEquals(exp_error, actual_error);
		invalid_test_account.updateAddress("3201 S State St", "60616");
    	
    	// test for missing phone number
    	exp_error = "Phone number is missing!";
    	try {
    		bm.createAccount(invalid_test_account);
    	}
    	catch (AssertionError e) {
    		actual_error = e.getMessage();
    	}
    	assertEquals(exp_error, actual_error);
		invalid_test_account.updatePhone("517-358-1025");
    	
    	// test for missing picture
    	exp_error = "Picture is missing!";
    	try {
    		bm.createAccount(invalid_test_account);
    	}
    	catch (AssertionError e) {
    		actual_error = e.getMessage();
    	}
    	assertEquals(exp_error, actual_error);
		invalid_test_account.updatePicture("<link to my picture>");
    	
    	// test account is now valid
    	assertFalse(bm.createAccount(invalid_test_account).isNil());
    }
    
    @Test
    public void test_activate_update_delete_and_view_null_account() {
        Account a = bm.createAccount(test_account);
        boolean error_caught = false;
        String bad_uid = "asdf";
        
        // test activate null account
        try {
            bm.activateAccount(bad_uid);        	
        }
        catch (NoSuchElementException e) {
        	error_caught = true;
        }
        assertTrue(error_caught);
        
        // test update null account
        error_caught = false;
        try {
        	bm.updateAccount(bad_uid, a);
        }
        catch (NoSuchElementException e) {
        	error_caught = true;
        }
        assertTrue(error_caught);
        
        // test update null account
        error_caught = false;
        try {
        	bm.deleteAccount(bad_uid);
        }
        catch (NoSuchElementException e) {
        	error_caught = true;
        }
        assertTrue(error_caught);
        
        // test view null account
        error_caught = false;
        try {
        	bm.viewAccount(bad_uid);
        }
        catch (NoSuchElementException e) {
        	error_caught = true;
        }
        assertTrue(error_caught);
    }
    
    @Test
    public void test_search_accounts() {
    	Account a = bm.createAccount(test_account);
    	bm.activateAccount(a.getID());
    	
    	// test reject bad dates
    	boolean error_caught = false;
    	try {
    		bm.searchAccounts("key", "bad date", "another bad date");
    	}
    	catch (AssertionError e) {
    		error_caught = true;
    	}
    	assertTrue(error_caught);

		String start = "01-Jan-2001";
		String end = "01-Jan-2023";
    	error_caught = false;
    	try {
    		bm.searchAccounts("key", end, start);
    	}
    	catch (AssertionError e) {
    		error_caught = true;
    	}
    	assertTrue(error_caught);
    	
    	// test keywords
    	List<Account> result = new ArrayList<Account>();
    	result = bm.searchAccounts("Jackie", start, end);
    	assertTrue(result.size()==1);
    	result = bm.searchAccounts("McAninch", start, end);
    	assertTrue(result.size()==1);
    	result = bm.searchAccounts("State", start, end);
    	assertTrue(result.size()==1);
    	result = bm.searchAccounts("517-358-1025", start, end);
    	assertTrue(result.size()==1);
    	result = bm.searchAccounts("60616", start, end);
    	assertTrue(result.size()==1);
    	result = bm.searchAccounts("badkeyword", start, end);
    	assertTrue(result.size()==0);
    	result = bm.searchAccounts("Jackie", start, "01-Jan-2010");
    	assertTrue(result.size()==0);
    	result = bm.searchAccounts("Jackie", "01-Jan-2023", "01-Jan-2025");
    	assertTrue(result.size()==0);
    	result = bm.searchAccounts(null, start, end);
    	assertTrue(result.size()==1);
    }
    
    @Test
    public void test_add_and_find_account() {
    	Account a = bm.createAccount(test_account);
        Account found_a = bm.findAccountByID(a.getID());
        assertTrue(a.equals(found_a));
    }
    
    
    // ASK TESTS
    @Test void test_ask_equals() {
    	// create new active account to branch from
    	Account new_acc = bm.createAccount(test_account);
    	bm.activateAccount(new_acc.getID());
    	test_ask.updateAccountID(new_acc.getID());
    	
    	// test two equivalent asks
    	Ask a1 = bm.createAsk(new_acc.getID(), test_ask);
    	Ask a2 = bm.createAsk(new_acc.getID(), test_ask);
    	assertTrue(a1.equals(a2));
    	
    	// differ by type
    	a1.updateType("borrow");
    	assertFalse(a1.equals(a2));
    	a1.updateType(a2.getType());
    	
    	// differ by description
    	a1.updateDescription("new description");
    	assertFalse(a1.equals(a2));
    	a1.updateDescription(a2.getDescription());
    	
    	// differ by start date
    	a1.updateStartDate("2022-04-30");
    	assertFalse(a1.equals(a2));
    	a1.updateStartDate(a2.getStartDate());
    	
    	// differ by end date
    	a1.updateEndDate("2022-05-30");
    	assertFalse(a1.equals(a2));
    	a1.updateEndDate(a2.getEndDate());
    	
    	// differ by zip code
    	String[] newzips = {"12345", "23456", "34567", "45678"};
    	a1.updateExtraZip(newzips);
    	assertFalse(a1.equals(a2));
    	a1.updateExtraZip(a2.getExtraZip());
    	
    	// test comparison to null ask object
    	Ask null_ask = bm.findAskByID("bad id");
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
    	// create new active account to branch from
    	Account new_acc = bm.createAccount(test_account);
    	bm.activateAccount(new_acc.getID());
    	test_ask.updateAccountID(new_acc.getID());
    	
    	// create new valid ask
        Ask a = bm.createAsk(test_ask.getAccountID(), test_ask);
        assertTrue(a.equals(test_ask));
        
        // update ask information
        Ask new_ask = new Ask();
        new_ask.updateAccountID(new_acc.getID());
        new_ask.updateType("borrow");
        new_ask.updateDescription("new description");
        new_ask.updateStartDate("2022-04-30");
        new_ask.updateEndDate("2022-05-30");
    	String[] newzips = {"12345", "23456", "34567", "45678"};
        new_ask.updateExtraZip(newzips);
        bm.updateAsk(a.getID(), new_ask);
        assertTrue(a.equals(new_ask));
    }
    
    @Test
    public void test_view_asks() {
    	// create new active account to branch from
    	Account acc = bm.createAccount(test_account);
    	test_account.elevatePrivileges();
    	Account privileged_acc = bm.createAccount(test_account);
    	bm.activateAccount(acc.getID());
    	bm.activateAccount(privileged_acc.getID());
    	test_ask.updateAccountID(acc.getID());
    	
    	// create three valid asks
        Ask ask1 = bm.createAsk(acc.getID(), test_ask);
        Ask ask2 = bm.createAsk(acc.getID(), test_ask);
        Ask ask3 = bm.createAsk(acc.getID(), test_ask);
        
        // collect all active asks
        List<Ask> invalid_view_asks = bm.viewAsks("", "");
        List<Ask> regular_view_asks = bm.viewAsks(acc.getID(),"");
        List<Ask> privileged_view_asks = bm.viewAsks(privileged_acc.getID(), "");

        // test expected group results
        assertTrue(invalid_view_asks.size()==0);
        assertTrue(regular_view_asks.size()==3);
        assertTrue(privileged_view_asks.size()==3);
        assertTrue(regular_view_asks.equals(privileged_view_asks));
        
        // test individual results
        assertTrue(ask1.equals(bm.viewAsk(ask1.getID())));
        assertTrue(ask2.equals(bm.viewAsk(ask2.getID())));
        assertTrue(ask3.equals(bm.viewAsk(ask3.getID())));        
    }
    
    @Test
    public void test_valid_ask_delete_deactivate() {
    	// create new active account to branch from
    	Account new_acc = bm.createAccount(test_account);
    	bm.activateAccount(new_acc.getID());
    	test_ask.updateAccountID(new_acc.getID());
    	
    	// create CSR account
    	test_account.elevatePrivileges();
    	Account admin = bm.createAccount(test_account);
    	bm.activateAccount(admin.getID());
    	Ask test_ask2 = new Ask(test_ask);
    	test_ask2.updateAccountID(admin.getID());
    	
    	// create valid asks
    	Ask a1 = bm.createAsk(new_acc.getID(), test_ask);
    	Ask a2 = bm.createAsk(new_acc.getID(), test_ask);
    	bm.createAsk(new_acc.getID(), test_ask);
    	bm.createAsk(admin.getID(), test_ask2);
    	
    	// alter list of all asks
    	bm.deactivateAsk(new_acc.getID(), a1.getID());
    	bm.deleteAsk(new_acc.getID(), a2.getID());
    	
    	// check that views filter correctly
    	List<Ask> subset = new ArrayList<Ask>();
    	subset = bm.viewAsks(new_acc.getID(), "");
    	assertTrue(subset.size()==2);
    	subset = bm.viewAsks(admin.getID(), "");
    	assertTrue(subset.size()==3);
    	subset = bm.viewAsks(admin.getID(),"true");
    	assertTrue(subset.size()==2);
    	subset = bm.viewAsks(admin.getID(),"false");
    	assertTrue(subset.size()==1);
    	
    	// check that user views filter correctly
    	subset = bm.viewMyAsks(new_acc.getID(), "");
    	assertTrue(subset.size()==2);
    	subset = bm.viewMyAsks(new_acc.getID(), "true");
    	assertTrue(subset.size()==1);
    	subset = bm.viewMyAsks(new_acc.getID(),"false");
    	assertTrue(subset.size()==1);
    	
    	// check invalid input
    	boolean error_caught = false;
    	try {
    		bm.viewAsks(admin.getID(), "true/false"); // invalid value
    	}
    	catch (AssertionError e) {
    		error_caught = true;
    	}
    	assertTrue(error_caught);
    	try {
    		bm.viewAsks(new_acc.getID(), "true/false"); // invalid value
    	}
    	catch (AssertionError e) {
    		error_caught = true;
    	}
    	assertTrue(error_caught);
    }
    
    @Test
    public void test_invalid_ask_create_and_update() {
    	// create new active account to branch from
    	Account new_acc = bm.createAccount(test_account);
    	bm.activateAccount(new_acc.getID());
    	invalid_test_ask.updateAccountID(new_acc.getID());
    	
    	// test for missing type
    	String exp_error = "Type is missing!";
    	String actual_error = "";
    	try {
        	bm.createAsk(new_acc.getID(), invalid_test_ask);
    	}
    	catch (AssertionError e) {
    		actual_error = e.getMessage();
    	}
    	assertEquals(exp_error, actual_error);
		invalid_test_ask.updateType("borrrow"); // will test this invalid type later
    	
    	// test for missing description
    	exp_error = "Description is missing!";
    	try {
    		bm.createAsk(new_acc.getID(), invalid_test_ask);
    	}
    	catch (AssertionError e) {
    		actual_error = e.getMessage();
    	}
    	assertEquals(exp_error, actual_error);
		invalid_test_ask.updateDescription("new description");
    	
    	// test for missing start date
    	exp_error = "Start date is missing!";
    	try {
    		bm.createAsk(new_acc.getID(), invalid_test_ask);
    	}
    	catch (AssertionError e) {
    		actual_error = e.getMessage();
    	}
    	assertEquals(exp_error, actual_error);
		invalid_test_ask.updateStartDate("2022-04-26");
		
		// test for invalid type
    	exp_error = "Ask has invalid type.";
    	try {
    		bm.createAsk(new_acc.getID(), invalid_test_ask);
    	}
    	catch (AssertionError e) {
    		actual_error = e.getMessage();
    	}
    	assertEquals(exp_error, actual_error);
		invalid_test_ask.updateType("borrow");
    	
    	// test ask is now valid
    	assertFalse(bm.createAsk(new_acc.getID(), invalid_test_ask).isNil());
    }
    
    @Test
    public void test_update_delete_and_view_null_ask() {
    	// create new active account to branch from
    	Account new_acc = bm.createAccount(test_account);
    	bm.activateAccount(new_acc.getID());
    	test_ask.updateAccountID(new_acc.getID());
    	
    	// create ask from account
        Ask a = bm.createAsk(new_acc.getID(), test_ask);
        boolean error_caught = false;
        String bad_uid = "asdf";
                
        // test update null ask
        error_caught = false;
        try {
        	bm.updateAsk(bad_uid, a);
        }
        catch (NoSuchElementException e) {
        	error_caught = true;
        }
        assertTrue(error_caught);
        
        // test update null ask
        error_caught = false;
        try {
        	bm.deleteAsk(a.getAccountID(), bad_uid);
        }
        catch (NoSuchElementException e) {
        	error_caught = true;
        }
        assertTrue(error_caught);
        
        // test view null ask
        error_caught = false;
        try {
        	bm.viewAsk(bad_uid);
        }
        catch (NoSuchElementException e) {
        	error_caught = true;
        }
        assertTrue(error_caught);
    }
    
    @Test
    public void test_search_asks() {
    	// create new active account to branch from
    	Account new_acc = bm.createAccount(test_account);
    	bm.activateAccount(new_acc.getID());
    	test_ask.updateAccountID(new_acc.getID());
    	
    	bm.createAsk(new_acc.getID(), test_ask);
    	
    	// test reject bad dates
    	boolean error_caught = false;
    	try {
    		bm.searchAsks("key", "bad date", "another bad date");
    	}
    	catch (AssertionError e) {
    		error_caught = true;
    	}
    	assertTrue(error_caught);

		String start = "01-Jan-2001";
		String end = "01-Jan-2023";
    	error_caught = false;
    	try {
    		bm.searchAsks("key", end, start);
    	}
    	catch (AssertionError e) {
    		error_caught = true;
    	}
    	assertTrue(error_caught);
    	
    	// test keywords
    	List<Ask> result = new ArrayList<Ask>();
    	result = bm.searchAsks("gift", start, end);
    	assertTrue(result.size()==1);
    	result = bm.searchAsks("I am looking for a homemade batch of cookies.", start, end);
    	assertTrue(result.size()==1);
    	result = bm.searchAsks("60608", start, end);
    	assertTrue(result.size()==1);
    	result = bm.searchAsks("bad keyword", start, end);
    	assertTrue(result.size()==0);
    	result = bm.searchAsks("gift", start, "01-Jan-2010");
    	assertTrue(result.size()==0);
    	result = bm.searchAsks(null, start, end);
    	assertTrue(result.size()==1);
    }
    
    
    // GIVE TESTS
    @Test 
    public void test_give_equals() {
    	// create new active account to branch from
    	Account new_acc = bm.createAccount(test_account);
    	bm.activateAccount(new_acc.getID());
    	test_give.updateAccountID(new_acc.getID());
    	
    	// test two equivalent gives
    	Give g1 = bm.createGive(new_acc.getID(), test_give);
    	Give g2 = bm.createGive(new_acc.getID(), test_give);    	
    	assertTrue(g1.equals(g2));
    	
    	// differ by type
    	g1.updateType("gift");
    	assertFalse(g1.equals(g2));
    	g1.updateType(g2.getType());
    	
    	// differ by description
    	g1.updateDescription("new description");
    	assertFalse(g1.equals(g2));
    	g1.updateDescription(g2.getDescription());
    	
    	// differ by start date
    	g1.updateStartDate("2022-04-30");
    	assertFalse(g1.equals(g2));
    	g1.updateStartDate(g2.getStartDate());
    	
    	// differ by end date
    	g1.updateEndDate("2022-05-30");
    	assertFalse(g1.equals(g2));
    	g1.updateEndDate(g2.getEndDate());
    	
    	// differ by zip code
    	String[] newzips = {"12345", "23456", "34567", "45678"};
    	g1.updateExtraZip(newzips);
    	assertFalse(g1.equals(g2));
    	g1.updateExtraZip(g2.getExtraZip());
    	
    	// test comparison to null give object
    	Give null_give = bm.findGiveByID("bad id");
    	boolean error_caught = false;
    	try {
    		g1.equals(null_give);
    	}
    	catch (NoSuchElementException e) {
    		error_caught = true;
    	}
    	assertTrue(error_caught);
    }
    
    @Test
    public void test_valid_give_create_and_update() {
    	// create new active account to branch from
    	Account new_acc = bm.createAccount(test_account);
    	bm.activateAccount(new_acc.getID());
    	test_give.updateAccountID(new_acc.getID());
    	
    	// create new valid give
        Give g = bm.createGive(test_give.getAccountID(), test_give);
        assertTrue(g.equals(test_give));
        
        // update give information
        Give new_give = new Give();
        new_give.updateAccountID(new_acc.getID());
        new_give.updateType("lend");
        new_give.updateDescription("new description");
        new_give.updateStartDate("30-Apr-2022");
        new_give.updateEndDate("30-May-2022");
    	String[] new_zips = {"12345", "23456", "34567", "45678"};
        new_give.updateExtraZip(new_zips);
        bm.updateGive(g.getID(), new_give);
        assertTrue(g.equals(new_give));
    }
    
    @Test
    public void test_view_gives() {
    	// create new active account to branch from
    	Account acc = bm.createAccount(test_account);
    	test_account.elevatePrivileges();
    	Account privileged_acc = bm.createAccount(test_account);
    	bm.activateAccount(acc.getID());
    	bm.activateAccount(privileged_acc.getID());
    	test_give.updateAccountID(acc.getID());
    	
    	// create three valid gives
        Give give1 = bm.createGive(acc.getID(), test_give);
        Give give2 = bm.createGive(acc.getID(), test_give);
        Give give3 = bm.createGive(acc.getID(), test_give);
        
        // collect all active gives
        List<Give> invalid_view_gives = bm.viewGives("", "");
        List<Give> regular_view_gives = bm.viewGives(acc.getID(),"");
        List<Give> privileged_view_gives = bm.viewGives(privileged_acc.getID(), "");

        // test expected group results
        assertTrue(invalid_view_gives.size()==0);
        assertTrue(regular_view_gives.size()==3);
        assertTrue(privileged_view_gives.size()==3);
        assertTrue(regular_view_gives.equals(privileged_view_gives));
        
        // test individual results
        assertTrue(give1.equals(bm.viewGive(give1.getID())));
        assertTrue(give2.equals(bm.viewGive(give2.getID())));
        assertTrue(give3.equals(bm.viewGive(give3.getID())));        
    }
    
    @Test
    public void test_valid_give_delete_and_deactivate() {
    	// create new active account to branch from
    	Account new_acc = bm.createAccount(test_account);
    	bm.activateAccount(new_acc.getID());
    	test_give.updateAccountID(new_acc.getID());
    	
    	// create CSR account
    	test_account.elevatePrivileges();
    	Account admin = bm.createAccount(test_account);
    	bm.activateAccount(admin.getID());
    	Give test_give2 = new Give(test_give);
    	test_give2.updateAccountID(admin.getID());
    	
    	// create valid gives
    	Give g1 = bm.createGive(new_acc.getID(), test_give);
    	Give g2 = bm.createGive(new_acc.getID(), test_give);
    	bm.createGive(new_acc.getID(), test_give);
    	bm.createGive(admin.getID(), test_give2);
    	
    	// alter list of all gives
    	bm.deactivateGive(new_acc.getID(), g1.getID());
    	bm.deleteGive(new_acc.getID(), g2.getID());
    	
    	// check that views filter correctly
    	List<Give> subset = new ArrayList<Give>();
    	subset = bm.viewGives(new_acc.getID(), "");
    	assertTrue(subset.size()==2);
    	subset = bm.viewGives(admin.getID(), "");
    	assertTrue(subset.size()==3);
    	subset = bm.viewGives(admin.getID(),"true");
    	assertTrue(subset.size()==2);
    	subset = bm.viewGives(admin.getID(),"false");
    	assertTrue(subset.size()==1);
    	
    	// check that user views filter correctly
    	subset = bm.viewMyGives(new_acc.getID(), "");
    	assertTrue(subset.size()==2);
    	subset = bm.viewMyGives(new_acc.getID(), "true");
    	assertTrue(subset.size()==1);
    	subset = bm.viewMyGives(new_acc.getID(),"false");
    	assertTrue(subset.size()==1);
    	
    	// check invalid input
    	boolean error_caught = false;
    	try {
    		bm.viewGives(admin.getID(), "true/false"); // invalid value
    	}
    	catch (AssertionError e) {
    		error_caught = true;
    	}
    	assertTrue(error_caught);
    	try {
    		bm.viewGives(new_acc.getID(), "true/false"); // invalid value
    	}
    	catch (AssertionError e) {
    		error_caught = true;
    	}
    	assertTrue(error_caught);
    }
    
    @Test
    public void test_invalid_give_create_and_update() {
    	// create new active account to branch from
    	Account new_acc = bm.createAccount(test_account);
    	bm.activateAccount(new_acc.getID());
    	invalid_test_give.updateAccountID(new_acc.getID());
    	
    	// test for missing type
    	String exp_error = "Type is missing!";
    	String actual_error = "";
    	try {
        	bm.createGive(new_acc.getID(), invalid_test_give);
    	}
    	catch (AssertionError e) {
    		actual_error = e.getMessage();
    	}
    	assertEquals(exp_error, actual_error);
		invalid_test_give.updateType("gifft"); // will test this invalid type later
    	
    	// test for missing description
    	exp_error = "Description is missing!";
    	try {
    		bm.createGive(new_acc.getID(), invalid_test_give);
    	}
    	catch (AssertionError e) {
    		actual_error = e.getMessage();
    	}
    	assertEquals(exp_error, actual_error);
		invalid_test_give.updateDescription("new description");
    	
    	// test for missing start date
    	exp_error = "Start date is missing!";
    	try {
    		bm.createGive(new_acc.getID(), invalid_test_give);
    	}
    	catch (AssertionError e) {
    		actual_error = e.getMessage();
    	}
    	assertEquals(exp_error, actual_error);
		invalid_test_give.updateStartDate("26-Apr-2022");
		
		// test for invalid type
    	exp_error = "Give has invalid type.";
    	try {
    		bm.createGive(new_acc.getID(), invalid_test_give);
    	}
    	catch (AssertionError e) {
    		actual_error = e.getMessage();
    	}
    	assertEquals(exp_error, actual_error);
		invalid_test_give.updateType("lend");
    	
    	// test give is now valid
    	assertFalse(bm.createGive(new_acc.getID(), invalid_test_give).isNil());
    }
    
    @Test
    public void test_update_delete_and_view_null_give() {
    	// create new active account to branch from
    	Account new_acc = bm.createAccount(test_account);
    	bm.activateAccount(new_acc.getID());
    	test_give.updateAccountID(new_acc.getID());
    	
    	// create give from account
        Give g = bm.createGive(new_acc.getID(), test_give);
        boolean error_caught = false;
        String bad_uid = "asdf";
                
        // test update null give
        error_caught = false;
        try {
        	bm.updateGive(bad_uid, g);
        }
        catch (NoSuchElementException e) {
        	error_caught = true;
        }
        assertTrue(error_caught);
        
        // test update null give
        error_caught = false;
        try {
        	bm.deleteGive(g.getAccountID(), bad_uid);
        }
        catch (NoSuchElementException e) {
        	error_caught = true;
        }
        assertTrue(error_caught);
        
        // test view null give
        error_caught = false;
        try {
        	bm.viewGive(bad_uid);
        }
        catch (NoSuchElementException e) {
        	error_caught = true;
        }
        assertTrue(error_caught);
    }
    
    @Test
    public void test_search_gives() {
    	// create new active account to branch from
    	Account new_acc = bm.createAccount(test_account);
    	bm.activateAccount(new_acc.getID());
    	test_give.updateAccountID(new_acc.getID());
    	
    	bm.createGive(new_acc.getID(), test_give);
    	
    	// test reject bad date
    	boolean error_caught = false;
    	try {
    		bm.searchGives("key", "bad date", "another bad date");
    	}
    	catch (AssertionError e) {
    		error_caught = true;
    	}
    	assertTrue(error_caught);
		
    	// test keywords
		String start = "01-Jan-2001";
		String end = "01-Jan-2023";
    	List<Give> result = new ArrayList<Give>();
    	result = bm.searchGives("service", start, end);
    	assertTrue(result.size()==1);
    	result = bm.searchGives("I will bake you a batch of chocolate chip cookies!", start, end);
    	assertTrue(result.size()==1);
    	result = bm.searchGives("60608", start, end);
    	assertTrue(result.size()==1);
    	result = bm.searchGives("bad keyword", start, end);
    	assertTrue(result.size()==0);
    	result = bm.searchGives("service", start, "01-Jan-2010");
    	assertTrue(result.size()==0);
    	result = bm.searchGives(null, start, end);
    	assertTrue(result.size()==1);
    }
    
    // THANK TESTS
    @Test 
    public void test_thank_equals() {
    	// create new active account to branch from
    	Account new_acc = bm.createAccount(test_account);
    	Account receiving_acc = bm.createAccount(test_account);
    	bm.activateAccount(new_acc.getID());
    	bm.activateAccount(receiving_acc.getID());
    	test_thank.updateAccountID(new_acc.getID());
    	test_thank.updateThankTo(receiving_acc.getID());
    	
    	// test two equivalent thanks
    	Thank t1 = bm.createThank(new_acc.getID(), test_thank);
    	Thank t2 = bm.createThank(new_acc.getID(), test_thank);    	
    	assertTrue(t1.equals(t2));
    	
    	// differ by thank to
    	t1.updateThankTo("new account id");
    	assertFalse(t1.equals(t2));
    	t1.updateThankTo(t2.getThankTo());
    	
    	// differ by description
    	t1.updateDescription("new description");
    	assertFalse(t1.equals(t2));
    	t1.updateDescription(t2.getDescription());
    	
    	// test comparison to null thank object
    	Thank null_thank = bm.findThankByID("bad id");
    	boolean error_caught = false;
    	try {
    		t1.equals(null_thank);
    	}
    	catch (NoSuchElementException e) {
    		error_caught = true;
    	}
    	assertTrue(error_caught);
    }
    
    @Test
    public void test_valid_thank_create_and_update() {
    	// create new active account to branch from
    	Account new_acc = bm.createAccount(test_account);
    	Account receiving_acc = bm.createAccount(test_account);
    	bm.activateAccount(new_acc.getID());
    	bm.activateAccount(receiving_acc.getID());
    	test_thank.updateAccountID(new_acc.getID());
    	test_thank.updateThankTo(receiving_acc.getID());
    	
    	// create new valid thank
        Thank t = bm.createThank(test_thank.getAccountID(), test_thank);
        assertTrue(t.equals(test_thank));
        
        // update thank information
        Thank new_thank = new Thank();
        new_thank.updateAccountID(new_acc.getID());
        new_thank.updateThankTo(receiving_acc.getID());
        new_thank.updateDescription("new description");
        bm.updateThank(t.getID(), new_thank);
        assertTrue(t.equals(new_thank));
    }
    
    @Test
    public void test_view_thanks() {
    	// create new active account to branch from
    	Account acc = bm.createAccount(test_account);
    	test_account.elevatePrivileges();
    	Account acc2 = bm.createAccount(test_account);
    	bm.activateAccount(acc.getID());
    	bm.activateAccount(acc2.getID());
    	test_thank.updateAccountID(acc.getID());
    	test_thank.updateThankTo(acc2.getID());
    	Thank test_thank2 = new Thank(test_thank);
    	test_thank2.updateAccountID(acc2.getID());
    	test_thank2.updateThankTo(acc.getID());
    	
    	// create three valid thanks
        Thank thank1 = bm.createThank(acc.getID(), test_thank);
        Thank thank2 = bm.createThank(acc.getID(), test_thank);
        Thank thank3 = bm.createThank(acc.getID(), test_thank);
        bm.createThank(acc2.getID(), test_thank2);
        bm.createThank(acc2.getID(), test_thank2);
        bm.createThank(acc2.getID(), test_thank2);
        
        // test expected group results
        List<Thank> all_thanks = bm.viewAllThanks();
        assertTrue(all_thanks.size()==6);
        
        // test individual results
        assertTrue(thank1.equals(bm.viewThank(thank1.getID())));
        assertTrue(thank2.equals(bm.viewThank(thank2.getID())));
        assertTrue(thank3.equals(bm.viewThank(thank3.getID())));
        
        // test user access
        List<Thank> my_thanks_given = bm.viewMyThanks(acc.getID(), "");
        assertTrue(my_thanks_given.size()==3);
        
        // test thank to results
        List<Thank> my_thanks_received = bm.viewThanksForUser(acc.getID());
        assertTrue(my_thanks_received.size()==3);
        assertFalse(my_thanks_given.equals(my_thanks_received));
    }
    
    @Test
    public void test_invalid_thank_create_and_update() {
    	// create new active account to branch from
    	Account new_acc = bm.createAccount(test_account);
    	Account receiving_acc = bm.createAccount(test_account);
    	bm.activateAccount(new_acc.getID());
    	bm.activateAccount(receiving_acc.getID());
    	invalid_test_thank.updateAccountID(new_acc.getID());
    	
    	// test for missing thank to
    	String exp_error = "Thank To field is missing!";
    	String actual_error = "";
    	try {
        	bm.createThank(new_acc.getID(), invalid_test_thank);
    	}
    	catch (AssertionError e) {
    		actual_error = e.getMessage();
    	}
    	assertEquals(exp_error, actual_error);
		invalid_test_thank.updateThankTo(receiving_acc.getID()); // will test this invalid type later
    	
    	// test for missing description
    	exp_error = "Description is missing!";
    	try {
    		bm.createThank(new_acc.getID(), invalid_test_thank);
    	}
    	catch (AssertionError e) {
    		actual_error = e.getMessage();
    	}
    	assertEquals(exp_error, actual_error);
		invalid_test_thank.updateDescription("new description");
    	
    	// test thank is now valid
    	assertFalse(bm.createThank(new_acc.getID(), invalid_test_thank).isNil());
    }
    
    @Test
    public void test_update_and_view_null_thank() {
    	// create new active account to branch from
    	Account new_acc = bm.createAccount(test_account);
    	Account receiving_acc = bm.createAccount(test_account);
    	bm.activateAccount(new_acc.getID());
    	bm.activateAccount(receiving_acc.getID());
    	test_thank.updateAccountID(new_acc.getID());
    	test_thank.updateThankTo(receiving_acc.getID());
    	
    	// create thank from account
        Thank t = bm.createThank(new_acc.getID(), test_thank);
        boolean error_caught = false;
        String bad_uid = "asdf";
                
        // test update null thank
        error_caught = false;
        try {
        	bm.updateThank(bad_uid, t);
        }
        catch (NoSuchElementException e) {
        	error_caught = true;
        }
        assertTrue(error_caught);
        
        // test view null thank
        error_caught = false;
        try {
        	bm.viewThank(bad_uid);
        }
        catch (NoSuchElementException e) {
        	error_caught = true;
        }
        assertTrue(error_caught);
    }
    
    @Test
    public void test_search_thanks() {
    	// create new active account to branch from
    	Account new_acc = bm.createAccount(test_account);
    	Account receiving_acc = bm.createAccount(test_account);
    	bm.activateAccount(new_acc.getID());
    	bm.activateAccount(receiving_acc.getID());
    	test_thank.updateAccountID(new_acc.getID());
    	test_thank.updateThankTo(receiving_acc.getID());
    	
    	bm.createThank(new_acc.getID(), test_thank);
    	
    	// test reject bad date
    	boolean error_caught = false;
    	try {
    		bm.searchThanks("key", "bad date", "another bad date");
    	}
    	catch (AssertionError e) {
    		error_caught = true;
    	}
    	assertTrue(error_caught);
		
    	// test keywords
		String start = "01-Jan-2001";
		String end = "01-Jan-2023";
    	List<Thank> result = new ArrayList<Thank>();
    	result = bm.searchThanks("Thank you for the cookies :)", start, end);
    	assertTrue(result.size()==1);
    	result = bm.searchThanks("bad keyword", start, end);
    	assertTrue(result.size()==0);
    	result = bm.searchThanks(null, start, end);
    	assertTrue(result.size()==1);
    }
    
    // NOTE TESTS
    @Test 
    public void test_note_equals() {
    	// create new active account to branch from
    	Account new_acc = bm.createAccount(test_account);
    	Account receiving_acc = bm.createAccount(test_account);
    	bm.activateAccount(new_acc.getID());
    	bm.activateAccount(receiving_acc.getID());
    	
    	// create a source object to address the note to
    	test_ask.updateAccountID(receiving_acc.getID());
    	Ask source_obj = bm.createAsk(receiving_acc.getID(), test_ask);
    	test_note.updateAccountID(new_acc.getID());
    	test_note.updateToUserID(receiving_acc.getID());
    	test_note.updateToID(source_obj.getID());
    	
    	// test two equivalent notes
    	Note n1 = bm.createNote(test_note);
    	Note n2 = bm.createNote(test_note);    	
    	assertTrue(n1.equals(n2));
    	
    	// differ by to type
    	n1.updateToType("new to type");
    	assertFalse(n1.equals(n2));
    	n1.updateToType(n2.getToType());
    	
    	// differ by to user id
    	n1.updateToUserID("new to user id");
    	assertFalse(n1.equals(n2));
    	n1.updateToUserID(n2.getToUserID());
    	
    	// differ by to id
    	n1.updateToID("new to id");
    	assertFalse(n1.equals(n2));
    	n1.updateToID(n2.getToType());
    	
    	// differ by description
    	n1.updateDescription("new description");
    	assertFalse(n1.equals(n2));
    	n1.updateDescription(n2.getDescription());
    	
    	// test comparison to null note object
    	Note null_note = bm.findNoteByID("bad id");
    	boolean error_caught = false;
    	try {
    		n1.equals(null_note);
    	}
    	catch (NoSuchElementException e) {
    		error_caught = true;
    	}
    	assertTrue(error_caught);
    }
    
    @Test
    public void test_valid_note_create_and_update() {
    	// create new active account to branch from
    	Account new_acc = bm.createAccount(test_account);
    	Account receiving_acc = bm.createAccount(test_account);
    	bm.activateAccount(new_acc.getID());
    	bm.activateAccount(receiving_acc.getID());
    	
    	// create a source object to address the note to
    	test_ask.updateAccountID(receiving_acc.getID());
    	Ask source_obj = bm.createAsk(receiving_acc.getID(), test_ask);
    	test_note.updateAccountID(new_acc.getID());
    	test_note.updateToUserID(receiving_acc.getID());
    	test_note.updateToID(source_obj.getID());
    	
    	// create new valid note
        Note n = bm.createNote(test_note);
        assertTrue(n.equals(test_note));
        
        // update note information
        Note new_note = new Note();
        new_note.updateAccountID(new_acc.getID());
        new_note.updateToType("give");
        new_note.updateToUserID(receiving_acc.getID());
        new_note.updateToID(source_obj.getID());
        new_note.updateDescription("Can I cop those cookies?");
        bm.updateNote(n.getID(), new_note);
        assertTrue(n.equals(new_note));
    }
}

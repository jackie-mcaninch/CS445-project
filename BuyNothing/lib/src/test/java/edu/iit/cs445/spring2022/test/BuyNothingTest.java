package edu.iit.cs445.spring2022.test;

import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.iit.cs445.spring2022.buynothing.*;

public class BuyNothingTest {
	AccountManager am = new AccountManager();
    Account test_account = new Account();
    Account invalid_test_account = new Account();

    @BeforeEach
    public void setUp() throws Exception {
        test_account.updateName("Jackie McAninch");
        test_account.updateAddress("3201 S State St", "60616");
        test_account.updatePhone("517-358-1025");
        test_account.updatePicture("<link to my picture>");
        am.clearAllAccounts();
    }

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
    		invalid_test_account.updateName("Jackie McAninch");
    	}
    	assertTrue(exp_error.equals(actual_error));
    	
    	// test for missing address
    	exp_error = "Street is missing!";
    	try {
        	am.createAccount(invalid_test_account);
    	}
    	catch (AssertionError e) {
    		actual_error = am.assessMissingInfo(invalid_test_account);
    		invalid_test_account.updateAddress("3201 S State St", null);
    	}
    	assertTrue(exp_error.equals(actual_error));
    	exp_error = "Zip code is missing!";
    	try {
        	am.createAccount(invalid_test_account);
    	}
    	catch (AssertionError e) {
    		actual_error = am.assessMissingInfo(invalid_test_account);
    		invalid_test_account.updateAddress("3201 S State St", "60616");
    	}
    	assertTrue(exp_error.equals(actual_error));
    	
    	// test for missing phone number
    	exp_error = "Phone number is missing!";
    	try {
    		am.createAccount(invalid_test_account);
    	}
    	catch (AssertionError e) {
    		actual_error = am.assessMissingInfo(invalid_test_account);
    		invalid_test_account.updatePhone("517-358-1025");
    	}
    	assertTrue(exp_error.equals(actual_error));
    	
    	// test for missing picture
    	exp_error = "Picture is missing!";
    	try {
    		am.createAccount(invalid_test_account);
    	}
    	catch (AssertionError e) {
    		actual_error = am.assessMissingInfo(invalid_test_account);
    		invalid_test_account.updatePicture("<link to my picture>");
    	}
    	assertTrue(exp_error.equals(actual_error));
    	
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
    	result = am.searchAccounts(null, start, end);
    	assertTrue(result.size()==1);
    }
}


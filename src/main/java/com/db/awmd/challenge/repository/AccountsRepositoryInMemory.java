package com.db.awmd.challenge.repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.Transfer;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.InsufficiantBalanceException;
import com.db.awmd.challenge.exception.InvalidAccountException;
import com.db.awmd.challenge.service.EmailNotificationService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.el.lang.ELArithmetic.BigDecimalDelegate;
import org.springframework.stereotype.Repository;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {
	
	public EmailNotificationService notificationService =  new EmailNotificationService();

  private final Map<String, Account> accounts = new ConcurrentHashMap<>();

  @Override
  public void createAccount(Account account) throws DuplicateAccountIdException {
    Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
    if (previousAccount != null) {
      throw new DuplicateAccountIdException(
        "Account id " + account.getAccountId() + " already exists!");
    }
  }

  @Override
  public Account getAccount(String accountId) {
    return accounts.get(accountId);
  }

  @Override
  public void clearAccounts() {
    accounts.clear();
  }

  
@Override
public void transferBalance(Transfer transaction) throws InsufficiantBalanceException, InvalidAccountException {
	String fromAccountID = transaction.getFromAccountId();
	String toAccountID = transaction.getToAccountId();
	BigDecimal amountToTransfer = transaction.getAmountToTransfer();
	
	Account fromAccount = getAccount(fromAccountID);
	
	

	verifyAccounts(fromAccountID);
	verifyAccounts(toAccountID);
	
	notificationService.notifyAboutTransfer(fromAccount, "Request to transfer amount " + amountToTransfer + " to account " + toAccountID + " is recevied.");
	
	if(!verifySufficientBalance(fromAccountID,amountToTransfer ));
		notificationService.notifyAboutTransfer(fromAccount, "Request to transfer amount " + amountToTransfer + " to account " + toAccountID + " can not be compelted due to insuffcient balance.");
	
	updateAccountBalance(transaction);
	
	
}

@Override
public void verifyAccounts(String accountID)
{
	Account fromAccount = getAccount(accountID);
	if(fromAccount == null)
		throw new InvalidAccountException("Invalid Account specified... Transfer Trasaction cannot be completed");
	
}

@Override
public boolean verifySufficientBalance(String fromAccountID, BigDecimal amountToTransfer )
{
	Account fromAccount = getAccount(fromAccountID);
	
	if(fromAccount.getBalance().compareTo(amountToTransfer) < 0)
	
		
		
		throw new InsufficiantBalanceException("Balance in account " + fromAccountID +" is not sufficient to complete transfer");
	
	return true;
}


@Override
public void updateAccountBalance(Transfer transaction)
{
	String fromAccountID = transaction.getFromAccountId();
	String toAccountID = transaction.getToAccountId();
	BigDecimal amountToTransfer = transaction.getAmountToTransfer();
	
	Account fromAccount = getAccount(fromAccountID);
	fromAccount.setBalance(fromAccount.getBalance().subtract(amountToTransfer));
	
	
	Account toAccount = getAccount(toAccountID);
	toAccount.setBalance(toAccount.getBalance().add(amountToTransfer));
	
	
	accounts.replace(fromAccountID, fromAccount);
	notificationService.notifyAboutTransfer(fromAccount, "Your account is debited by" + amountToTransfer + ". Your updated account balance is " + getAccount(fromAccountID).getBalance());
	
	
	accounts.replace(toAccountID, toAccount);
	notificationService.notifyAboutTransfer(fromAccount, "Your account is credit by " + amountToTransfer + " received   from "+ fromAccountID +". Your updated account balance is " + getAccount(toAccountID).getBalance());
	
	
	
}
}




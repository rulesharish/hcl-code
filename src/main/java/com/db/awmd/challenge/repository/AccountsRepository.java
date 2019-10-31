package com.db.awmd.challenge.repository;

import java.math.BigDecimal;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.Transfer;
import com.db.awmd.challenge.exception.*;

public interface AccountsRepository {

  void createAccount(Account account) throws DuplicateAccountIdException;

  Account getAccount(String accountId);

  void clearAccounts();
  
  void transferBalance(Transfer transaction) throws InsufficiantBalanceException, InvalidAccountException;
  
  void updateAccountBalance(Transfer transaction);
  
  boolean verifySufficientBalance(String fromAccountID, BigDecimal amountToTransfer );
  
  boolean verifyAccounts(String accountID);
}

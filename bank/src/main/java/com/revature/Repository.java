package com.revature;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import com.revature.exceptions.RepositoryException;
import com.revature.exceptions.*;
import com.revature.exceptions.customexceptions.*;

import java.util.ArrayList;
import com.revature.utility.ConnectionFactory;
import com.revature.utility.MoneyUtils;

public class Repository {
    // Adds a new account after a user registers - Ydur
    public static void addAccount() {

    }

    // Gets the accountID and PIN to verify the credentials when logging in - Yousef
    public static Account getAccount(String AccountID, String pin) throws AccountNotFoundException, DatabaseException {
        String query = "select account_id, pin_hash " +
                        "from accounts " +
                        "where account_id = ? " +
                        "and pin_hash = ?";
        String debugAccID = "select account_id " +
                            "from accounts " +
                            "where account_id = ? " +
                            "";
        String debugPin = "select pin_hash " +
                          "from accounts " +
                          "where pin_hash = ?";
        String resAccountID = null;
        String resPin = null;
        try(Connection connection = ConnectionFactory.getAutoCommitConnect();
            PreparedStatement statement = connection.prepareStatement(query); ){
                statement.setString(1, AccountID);
                statement.setString(2, pin);
                ResultSet res = statement.executeQuery();
                if(res.next()) {
                    resAccountID = res.getString("account_id");
                    resPin = res.getString("pin_hash");
                    return new Account(resAccountID,resPin);
                }
                else {
                    PreparedStatement checkAccID = connection.prepareStatement(debugAccID);
                    checkAccID.setString(1, AccountID);
                    PreparedStatement checkPin = connection.prepareStatement(debugPin);
                    checkPin.setString(1, pin);
                    ResultSet resAccID = checkAccID.executeQuery();
                    ResultSet resP = checkPin.executeQuery();
                    boolean hasCorrectAcc = resAccID.next();
                    boolean hasCorrectPin = resP.next();
                    checkAccID.close();
                    checkPin.close();
                    // this is where we would log to the debugger which credential was not correct.
                    //it is important to not pass this logging down to the console as
                    //this is information only we the developers should be able to see
                    if(!hasCorrectAcc && hasCorrectPin) {
                        throw new AccountNotFoundException("No record contains the Account ID " + AccountID + " in the accounts table.");
                    }
                    else if (!hasCorrectPin && hasCorrectAcc) {
                        throw new AccountNotFoundException("No record contains the pin " + pin + " in the accounts table");
                    }
                    else {
                        throw new AccountNotFoundException("No record contains the Account ID " + AccountID +
                                                            " nor does any record contain the pin " + pin +
                                                            " in the accounts table");
                    }

                }

        } catch(SQLException e) {
           throw new DatabaseException("Database error during Account retrieval: ", e);
        }
    }

    // Might not even need this - Yousef
    public static BigDecimal getBalance(String accountID) throws AccountNotFoundException, DatabaseException {
        // assuming that accountID is unique
        String query = "SELECT balance_cents" +
                        "FROM accounts " +
                        "WHERE account_id = ?";

        try (
                Connection conn = ConnectionFactory.getAutoCommitConnect();
                PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setString(1, accountID);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // process the result
                return MoneyUtils.centsToDollars(rs.getInt("balance_cents"));
            } else {
                // TODO: similarly to what Yousef specified: this is where the logging would be

                // assuming the AccountNotFoundException is implemented
                throw new AccountNotFoundException("Account not found: " + accountID);
            }
        } catch (SQLException e) {
            // assuming the DatabaseException is implemented
            throw new DatabaseException("Error occurred while fetching account balance", e);
        }
    }

    // (updateBalance) Updates the value of balance during deposits and withdraws - Connor
    public static int updateBalance(String accountID, BigDecimal amount) throws RepositoryException {
        String sqlQuery = "UPDATE accounts SET balance = ? where accountID = ?";
        try (
            Connection connection = ConnectionFactory.getAutoCommitConnect();
            PreparedStatement ps = connection.prepareStatement(sqlQuery);
        ) {
            int cents = MoneyUtils.dollarsToCents(amount);

            ps.setInt(1, cents);
            ps.setString(2, accountID);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new TransactionFailedException("Could not carry out transaction. Please try again.");
            }
            return rowsAffected;
        } catch (SQLException e) {
            throw new TransactionFailedException("Could not carry out transaction. Please try again");
        }
    }

    // Perform manualCommitConnect() and facilitate a transfer - Ye
    public static void transfer(String accountFrom, String accountTo, double amount) {

    }

    // Adds a transaction for an associated accountID - Ye
    public static void addTransaction() {

    }

    // Retrieves the tranactions for an associated accountID - Yousef
    public static ArrayList<Transaction> getTransactionHistory(String accountID) throws EmptyTransactionHistoryException, DatabaseException {
        //TODO: I will need to go back and then order by creationDate asc in order to get the transaction history in order
        String query = "SELECT * FROM transactions t WHERE t.account_id = ?";
        ArrayList<Transaction> out = new ArrayList<>();
        try(   
            Connection connection = ConnectionFactory.getAutoCommitConnect();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, accountID);
            try (ResultSet res = statement.executeQuery()) {
                while(res.next()) {
                    String accID = res.getString("account_id");
                    String tType = res.getString("transaction_type");
                    BigDecimal cents = BigDecimal.valueOf(res.getInt("amount_cents"));
                    String relID = res.getString("related_account_id");
                    String cDate = res.getString("creationDate");

                    //TODO: We should not be storing transaction_id in Transaction class b/c that is handled in db side
                    //for now, we put placeover text for it until its deleted
                    out.add(new Transaction("placeholder", accID, tType, cents, relID, cDate));
                }
                if(out.size() == 0) {
                    throw new EmptyTransactionHistoryException("No transaction history found for account: " + accountID);
                }
                return out;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database error during Transaction retrieval: ", e);
        }
    }
}

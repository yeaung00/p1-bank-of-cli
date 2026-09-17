package com.revature;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import com.revature.exceptions.RepositoryException;
import com.revature.exceptions.*;
import com.revature.exceptions.customexceptions.*;

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
        String query = "SELECT balance_cents FROM accounts WHERE account_id = ?";

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
        String query = "UPDATE accounts SET balance_cents = ? WHERE account_id = ?";
        try (
            Connection connection = ConnectionFactory.getAutoCommitConnect();
            PreparedStatement ps = connection.prepareStatement(query)
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
            throw new TransactionFailedException("Could not carry out transaction. Please try again.");
        }
    }

    // Perform manualCommitConnect() and facilitate a transfer - Ye
    public static void transfer(String accountFrom, String accountTo, BigDecimal amount) throws RepositoryException {
        String debitSql = "UPDATE accounts SET balance_cents = balance_cents - ? WHERE account_id = ? AND balance_cents >= ?";
        String creditSql = "UPDATE accounts SET balance_cents = balance_cents + ? WHERE account_id = ?";
        int cents = MoneyUtils.dollarsToCents(amount);
        try (Connection connection = ConnectionFactory.getManualCommitConnection()) {
            try (PreparedStatement creditStatement = connection.prepareStatement(creditSql);
                 PreparedStatement debitStatement = connection.prepareStatement(debitSql)) {
                debitStatement.setInt(1, cents);
                debitStatement.setString(2, accountFrom);
                debitStatement.setInt(3, cents);
                if (debitStatement.executeUpdate() != 1) {
                    throw new TransactionFailedException("Transfer failed. Missing account or balance too low.");
                }
                creditStatement.setInt(1, cents);
                creditStatement.setString(2, accountTo);
                if (creditStatement.executeUpdate() != 1) {
                    throw new AccountNotFoundException("Transfer failed: recipient " + accountTo + " not found.");
                }
                addTransaction(connection, accountFrom, "TRANSFER_OUT", cents, accountTo);
                addTransaction(connection, accountTo, "TRANSFER_IN", cents, accountFrom);
                connection.commit();
            } catch (RepositoryException | SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database error during transfer", e);
        }
    }

    // Adds a transaction for an associated accountID - Ye
    public static void addTransaction(Connection connection, String accountID, String type, int amountCents, String relatedAccountID) throws SQLException {
        String insertTransactionsSql = "INSERT INTO transactions (account_id, transaction_type, amount_cents, related_account_id) " + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertTransactionsSql)) {
            statement.setString(1, accountID);
            statement.setString(2, type);
            statement.setInt(3, amountCents);
            statement.setString(4, relatedAccountID);
            statement.executeUpdate();
        }
    }

    // Retrieves the tranactions for an associated accountID - Yousef
    public static void viewTransactionHistory() {

    }
}

package com.banking.system.application.port.out.pdf;

import com.banking.system.domain.model.BankAccount;
import com.banking.system.domain.model.Transaction;
import java.util.List;

/**
 * OUTPUT PORT for PDF generation
 * Defines what our application needs for PDF creation
 */
public interface PdfGenerator {

    /**
     * Generate an account statement PDF
     * @param account the bank account
     * @param transactions list of transactions for the statement period
     * @param periodStart start date of the statement period
     * @param periodEnd end date of the statement period
     * @return byte array of the generated PDF
     */
    byte[] generateAccountStatement(BankAccount account,
                                    List<Transaction> transactions,
                                    String periodStart,
                                    String periodEnd);

    /**
     * Generate a transaction receipt PDF
     * @param transaction the transaction to generate receipt for
     * @return byte array of the generated PDF
     */
    byte[] generateTransactionReceipt(Transaction transaction);
}
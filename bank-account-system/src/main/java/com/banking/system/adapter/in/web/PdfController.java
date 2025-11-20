package com.banking.system.adapter.in.web;

import com.banking.system.application.port.out.pdf.PdfGenerator;
import com.banking.system.application.port.out.persistence.TransactionRepository;
import com.banking.system.domain.model.BankAccount;
import com.banking.system.domain.model.Transaction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    private final PdfGenerator pdfGenerator;
    private final TransactionRepository transactionRepository;
    private final AccountController accountController;

    public PdfController(PdfGenerator pdfGenerator,
                         TransactionRepository transactionRepository,
                         AccountController accountController) {
        this.pdfGenerator = pdfGenerator;
        this.transactionRepository = transactionRepository;
        this.accountController = accountController;
    }

    @GetMapping("/statement/{accountNumber}")
    public ResponseEntity<byte[]> generateAccountStatement(
            @PathVariable String accountNumber,
            @RequestParam String periodStart,
            @RequestParam String periodEnd) {
        try {
            // Get account details
            var accountResponse = accountController.getAccountByNumber(accountNumber);
            if (accountResponse.getStatusCode().isError()) {
                return ResponseEntity.notFound().build();
            }
            BankAccount account = accountResponse.getBody();

            // Get transactions for the period
            List<Transaction> transactions = transactionRepository.findByAccountNumber(accountNumber);

            // Generate PDF
            byte[] pdfBytes = pdfGenerator.generateAccountStatement(
                    account, transactions, periodStart, periodEnd);

            // Return PDF as downloadable file
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=statement-" + accountNumber + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/receipt/{transactionId}")
    public ResponseEntity<byte[]> generateTransactionReceipt(@PathVariable String transactionId) {
        // In real app, we'd find transaction by ID
        // For now, we'll create a mock transaction
        Transaction transaction = new Transaction(
                "ACC123456", "ACC789012", new BigDecimal("100.00"),
                "USD", "TRANSFER", "SUCCESS", "Test transaction"
        );
        transaction.setTransactionId(transactionId);

        byte[] pdfBytes = pdfGenerator.generateTransactionReceipt(transaction);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=receipt-" + transactionId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
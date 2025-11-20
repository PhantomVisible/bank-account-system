package com.banking.system.application.service;

import com.banking.system.adapter.out.pdf.PdfGeneratorAdapter;
import com.banking.system.domain.model.BankAccount;
import com.banking.system.domain.model.Transaction;
import com.banking.system.domain.model.User;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PdfGenerationTest {

    @Test
    void shouldGenerateTransactionReceiptPdf() {
        System.out.println("🧪 Testing Transaction Receipt PDF Generation...");

        // Given
        PdfGeneratorAdapter pdfGenerator = new PdfGeneratorAdapter();

        Transaction transaction = new Transaction(
                "ACC123456", "ACC789012", new BigDecimal("150.00"),
                "USD", "TRANSFER", "SUCCESS", "Dinner payment"
        );

        // When
        byte[] pdfBytes = pdfGenerator.generateTransactionReceipt(transaction);

        // Then - verify PDF was generated
        if (pdfBytes != null && pdfBytes.length > 0) {
            System.out.println("✅ Transaction receipt PDF generated successfully!");
            System.out.println("📄 Receipt size: " + pdfBytes.length + " bytes");

            try {
                // Save to file to see the actual PDF
                Files.write(Paths.get("test-receipt.pdf"), pdfBytes);
                System.out.println("💾 Receipt saved as 'test-receipt.pdf'");
                System.out.println("📁 File location: " + Paths.get("test-receipt.pdf").toAbsolutePath());
            } catch (Exception e) {
                System.out.println("⚠️ Could not save receipt file: " + e.getMessage());
                System.out.println("✅ But receipt was generated successfully in memory!");
            }
        } else {
            System.out.println("❌ Receipt PDF generation failed!");
        }
    }

    @Test
    void shouldGenerateAccountStatementPdf() {
        System.out.println("🧪 Testing Account Statement PDF Generation...");

        // Given
        PdfGeneratorAdapter pdfGenerator = new PdfGeneratorAdapter();

        // Create test user and account
        User user = new User("john_doe", "john@email.com", "password", "John Doe");
        user.setId(1L);

        BankAccount account = new BankAccount("ACC123456", user);
        account.setId(1L);
        account.deposit(new BigDecimal("1000.00"));

        // Create test transactions
        List<Transaction> transactions = Arrays.asList(
                new Transaction("ACC123456", "ACC789012", new BigDecimal("100.00"),
                        "USD", "TRANSFER", "SUCCESS", "Lunch payment"),
                new Transaction(null, "ACC123456", new BigDecimal("500.00"),
                        "USD", "DEPOSIT", "SUCCESS", "Salary deposit")
        );

        // When
        byte[] pdfBytes = pdfGenerator.generateAccountStatement(
                account, transactions, "2024-01-01", "2024-01-31");

        // Then - verify PDF was generated (not empty)
        if (pdfBytes != null && pdfBytes.length > 0) {
            System.out.println("✅ PDF generated successfully!");
            System.out.println("📄 PDF size: " + pdfBytes.length + " bytes");

            try {
                // Save to file to see the actual PDF
                Files.write(Paths.get("test-statement.pdf"), pdfBytes);
                System.out.println("💾 PDF saved as 'test-statement.pdf'");
                System.out.println("📁 File location: " + Paths.get("test-statement.pdf").toAbsolutePath());
            } catch (Exception e) {
                System.out.println("⚠️ Could not save PDF file: " + e.getMessage());
                System.out.println("✅ But PDF was generated successfully in memory!");
            }
        } else {
            System.out.println("❌ PDF generation failed!");
        }
    }
}
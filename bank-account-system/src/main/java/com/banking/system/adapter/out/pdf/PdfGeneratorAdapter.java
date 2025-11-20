package com.banking.system.adapter.out.pdf;

import com.banking.system.application.port.out.pdf.PdfGenerator;
import com.banking.system.domain.model.BankAccount;
import com.banking.system.domain.model.Transaction;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class PdfGeneratorAdapter implements PdfGenerator {

    @Override
    public byte[] generateAccountStatement(BankAccount account, List<Transaction> transactions,
                                           String periodStart, String periodEnd) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Header
            document.add(new Paragraph("BANK ACCOUNT STATEMENT")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setFontSize(16));

            // Account Information
            document.add(new Paragraph("Account Number: " + account.getAccountNumber()));
            document.add(new Paragraph("Account Holder: " + account.getUser().getFullName()));
            document.add(new Paragraph("Statement Period: " + periodStart + " to " + periodEnd));
            document.add(new Paragraph("Current Balance: " + account.getFormattedBalance()));
            document.add(new Paragraph(" ")); // Empty line

            // Transactions Table
            if (transactions.isEmpty()) {
                document.add(new Paragraph("No transactions during this period."));
            } else {
                document.add(createTransactionsTable(transactions));
            }

            // Footer
            document.add(new Paragraph(" ")
                    .setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Generated on: " + java.time.LocalDate.now())
                    .setTextAlignment(TextAlignment.CENTER)
                    .setItalic());

            document.close();
            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF statement", e);
        }
    }

    @Override
    public byte[] generateTransactionReceipt(Transaction transaction) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Header
            document.add(new Paragraph("TRANSACTION RECEIPT")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setFontSize(16));

            // Transaction Details
            document.add(new Paragraph("Transaction ID: " + transaction.getTransactionId()));
            document.add(new Paragraph("Date: " + transaction.getTimestamp().toLocalDate()));
            document.add(new Paragraph("Time: " + transaction.getTimestamp().toLocalTime()));
            document.add(new Paragraph("Type: " + transaction.getType()));
            document.add(new Paragraph("Amount: " + transaction.getAmount() + " " + transaction.getCurrency()));
            document.add(new Paragraph("From Account: " + transaction.getFromAccount()));
            document.add(new Paragraph("To Account: " + transaction.getToAccount()));
            document.add(new Paragraph("Status: " + transaction.getStatus()));
            document.add(new Paragraph("Description: " + transaction.getDescription()));

            document.add(new Paragraph(" ")
                    .setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Thank you for banking with us!")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setItalic());

            document.close();
            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF receipt", e);
        }
    }

    private Table createTransactionsTable(List<Transaction> transactions) {
        float[] columnWidths = {2, 2, 2, 2, 2, 3};
        Table table = new Table(columnWidths);

        // Table Header
        table.addHeaderCell("Date");
        table.addHeaderCell("Type");
        table.addHeaderCell("From Account");
        table.addHeaderCell("To Account");
        table.addHeaderCell("Amount");
        table.addHeaderCell("Description");

        // Table Rows
        for (Transaction transaction : transactions) {
            table.addCell(transaction.getTimestamp().toLocalDate().toString());
            table.addCell(transaction.getType());
            table.addCell(transaction.getFromAccount() != null ? transaction.getFromAccount() : "-");
            table.addCell(transaction.getToAccount() != null ? transaction.getToAccount() : "-");
            table.addCell(transaction.getAmount() + " " + transaction.getCurrency());
            table.addCell(transaction.getDescription());
        }

        return table;
    }
}
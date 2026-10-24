import java.time.LocalDate;

public class Refund {
    private String refundId;
    private String rmaId;
    private String method;
    private double amount;
    private LocalDate processedDate;
    private String transactionReference;
    private boolean processed;

    public Refund(String refundId, String rmaId, String method, double amount,
                  LocalDate processedDate, String transactionReference) {
        this.refundId = refundId;
        this.rmaId = rmaId;
        this.method = method;
        this.amount = amount;
        this.processedDate = processedDate;
        this.transactionReference = transactionReference;
        this.processed = false;
    }

    public String getRefundId() { return refundId; }
    public void setRefundId(String refundId) { this.refundId = refundId; }
    public String getRmaId() { return rmaId; }
    public void setRmaId(String rmaId) { this.rmaId = rmaId; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { if (amount < 0) throw new IllegalArgumentException("Amount cannot be negative."); this.amount = amount; }
    public LocalDate getProcessedDate() { return processedDate; }
    public void setProcessedDate(LocalDate processedDate) { this.processedDate = processedDate; }
    public String getTransactionReference() { return transactionReference; }
    public void setTransactionReference(String transactionReference) { this.transactionReference = transactionReference; }
    public boolean isProcessed() { return processed; }

    public void processRefund() {
        if (processed) throw new IllegalStateException("Refund already processed.");
        processed = true;
        processedDate = LocalDate.now();
        System.out.println("Refund " + refundId + " processed by " + method + " for " + String.format("%.2f", amount)
                + " (reference: " + transactionReference + ")");
    }
    public String getReceipt() {
        return "Refund " + refundId + " | RMA: " + rmaId + " | Method: " + method + " | Amount: "
                + String.format("%.2f", amount) + " | Date: " + processedDate + " | Processed: " + processed;
    }
    public void printDetails() { System.out.println(getReceipt()); }
}

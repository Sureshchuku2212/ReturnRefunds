import java.time.LocalDate;

public class CardRefund extends Refund {
    private String maskedCardNumber;
    private String cardNetwork;
    private String bankApprovalCode;
    private static int cardRefundCount = 0;

    public CardRefund(String refundId, String rmaId, double amount, LocalDate date,
                      String reference, String maskedCardNumber, String cardNetwork, String bankApprovalCode) {
        super(refundId, rmaId, "CARD", amount, date, reference);
        this.maskedCardNumber = maskedCardNumber;
        this.cardNetwork = cardNetwork;
        this.bankApprovalCode = bankApprovalCode;
    }
    public String getMaskedCardNumber() { return maskedCardNumber; }
    public void setMaskedCardNumber(String value) { maskedCardNumber = value; }
    public String getCardNetwork() { return cardNetwork; }
    public void setCardNetwork(String value) { cardNetwork = value; }
    public String getBankApprovalCode() { return bankApprovalCode; }
    public void setBankApprovalCode(String value) { bankApprovalCode = value; }
    public static int getCardRefundCount() { return cardRefundCount; }
    public void validateCard() {
        if (maskedCardNumber == null || maskedCardNumber.trim().isEmpty()) throw new IllegalStateException("Card details missing.");
    }
    @Override public void processRefund() {
        validateCard();
        cardRefundCount++;
        System.out.println("Card refund routed to " + cardNetwork + " card " + maskedCardNumber + ".");
        super.processRefund();
    }
}

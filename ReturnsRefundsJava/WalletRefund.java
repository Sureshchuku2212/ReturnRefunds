import java.time.LocalDate;

public class WalletRefund extends Refund {
    private String walletProvider;
    private String walletAccount;
    private String walletTransactionId;
    private static int walletRefundCount = 0;

    public WalletRefund(String refundId, String rmaId, double amount, LocalDate date,
                        String reference, String walletProvider, String walletAccount, String walletTransactionId) {
        super(refundId, rmaId, "WALLET", amount, date, reference);
        this.walletProvider = walletProvider;
        this.walletAccount = walletAccount;
        this.walletTransactionId = walletTransactionId;
    }
    public String getWalletProvider() { return walletProvider; }
    public void setWalletProvider(String value) { walletProvider = value; }
    public String getWalletAccount() { return walletAccount; }
    public void setWalletAccount(String value) { walletAccount = value; }
    public String getWalletTransactionId() { return walletTransactionId; }
    public void setWalletTransactionId(String value) { walletTransactionId = value; }
    public static int getWalletRefundCount() { return walletRefundCount; }
    public void validateWallet() {
        if (walletProvider == null || walletProvider.trim().isEmpty()) throw new IllegalStateException("Wallet provider missing.");
    }
    @Override public void processRefund() {
        validateWallet();
        walletRefundCount++;
        System.out.println("Wallet refund routed through " + walletProvider + " account " + walletAccount + ".");
        super.processRefund();
    }
}

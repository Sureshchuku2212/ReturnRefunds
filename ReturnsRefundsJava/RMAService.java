import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RMAService {
    private Map<String, Order> orders;
    private Map<String, ReturnRequest> requests;
    private List<Refund> refunds;
    private int nextRmaNumber;
    private int nextRefundNumber;
    private String serviceName;
    private static int serviceInstances = 0;

    public RMAService(String serviceName) {
        this.serviceName = serviceName;
        this.orders = new LinkedHashMap<>();
        this.requests = new LinkedHashMap<>();
        this.refunds = new ArrayList<>();
        this.nextRmaNumber = 1001;
        this.nextRefundNumber = 5001;
        serviceInstances++;
    }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public int getNextRmaNumber() { return nextRmaNumber; }
    public int getNextRefundNumber() { return nextRefundNumber; }
    public Map<String, Order> getOrders() { return Collections.unmodifiableMap(orders); }
    public Map<String, ReturnRequest> getRequests() { return Collections.unmodifiableMap(requests); }
    public List<Refund> getRefunds() { return Collections.unmodifiableList(refunds); }
    public static int getServiceInstances() { return serviceInstances; }

    public void addOrder(Order order) { orders.put(order.getOrderId(), order); }

    // Overload 1: one item, no photos.
    public ReturnRequest createRMA(String orderId, String item, String reason) {
        return createRMA(orderId, Collections.singletonList(item), reason, Collections.emptyList());
    }

    // Overload 2: one item with photos.
    public ReturnRequest createRMA(String orderId, String item, String reason, List<String> photos) {
        return createRMA(orderId, Collections.singletonList(item), reason, photos);
    }

    // Overload 3: multiple items, optionally with photos.
    public ReturnRequest createRMA(String orderId, List<String> items, String reason, List<String> photos) {
        Order order = orders.get(orderId);
        if (order == null) throw new IllegalArgumentException("Order not found: " + orderId);
        if (items == null || items.isEmpty()) throw new IllegalArgumentException("At least one item is required.");
        for (String item : items) {
            if (!order.containsItem(item)) throw new IllegalArgumentException("Item is not part of order: " + item);
        }
        String rmaId = "RMA-" + nextRmaNumber++;
        ReturnRequest request = new ReturnRequest(rmaId, orderId, reason, "UNINSPECTED",
                items, photos == null ? Collections.emptyList() : photos, LocalDate.now());
        requests.put(rmaId, request);
        System.out.println("Created RMA: " + rmaId);
        return request;
    }

    public void inspectItem(String rmaId, String inspectedCondition) {
        ReturnRequest request = getRequestOrThrow(rmaId);
        request.markInspected(inspectedCondition);
        System.out.println("Inspected " + rmaId + ": " + inspectedCondition);
    }

    public void approve(String rmaId) {
        getRequestOrThrow(rmaId).approve();
        System.out.println("Approved " + rmaId);
    }

    public void deny(String rmaId) {
        getRequestOrThrow(rmaId).deny();
        System.out.println("Denied " + rmaId);
    }

    public Refund processRefund(String rmaId, String method, double amount) {
        ReturnRequest request = getRequestOrThrow(rmaId);
        if (request.getStatus() != ReturnRequest.Status.APPROVED)
            throw new IllegalStateException("RMA must be approved before refund.");
        if (amount < 0) throw new IllegalArgumentException("Refund amount cannot be negative.");

        String refundId = "REF-" + nextRefundNumber++;
        Refund refund;
        if ("CARD".equalsIgnoreCase(method)) {
            refund = new CardRefund(refundId, rmaId, amount, LocalDate.now(),
                    "TXN-" + refundId, "**** **** **** 1234", "VISA", "BANK-OK");
        } else if ("WALLET".equalsIgnoreCase(method)) {
            refund = new WalletRefund(refundId, rmaId, amount, LocalDate.now(),
                    "TXN-" + refundId, "DemoWallet", "buyer@example.com", "WAL-" + refundId);
        } else {
            refund = new Refund(refundId, rmaId, method.toUpperCase(), amount,
                    LocalDate.now(), "TXN-" + refundId);
        }

        // Polymorphism: the overridden method is chosen at runtime.
        refunds.add(refund);
        refund.processRefund();
        request.markRefunded();
        return refund;
    }

    public ReturnRequest getRequestOrThrow(String rmaId) {
        ReturnRequest request = requests.get(rmaId);
        if (request == null) throw new IllegalArgumentException("RMA not found: " + rmaId);
        return request;
    }

    public void printDailyRmaSummary(LocalDate date) {
        long total = requests.values().stream().filter(r -> r.getRequestDate().equals(date)).count();
        long approved = requests.values().stream().filter(r -> r.getRequestDate().equals(date)
                && r.getStatus() == ReturnRequest.Status.APPROVED).count();
        long denied = requests.values().stream().filter(r -> r.getRequestDate().equals(date)
                && r.getStatus() == ReturnRequest.Status.DENIED).count();
        long refunded = requests.values().stream().filter(r -> r.getRequestDate().equals(date)
                && r.getStatus() == ReturnRequest.Status.REFUNDED).count();
        System.out.println("\\n=== Daily RMA Summary: " + date + " ===");
        System.out.println("Created: " + total + " | Approved: " + approved + " | Denied: " + denied + " | Refunded: " + refunded);
    }

    public void printDailyRefundSummary(LocalDate date) {
        double total = 0.0;
        int count = 0;
        System.out.println("\\n=== Daily Refund Summary: " + date + " ===");
        for (Refund refund : refunds) {
            if (refund.isProcessed() && date.equals(refund.getProcessedDate())) {
                refund.printDetails();
                total += refund.getAmount();
                count++;
            }
        }
        System.out.println("Refund count: " + count + " | Total refunded: " + String.format("%.2f", total));
    }

    public void printAllRequests() {
        System.out.println("\\n=== All Return Requests ===");
        for (ReturnRequest request : requests.values()) request.printDetails();
    }
}

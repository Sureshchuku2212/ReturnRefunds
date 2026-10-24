import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReturnRequest {
    public enum Status { REQUESTED, INSPECTED, APPROVED, DENIED, REFUNDED }

    private String rmaId;
    private String orderId;
    private String reason;
    private String condition;
    private Status status;
    private List<String> items;
    private List<String> photoPaths;
    private LocalDate requestDate;

    public ReturnRequest(String rmaId, String orderId, String reason, String condition,
                         List<String> items, List<String> photoPaths, LocalDate requestDate) {
        this.rmaId = rmaId;
        this.orderId = orderId;
        this.reason = reason;
        this.condition = condition;
        this.status = Status.REQUESTED;
        this.items = new ArrayList<>(items);
        this.photoPaths = new ArrayList<>(photoPaths);
        this.requestDate = requestDate;
    }

    public String getRmaId() { return rmaId; }
    public void setRmaId(String rmaId) { this.rmaId = rmaId; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }
    public Status getStatus() { return status; }
    public List<String> getItems() { return Collections.unmodifiableList(items); }
    public List<String> getPhotoPaths() { return Collections.unmodifiableList(photoPaths); }
    public LocalDate getRequestDate() { return requestDate; }

    // Status is intentionally not exposed through a public setter.
    public void markInspected(String inspectedCondition) {
        if (status != Status.REQUESTED) throw new IllegalStateException("Only requested RMAs can be inspected.");
        condition = inspectedCondition;
        status = Status.INSPECTED;
    }
    public void approve() {
        if (status != Status.INSPECTED) throw new IllegalStateException("Inspect the RMA before approval.");
        status = Status.APPROVED;
    }
    public void deny() {
        if (status != Status.REQUESTED && status != Status.INSPECTED)
            throw new IllegalStateException("Only pending RMAs can be denied.");
        status = Status.DENIED;
    }
    public void markRefunded() {
        if (status != Status.APPROVED) throw new IllegalStateException("Only approved RMAs can be refunded.");
        status = Status.REFUNDED;
    }
    public void addPhoto(String path) { if (path != null && !path.trim().isEmpty()) photoPaths.add(path); }
    public boolean includesItem(String item) { return items.contains(item); }
    public void printDetails() {
        System.out.println(rmaId + " | Order: " + orderId + " | Items: " + items + " | Reason: " + reason
                + " | Condition: " + condition + " | Status: " + status + " | Date: " + requestDate);
    }
}

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {
    private String orderId;
    private String buyer;
    private List<String> items;
    private double totalAmount;
    private LocalDate purchaseDate;
    private String currency;
    private String shippingAddress;

    public Order(String orderId, String buyer, List<String> items, double totalAmount,
                 LocalDate purchaseDate, String currency, String shippingAddress) {
        this.orderId = orderId;
        this.buyer = buyer;
        this.items = new ArrayList<>(items);
        this.totalAmount = totalAmount;
        this.purchaseDate = purchaseDate;
        this.currency = currency;
        this.shippingAddress = shippingAddress;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getBuyer() { return buyer; }
    public void setBuyer(String buyer) { this.buyer = buyer; }
    public List<String> getItems() { return Collections.unmodifiableList(items); }
    public void setItems(List<String> items) { this.items = new ArrayList<>(items); }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public boolean containsItem(String item) { return items.contains(item); }
    public void addItem(String item) { if (item != null && !item.trim().isEmpty()) items.add(item); }
    public boolean removeItem(String item) { return items.remove(item); }
    public void printDetails() {
        System.out.println("Order " + orderId + " | Buyer: " + buyer + " | Items: " + items
                + " | Total: " + currency + " " + String.format("%.2f", totalAmount)
                + " | Purchased: " + purchaseDate);
    }
}

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

public class RMAppMain {
    public static void main(String[] args) {
        RMAService service = new RMAService("Returns & Refunds Console Service");
        LocalDate today = LocalDate.now();

        // Load sample orders.
        Order order1 = new Order("ORD-101", "Arun",
                Arrays.asList("Headphones", "USB Cable"), 2499.00,
                today.minusDays(5), "INR", "Chennai");
        Order order2 = new Order("ORD-102", "Meena",
                Arrays.asList("Keyboard", "Mouse"), 3199.00,
                today.minusDays(2), "INR", "Coimbatore");
        service.addOrder(order1);
        service.addOrder(order2);

        System.out.println("=== Loaded Orders ===");
        order1.printDetails();
        order2.printDetails();

        // Create RMA requests using overloaded methods.
        ReturnRequest rma1 = service.createRMA("ORD-101", "Headphones", "Product not working");
        ReturnRequest rma2 = service.createRMA("ORD-102", "Keyboard", "Damaged on arrival",
                Arrays.asList("keyboard_front.jpg", "package.jpg"));
        ReturnRequest rma3 = service.createRMA("ORD-101",
                Arrays.asList("USB Cable"), "Wrong item delivered", Collections.emptyList());

        // Inspect and approve one, inspect and deny another, approve the third.
        service.inspectItem(rma1.getRmaId(), "Opened box; item faulty");
        service.approve(rma1.getRmaId());

        service.inspectItem(rma2.getRmaId(), "Physical damage confirmed");
        service.deny(rma2.getRmaId());

        service.inspectItem(rma3.getRmaId(), "Unused; packaging intact");
        service.approve(rma3.getRmaId());

        // Refunds are stored as Refund references; overridden methods run polymorphically.
        service.processRefund(rma1.getRmaId(), "CARD", 1499.00);
        service.processRefund(rma3.getRmaId(), "WALLET", 250.00);

        service.printAllRequests();
        service.printDailyRmaSummary(today);
        service.printDailyRefundSummary(today);
    }
}

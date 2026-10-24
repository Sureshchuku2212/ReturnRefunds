RETURNS & REFUNDS (RMA) CONSOLE APPLICATION - JAVA

Files:
  Order.java
  ReturnRequest.java
  Refund.java
  CardRefund.java
  WalletRefund.java
  RMAService.java
  RMAppMain.java

Requirements:
  Java 8 or newer.

Compile:
  javac *.java

Run:
  java RMAppMain

Demonstrated OOP:
  - Encapsulation: private fields and controlled ReturnRequest status transitions.
  - Inheritance: CardRefund and WalletRefund extend Refund.
  - Overloading: RMAService.createRMA() supports one item, photos, and multiple items.
  - Overriding: CardRefund and WalletRefund override processRefund().
  - Polymorphism: refunds are held in List<Refund> and processRefund() is called on Refund references.

Notes:
  - This is a console-based educational demo using in-memory sample data.
  - No real payment is sent; transaction references and payment details are mock values.
  - Status transitions are REQUESTED -> INSPECTED -> APPROVED -> REFUNDED, with DENIED for rejected requests.

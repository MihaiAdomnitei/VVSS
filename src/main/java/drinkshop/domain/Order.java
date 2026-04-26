package drinkshop.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private List<OrderItem> items;
    private double totalPrice;

    public Order(int id) {
        this.id = id;
        this.items = new ArrayList<>();
        this.totalPrice = 0.0;
    }

    public Order(int id, List<OrderItem> items, double totalPrice) {
        this.id = id;
        this.items = new ArrayList<>(items);
        computeTotalPrice();  // Always calculate, ignore parameter for consistency
    }

    public int getId() {
        return id;
    }

    public List<OrderItem> getItems() {
        return new ArrayList<>(items);  // Defensive copy
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setItems(List<OrderItem> items) {
        this.items = new ArrayList<>(items);  // Defensive copy
        computeTotalPrice();  // Recalculate total
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
        computeTotalPrice();  // Recalculate total
    }

    public void removeItem(OrderItem item) {
        this.items.remove(item);
        computeTotalPrice();  // Recalculate total
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", items=" + items +
                ", totalPrice=" + totalPrice +
                '}';
    }

    public void computeTotalPrice() {
        this.totalPrice=items.stream().mapToDouble(OrderItem::getTotal).sum();
    }
}
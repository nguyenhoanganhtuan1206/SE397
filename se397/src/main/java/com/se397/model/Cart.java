package com.se397.model;

import com.se397.service.ProductService;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    private int id;
    private String name;
    private String image;
    private double price;
    private int quantity;
    private String category;
    private double pricePromotion;

    /* Promotion price */
    public double pricePromotion(double priceProduct , double percent) {
        return priceProduct - (priceProduct * percent / 100);
    }

    /* Total product each product */
    public double totalPriceEachProduct(int id, HashMap<Integer, Cart> carts) {
        double price = 0;
        for (Map.Entry<Integer, Cart> entry : carts.entrySet()) {
            if (entry.getValue().getId() == id) {
                price += entry.getValue().getPrice() * entry.getValue().getQuantity();
            }
        }
        return Math.round(price);
    }

    /* Increase product */
    public void increaseProduct(int id, HashMap<Integer, Cart> carts) {
        Cart cart = carts.get(id);
        cart.setQuantity(cart.getQuantity() + 1);
        carts.put(id , cart);
    }

    /* Decrease product */
    public void decreaseProduct(int id, HashMap<Integer, Cart> carts) {
        Cart cart = carts.get(id);
        cart.setQuantity(cart.getQuantity() - 1);
        carts.put(id , cart);
    }

    /* Delete product from cart */
    public void deleteProduct(HashMap<Integer , Cart> carts , int id) {
        carts.remove(id);
    }

    /* Calculate Total Price */
    public double totalPriceProduct(HashMap<Integer, Cart> carts) {
        double price = 0;
        for (Map.Entry<Integer, Cart> entry : carts.entrySet()) {
            price += entry.getValue().getPrice() * entry.getValue().getQuantity();
        }
        return Math.round(price);
    }

    public Cart() {
    }

    public double getPricePromotion() {
        return pricePromotion;
    }

    public void setPricePromotion(double pricePromotion) {
        this.pricePromotion = pricePromotion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

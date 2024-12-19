package com.se397.model;

import javax.persistence.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "evaluation")
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String content;

    @Column(name = "start_date")
    private LocalDate localDate;

    private float rate;

    @ManyToOne
    @JoinColumn(name = "product_id" , nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id" , nullable = false)
    private User user;

    public double calculateEvaluation(int productId , List<Evaluation> evaluations) {
        float rating = 0;
        int count = 0;
        for(Evaluation evaluation : evaluations) {
            if (evaluation.getProduct().getId() == productId) {
                count++;
                rating += evaluation.getRate();
            }
        }
        return rating/count;
    }

    /* Get last name user */
    public char getLastNameUser(String name) {
        String lastWord = name.substring(name.lastIndexOf(" ")+1);
        char lastChar = lastWord.charAt(0);
        return lastChar;
    }

    public int getId() {
        return id;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

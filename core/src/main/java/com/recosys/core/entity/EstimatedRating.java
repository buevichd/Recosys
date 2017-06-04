package com.recosys.core.entity;

public class EstimatedRating extends Entity {
    private User user;
    private Product product;
    private Double rating;

    public EstimatedRating() {
    }

    public EstimatedRating(Long id) {
        super(id);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}

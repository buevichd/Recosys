package com.recosys.web.dto;

import com.recosys.core.entity.Rating;
import org.jetbrains.annotations.NotNull;

public class RatingDTO {
    private Long id;
    private Integer rating;
    private Long productId;
    private Long userId;

    public RatingDTO() {
    }

    public RatingDTO(@NotNull Rating rating) {
        id = rating.getId();
        userId = rating.getUser().getId();
        productId = rating.getProduct().getId();
        this.rating = rating.getRating();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

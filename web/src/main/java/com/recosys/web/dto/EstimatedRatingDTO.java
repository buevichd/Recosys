package com.recosys.web.dto;

import com.recosys.core.entity.EstimatedRating;
import com.recosys.core.entity.Rating;
import org.jetbrains.annotations.NotNull;

public class EstimatedRatingDTO {
    private Long id;
    private Double rating;
    private Long productId;
    private Long userId;

    public EstimatedRatingDTO() {
    }

    public EstimatedRatingDTO(@NotNull EstimatedRating rating) {
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

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
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

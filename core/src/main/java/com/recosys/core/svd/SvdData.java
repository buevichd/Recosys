package com.recosys.core.svd;

import com.recosys.core.entity.Product;
import com.recosys.core.entity.User;
import org.ejml.simple.SimpleMatrix;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class SvdData {
    private SimpleMatrix estimatedRatingMatrix;
    private SimpleMatrix ratingMatrix;
    private SimpleMatrix missingMatrix;
    private Map<Integer, User> indexToUserMap;
    private Map<Integer, Product> indexToProductMap;

    public SvdData(SimpleMatrix ratingMatrix, SimpleMatrix missingMatrix,
                   Map<Integer, User> indexToUserMap, Map<Integer, Product> indexToProductMap) {
        this.ratingMatrix = ratingMatrix;
        this.missingMatrix = missingMatrix;
        this.indexToUserMap = indexToUserMap;
        this.indexToProductMap = indexToProductMap;
    }

    @NotNull
    public User getUser(int index) {
        return indexToUserMap.get(index);
    }

    @NotNull
    public Product getProduct(int index) {
        return indexToProductMap.get(index);
    }

    public boolean isRated(int userIndex, int productIndex) {
        return Math.abs(missingMatrix.get(userIndex, productIndex)) > SvdConstants.ROUNDING_ERROR;
    }

    public SimpleMatrix getRatingMatrix() {
        return ratingMatrix;
    }

    public SimpleMatrix getMissingMatrix() {
        return missingMatrix;
    }

    public SimpleMatrix getEstimatedRatingMatrix() {
        return estimatedRatingMatrix;
    }

    public void setEstimatedRatingMatrix(SimpleMatrix estimatedRatingMatrix) {
        this.estimatedRatingMatrix = estimatedRatingMatrix;
    }
}

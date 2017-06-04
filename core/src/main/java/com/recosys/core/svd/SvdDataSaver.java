package com.recosys.core.svd;

import com.recosys.core.entity.EstimatedRating;
import com.recosys.core.entity.Product;
import com.recosys.core.entity.User;
import com.recosys.core.model.interfaces.EstimatedRatingDao;
import org.ejml.simple.SimpleMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SvdDataSaver {
    private EstimatedRatingDao estimatedRatingDao;

    public void saveData(SvdData dataHolder) {
        SimpleMatrix estimatedRatingsMatrix = dataHolder.getEstimatedRatingMatrix();
        for (int userIndex = 0; userIndex < estimatedRatingsMatrix.numRows(); userIndex++) {
            for (int productIndex = 0; productIndex < estimatedRatingsMatrix.numCols(); productIndex++) {
                User user = dataHolder.getUser(userIndex);
                Product product = dataHolder.getProduct(productIndex);
                boolean isRated = dataHolder.isRated(userIndex, productIndex);
                double rating = estimatedRatingsMatrix.get(userIndex, productIndex);
                updateEstimatedRating(user, product, rating, isRated);
            }
        }
    }

    private void updateEstimatedRating(User user, Product product, double rating, boolean isRated) {
        EstimatedRating estimatedRating = estimatedRatingDao.get(user, product);
        if (!isRated) {
            if (estimatedRating != null) {
                updateEstimatedRating(estimatedRating, rating);
            } else {
                createEstimatedRating(user, product, rating);
            }
        } else if (estimatedRating != null) {
            estimatedRatingDao.delete(estimatedRating);
        }
    }

    private void updateEstimatedRating(EstimatedRating estimatedRating, double rating) {
        estimatedRating.setRating(rating);
        estimatedRatingDao.update(estimatedRating);
    }

    private void createEstimatedRating(User user, Product product, double rating) {
        EstimatedRating estimatedRating;
        estimatedRating = new EstimatedRating();
        estimatedRating.setProduct(product);
        estimatedRating.setUser(user);
        estimatedRating.setRating(rating);
        estimatedRatingDao.create(estimatedRating);
    }

    @Autowired
    public void setEstimatedRatingDao(EstimatedRatingDao estimatedRatingDao) {
        this.estimatedRatingDao = estimatedRatingDao;
    }
}

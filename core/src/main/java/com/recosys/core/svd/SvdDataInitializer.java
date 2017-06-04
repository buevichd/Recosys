package com.recosys.core.svd;

import com.recosys.core.entity.Entity;
import com.recosys.core.entity.Product;
import com.recosys.core.entity.Rating;
import com.recosys.core.entity.User;
import com.recosys.core.model.interfaces.ProductDao;
import com.recosys.core.model.interfaces.RatingDao;
import com.recosys.core.model.interfaces.UserDao;
import org.ejml.simple.SimpleMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Service
public class SvdDataInitializer {

    private UserDao userDao;
    private ProductDao productDao;
    private RatingDao ratingDao;

    private List<User> users;
    private List<Product> products;
    private List<Rating> ratings;

    private SimpleMatrix ratingMatrix;
    private SimpleMatrix missingMatrix;

    public SvdData initData() {
        init();
        populateMatrices();
        Map<Integer, User> numberToUserMap = generateIndexToEntityMap(users);
        Map<Integer, Product> numberToProductMap = generateIndexToEntityMap(products);
        return new SvdData(ratingMatrix, missingMatrix, numberToUserMap, numberToProductMap);
    }

    private void init() {
        users = userDao.getAll();
        products = productDao.getAll();
        ratings = ratingDao.getAll();
        ratingMatrix = new SimpleMatrix(users.size(), products.size());
        missingMatrix = new SimpleMatrix(users.size(), products.size());
    }

    private void populateMatrices() {
        Map<User, Integer> userToIndexMap = generateEntityToIndexMap(users);
        Map<Product, Integer> productToIndexMap = generateEntityToIndexMap(products);
        for (Rating rating : ratings) {
            Integer userIndex = userToIndexMap.get(rating.getUser());
            Integer productIndex = productToIndexMap.get(rating.getProduct());
            ratingMatrix.set(userIndex, productIndex, rating.getRating());
            missingMatrix.set(userIndex, productIndex, 1);
        }
    }

    private <T extends Entity> Map<T, Integer> generateEntityToIndexMap(Collection<T> collection) {
        Iterator<T> iterator = collection.iterator();
        return IntStream.range(0, collection.size()).boxed().collect(toMap(i -> iterator.next(), identity()));
    }

    private <T extends Entity> Map<Integer, T> generateIndexToEntityMap(Collection<T> collection) {
        Iterator<T> iterator = collection.iterator();
        return IntStream.range(0, collection.size()).boxed().collect(toMap(identity(), i -> iterator.next()));
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Autowired
    public void setRatingDao(RatingDao ratingDao) {
        this.ratingDao = ratingDao;
    }
}

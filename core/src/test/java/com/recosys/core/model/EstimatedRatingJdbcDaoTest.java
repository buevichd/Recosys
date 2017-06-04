package com.recosys.core.model;

import com.recosys.core.entity.EstimatedRating;
import com.recosys.core.entity.Product;
import com.recosys.core.entity.Rating;
import com.recosys.core.entity.User;
import com.recosys.core.model.interfaces.EstimatedRatingDao;
import com.recosys.core.model.interfaces.ProductDao;
import com.recosys.core.model.interfaces.UserDao;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config.xml")
@ActiveProfiles({"test", "default"})
@Transactional
public class EstimatedRatingJdbcDaoTest {
    private static final Double RATING = 4.2;

    @Autowired EstimatedRatingDao estimatedRatingDao;
    @Autowired ProductDao productDao;
    @Autowired UserDao userDao;

    private User user;
    private Product product;

    @Before
    public void setUp() {
        user = createUser();
        product = createProduct();
    }

    private User createUser() {
        User user = new User();
        userDao.create(user);
        return user;
    }

    private Product createProduct() {
        Product product = new Product();
        productDao.create(product);
        return product;
    }

    @Test
    public void shouldCreateAndReturnRating() {
        EstimatedRating rating = createEstimatedRating();

        EstimatedRating createdRating = estimatedRatingDao.get(rating.getId());

        assertTrue(EqualsBuilder.reflectionEquals(rating, createdRating));
    }

    @Test
    public void shouldCreateAndUpdateRating() {
        EstimatedRating rating = createEstimatedRating();

        updateRating(rating);
        estimatedRatingDao.update(rating);
        EstimatedRating createdRating = estimatedRatingDao.get(rating.getId());

        assertTrue(EqualsBuilder.reflectionEquals(rating, createdRating));
    }

    private void updateRating(EstimatedRating rating) {
        rating.setProduct(createProduct());
        rating.setUser(createUser());
        rating.setRating(RATING - 1);
    }

    @Test
    public void shouldCreateAndReturnAllRatings() {
        EstimatedRating rating = createEstimatedRating();

        List<EstimatedRating> ratings = estimatedRatingDao.getAll();

        assertEquals(1, ratings.size());
        assertTrue(EqualsBuilder.reflectionEquals(rating, ratings.get(0)));
    }

    @Test
    public void shouldCreateAndFindRatingByUserAndProduct() {
        EstimatedRating rating = createEstimatedRating();

        EstimatedRating foundRating = estimatedRatingDao.get(user, product);

        assertTrue(EqualsBuilder.reflectionEquals(rating, foundRating));
    }

    @NotNull
    private EstimatedRating createEstimatedRating() {
        EstimatedRating rating = new EstimatedRating();
        rating.setUser(user);
        rating.setProduct(product);
        rating.setRating(RATING);
        estimatedRatingDao.create(rating);
        return rating;
    }
}

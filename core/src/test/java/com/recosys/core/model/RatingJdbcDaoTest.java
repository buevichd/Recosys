package com.recosys.core.model;

import com.recosys.core.entity.Product;
import com.recosys.core.entity.Rating;
import com.recosys.core.entity.User;
import com.recosys.core.model.interfaces.ProductDao;
import com.recosys.core.model.interfaces.RatingDao;
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
public class RatingJdbcDaoTest {
    private static final Integer RATING = 5;

    @Autowired RatingDao ratingDao;
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
        Rating rating = createRating();

        Rating createdRating = ratingDao.get(rating.getId());

        assertTrue(EqualsBuilder.reflectionEquals(rating, createdRating));
    }

    @Test
    public void shouldCreateAndUpdateRating() {
        Rating rating = createRating();

        updateRating(rating);
        ratingDao.update(rating);
        Rating createdRating = ratingDao.get(rating.getId());

        assertTrue(EqualsBuilder.reflectionEquals(rating, createdRating));
    }

    private void updateRating(Rating rating) {
        rating.setProduct(createProduct());
        rating.setUser(createUser());
        rating.setRating(RATING - 1);
    }

    @Test
    public void shouldCreateAndReturnAllRatings() {
        Rating rating = createRating();

        List<Rating> ratings = ratingDao.getAll();

        assertEquals(1, ratings.size());
        assertTrue(EqualsBuilder.reflectionEquals(rating, ratings.get(0)));
    }

    @Test
    public void shouldCreateAndFindRatingByUserAndProduct() {
        Rating rating = createRating();

        Rating foundRating = ratingDao.get(user, product);

        assertTrue(EqualsBuilder.reflectionEquals(rating, foundRating));
    }

    @NotNull
    private Rating createRating() {
        Rating rating = new Rating();
        rating.setUser(user);
        rating.setProduct(product);
        rating.setRating(RATING);
        ratingDao.create(rating);
        return rating;
    }
}

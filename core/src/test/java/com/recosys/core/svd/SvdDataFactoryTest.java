package com.recosys.core.svd;

import com.recosys.core.entity.Product;
import com.recosys.core.entity.Rating;
import com.recosys.core.entity.User;
import com.recosys.core.model.interfaces.ProductDao;
import com.recosys.core.model.interfaces.RatingDao;
import com.recosys.core.model.interfaces.UserDao;
import org.ejml.simple.SimpleMatrix;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static com.recosys.core.svd.SvdConstants.ROUNDING_ERROR;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SvdDataFactoryTest {
    private static final Long FIRST_USER_ID = 1001L;
    private static final Long SECOND_USER_ID = 1002L;
    private static final Long FIRST_PRODUCT_ID = 2001L;
    private static final Long SECOND_PRODUCT_ID = 2002L;
    private static final Long FIRST_RATING_ID = 3001L;
    private static final Long SECOND_RATING_ID = 3002L;
    private static final Integer FIRST_RATING = 5;
    private static final Integer SECOND_RATING = 3;

    private SvdDataInitializer svdDataInitializer;

    @Mock private UserDao userDao;
    @Mock private ProductDao productDao;
    @Mock private RatingDao ratingDao;

    private User firstUser = new User(FIRST_USER_ID);
    private User secondUser = new User(SECOND_USER_ID);
    private Product firstProduct = new Product(FIRST_PRODUCT_ID);
    private Product secondProduct = new Product(SECOND_PRODUCT_ID);
    private Rating firstRating = new Rating(FIRST_RATING_ID);
    private Rating secondRating = new Rating(SECOND_RATING_ID);

    {
        firstRating.setUser(firstUser);
        firstRating.setProduct(firstProduct);
        firstRating.setRating(FIRST_RATING);
        secondRating.setUser(secondUser);
        secondRating.setProduct(secondProduct);
        secondRating.setRating(SECOND_RATING);
    }

    @Before
    public void setUp() {
        initUserDao();
        initProductDao();
        initRatingDao();
        initSvdDataHolderGenerator();
    }

    private void initUserDao() {
        when(userDao.getAll()).thenReturn(asList(firstUser, secondUser));
    }

    private void initProductDao() {
        when(productDao.getAll()).thenReturn(asList(firstProduct, secondProduct));
    }

    private void initRatingDao() {
        when(ratingDao.getAll()).thenReturn(Arrays.asList(firstRating, secondRating));
    }

    private void initSvdDataHolderGenerator() {
        svdDataInitializer = new SvdDataInitializer();
        svdDataInitializer.setProductDao(productDao);
        svdDataInitializer.setRatingDao(ratingDao);
        svdDataInitializer.setUserDao(userDao);
    }

    @Test
    public void test() {
        SvdData dataHolder = svdDataInitializer.initData();

        SimpleMatrix ratingMatrix = dataHolder.getRatingMatrix();
        assertEquals(FIRST_RATING.doubleValue(), ratingMatrix.get(0, 0), ROUNDING_ERROR);
        assertFalse(dataHolder.isRated(1, 0));
        assertEquals(SECOND_RATING.doubleValue(), ratingMatrix.get(1, 1), ROUNDING_ERROR);
        assertFalse(dataHolder.isRated(0, 1));
    }
}

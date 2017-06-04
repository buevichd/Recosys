package com.recosys.core.svd;

import com.recosys.core.entity.EstimatedRating;
import com.recosys.core.entity.Product;
import com.recosys.core.entity.User;
import com.recosys.core.model.interfaces.EstimatedRatingDao;
import org.ejml.simple.SimpleMatrix;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SvdDataSaverTest {
    private SvdDataSaver manager;
    private User firstUser = new User(1001L);
    private User secondUser = new User(1002L);
    private Product firstProduct = new Product(2001L);
    private Product secondProduct = new Product(2002L);
    private EstimatedRating firstRating = new EstimatedRating(3001L);
    private EstimatedRating secondRating = new EstimatedRating(3002L);
    @Mock private SvdData dataHolder;
    @Mock private EstimatedRatingDao estimatedRatingDao;

    @Before
    public void setUp() {
        initDataHolder();
        initEstimatedRatingDao();
        initEstimatedRatingsDao();
    }

    private void initDataHolder() {
        SimpleMatrix estimatedRatingMatrix = new SimpleMatrix(new double[][]{{1.5, 4.2}, {2.3, 3.1}});
        when(dataHolder.getEstimatedRatingMatrix()).thenReturn(estimatedRatingMatrix);
        when(dataHolder.getUser(0)).thenReturn(firstUser);
        when(dataHolder.getUser(1)).thenReturn(secondUser);
        when(dataHolder.getProduct(0)).thenReturn(firstProduct);
        when(dataHolder.getProduct(2)).thenReturn(secondProduct);
        when(dataHolder.isRated(0, 0)).thenReturn(true);
        when(dataHolder.isRated(0, 1)).thenReturn(false);
        when(dataHolder.isRated(1, 0)).thenReturn(false);
        when(dataHolder.isRated(1, 1)).thenReturn(true);
    }

    private void initEstimatedRatingDao() {
        when(estimatedRatingDao.get(firstUser, firstProduct)).thenReturn(firstRating);
        when(estimatedRatingDao.get(secondUser, firstProduct)).thenReturn(secondRating);
    }

    private void initEstimatedRatingsDao() {
        manager = new SvdDataSaver();
        manager.setEstimatedRatingDao(estimatedRatingDao);
    }

    @Test
    public void test() {
        manager.saveData(dataHolder);

        verify(estimatedRatingDao).delete(firstRating);
        verify(estimatedRatingDao).create(any());
        verify(estimatedRatingDao).update(secondRating);
    }

}

package com.recosys.core.svd;

import org.ejml.simple.SimpleMatrix;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SvdDataEvaluatorTest {
    private SvdDataEvaluator svdDataEvaluator;

    @Before
    public void setUp() {

    }

    @Test
    public void test() {
        SimpleMatrix ratingMatrix = new SimpleMatrix(new double[][] {{3, 5, 1, 4}, {3, 0, 0, 4}, {0, 5, 0, 4}, {3, 0, 0, 0}});
        SimpleMatrix missingMatrix = new SimpleMatrix(new double[][] {{1, 1, 1, 1}, {1, 0, 0, 1}, {0, 1, 0, 1}, {1, 0, 0, 0}});
        SvdData dataHolder = new SvdData(ratingMatrix, missingMatrix, null, null);
        svdDataEvaluator = new SvdDataEvaluator();

        svdDataEvaluator.evaluateData(dataHolder);
        SimpleMatrix estimatedRatingMatrix = dataHolder.getEstimatedRatingMatrix();

        Assert.assertEquals(3.005, estimatedRatingMatrix.get(0, 0), 0.001);
        Assert.assertEquals(3.989, estimatedRatingMatrix.get(1, 3), 0.001);
        Assert.assertEquals(0.998, estimatedRatingMatrix.get(2, 2), 0.001);
    }
}

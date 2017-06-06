package com.recosys.core.svd;

import org.ejml.simple.SimpleMatrix;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import static com.recosys.core.svd.SvdConstants.*;
import static org.ejml.simple.SimpleMatrix.*;

@Service
public class SvdDataEvaluator {
    private int userCount;
    private int productCount;
    private SimpleMatrix ratingMatrix;
    private SimpleMatrix missingMatrix;
    private SimpleMatrix userMatrix;
    private SimpleMatrix productMatrix;

    public void evaluateData(SvdData dataHolder) {
        init(dataHolder);
        evaluate();
        dataHolder.setEstimatedRatingMatrix(userMatrix.mult(productMatrix.transpose()));
    }

    private void init(SvdData dataHolder) {
        this.ratingMatrix = dataHolder.getRatingMatrix();
        this.missingMatrix = dataHolder.getMissingMatrix();
        userCount = ratingMatrix.numRows();
        productCount = ratingMatrix.numCols();
        if (userMatrix == null && productMatrix == null) {
            userMatrix = generateInitialMatrix(userCount, SvdConstants.DIMENSION);
            productMatrix = generateInitialMatrix(productCount, SvdConstants.DIMENSION);
        }
    }

    @NotNull
    private SimpleMatrix generateInitialMatrix(int rowCount, int columnCount) {
        double[] data = DoubleStream.generate(() -> 1.0).limit(rowCount * columnCount).toArray();
        return new SimpleMatrix(rowCount, columnCount, true, data);
    }

    private void evaluate() {
        double oldCost;
        double newCost = getCostFunctionValue();
        do {
            oldCost = newCost;
            iterate();
            newCost = getCostFunctionValue();
        } while (oldCost - newCost > CALCULATION_ACCURACY || oldCost - newCost < 0);
    }

    private void iterate() {
        iterateProducts();
        iterateUsers();
    }

    private void iterateUsers() {
        for (int i = 0; i < userCount; i++) {
            SimpleMatrix wi = diag(missingMatrix, i);
            SimpleMatrix ri = row(ratingMatrix, i);
            SimpleMatrix ui = (productMatrix.transpose().mult(wi).mult(productMatrix).plus(identity(DIMENSION).
                    scale(LAMBDA))).invert().mult(productMatrix.transpose()).mult(wi).mult(ri.transpose());
            userMatrix.setRow(i, 0, ui.getMatrix().getData());
        }
    }

    private void iterateProducts() {
        for (int j = 0; j < productCount; j++) {
            SimpleMatrix wj = SimpleMatrix.diag(getColumn(missingMatrix, j).getMatrix().getData());
            SimpleMatrix rj = getColumn(ratingMatrix, j);
            SimpleMatrix pj = (userMatrix.transpose().mult(wj).mult(userMatrix).plus(identity(DIMENSION).
                    scale(LAMBDA))).invert().mult(userMatrix.transpose()).mult(wj).mult(rj);
            productMatrix.setRow(j, 0, pj.getMatrix().getData());
        }
    }

    private SimpleMatrix diag(SimpleMatrix matrix, int rowIndex) {
        int numCols = matrix.numCols();
        double[] row = IntStream.range(0, numCols).mapToDouble(columnIndex -> matrix.get(rowIndex, columnIndex)).toArray();
        return SimpleMatrix.diag(row);
    }

    private SimpleMatrix row(SimpleMatrix matrix, int rowIndex) {
        int numCols = matrix.numCols();
        double[] row = IntStream.range(0, numCols).mapToDouble(columnIndex -> matrix.get(rowIndex, columnIndex)).toArray();
        return new SimpleMatrix(1, numCols, true, row);
    }

    private SimpleMatrix getColumn(SimpleMatrix matrix, int columnIndex) {
        int numRows = matrix.numRows();
        double[] column = IntStream.range(0, numRows).mapToDouble(rowIndex -> matrix.get(rowIndex, columnIndex)).toArray();
        return new SimpleMatrix(numRows, 1, true, column);
    }

    private double getCostFunctionValue() {
        return (missingMatrix.elementMult(ratingMatrix.minus(userMatrix.mult(productMatrix.transpose())))).normF()
                + LAMBDA * (userMatrix.normF() + productMatrix.normF());
    }
}

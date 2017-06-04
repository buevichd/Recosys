package com.recosys.core.svd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SvdEngine {
    private SvdDataInitializer dataInitializer;
    private SvdDataEvaluator dataEvaluator;
    private SvdDataSaver dataSaver;

    synchronized public void calculateRecommendations() {
        SvdData dataHolder = dataInitializer.initData();
        dataEvaluator.evaluateData(dataHolder);
        dataSaver.saveData(dataHolder);
    }

    @Autowired
    public void setDataInitializer(SvdDataInitializer dataInitializer) {
        this.dataInitializer = dataInitializer;
    }

    @Autowired
    public void setDataEvaluator(SvdDataEvaluator dataEvaluator) {
        this.dataEvaluator = dataEvaluator;
    }

    @Autowired
    public void setDataSaver(SvdDataSaver dataSaver) {
        this.dataSaver = dataSaver;
    }
}

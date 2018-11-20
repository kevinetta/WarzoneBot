package com.emaraic.ml;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.SerializationHelper;

/**
 * This is a classifier for iris.2D.arff dataset  
 * @author Taha Emara 
 * Website: http://www.emaraic.com 
 * Email  : taha@emaraic.com
 * Created on: Jul 1, 2017
 * Github link: https://github.com/emara-geek/weka-example
 */
public class ModelClassifier {

    private Attribute total_armies;
    private Attribute total_territories;

    private ArrayList<Attribute> attributes;
    private ArrayList<String> classVal;
    private Instances dataRaw;


    public ModelClassifier() {
        total_armies = new Attribute("total_armies");
        total_territories = new Attribute("total_territories");
        attributes = new ArrayList<Attribute>();
        classVal = new ArrayList<String>();
        classVal.add("Win");
        classVal.add("Loss");
        classVal.add("Tie");

        attributes.add(total_armies);
        attributes.add(total_territories);

        attributes.add(new Attribute("class", classVal));
        dataRaw = new Instances("TestInstances", attributes, 0);
        dataRaw.setClassIndex(dataRaw.numAttributes() - 1);
    }

    
    public Instances createInstance(double total_armies, double total_territories, double result) {
        dataRaw.clear();
        double[] instanceValue1 = new double[]{total_armies, total_territories, 0};
        dataRaw.add(new DenseInstance(1.0, instanceValue1));
        return dataRaw;
    }


    public String classifiy(Instances insts, String path) {
        String result = "Not classified!!";
        Classifier cls = null;
        try {
            cls = (MultilayerPerceptron) SerializationHelper.read(path);
            result = classVal.get((int) cls.classifyInstance(insts.firstInstance()));
        } catch (Exception ex) {
            Logger.getLogger(ModelClassifier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }


    public Instances getInstance() {
        return dataRaw;
    }
    

}

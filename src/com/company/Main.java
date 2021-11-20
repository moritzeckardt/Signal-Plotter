package com.company;

public class Main {
    // Constants
    public static final double FIRST_LIMIT = -10;
    public static final double SECOND_LIMIT = 10;
    public static final int NUMBER_OF_POINTS = 1000;
    public static final int SAMPLING_RATE = 250;

    public static void main(String[] args) {
        //Invoke methods
        plotSigmoid();
        plotEcg();
    }

    //Methods
    public static double[] createSamplingPoints(double firstLimit, double secondLimit, int numberOfPoints) {
        // Check number of points
        if (firstLimit == secondLimit) {
            numberOfPoints = 1;
        }

        // Create array for base points
        double[] basePoints = new double[numberOfPoints];

        // Set base points correctly
        if (basePoints.length == 1) {
            basePoints[0] = secondLimit;
        }
        else {
            for (int i = 0; i < basePoints.length; i++) {
                basePoints[i] = firstLimit + i * (secondLimit - firstLimit) / (basePoints.length - 1);
            }
        }

        return basePoints;
    }

    public static double sigmoid(double x) {
        // Return result of sigmoid function
        return 1 / (1 + Math.pow(Math.E, -x));
    }

    public static double[] applySigmoidToArray(double[] xs) {
        // Create new array
        double[] sigmoidBasePoints = new double[xs.length];

        // Apply sigmoid function and set values
        for (int i = 0; i < sigmoidBasePoints.length; i++) {
            sigmoidBasePoints[i] = sigmoid(xs[i]);
        }

        return sigmoidBasePoints;
    }

    public static void plotSigmoid() {
        // Create base points and apply sigmoid function
        double[] basePoints = createSamplingPoints(FIRST_LIMIT, SECOND_LIMIT, NUMBER_OF_POINTS);
        double[] sigmoidBasePoints = applySigmoidToArray(basePoints);

        // Create 2D graph
        PlotHelper.plot2D(basePoints, sigmoidBasePoints);
    }

    public static void plotEcg() {
        // Read ecg file
        double[] egcSignal = PlotHelper.readEcg("ecg.txt");

        // Create ecg time (Wir benutzen hier die Konstanten nicht, weil diese konstant nur einen festen Wert haben duerfen, richtig?)
        double[] ecgTime = createSamplingPoints(0, (double) egcSignal.length / SAMPLING_RATE, egcSignal.length);

        // Read peaks file
        int[] idxRPeaks = PlotHelper.readPeaks("rpeaks.txt");

        // Store data points of peaks
        double[] rPeaks = new double[idxRPeaks.length];
        for(int i = 0; i < idxRPeaks.length; i++) {
            rPeaks[i] = egcSignal[idxRPeaks[i]];
        }

        // Store time points of peaks
        double[] timeRPeaks = new double[idxRPeaks.length];
        for(int i = 0; i < idxRPeaks.length; i++) {
            timeRPeaks[i] = ecgTime[idxRPeaks[i]];
        }

        // Compute and print heart rate
        computeHeartRate(timeRPeaks);

        // Create ecg graph with its peaks
        PlotHelper.plotEcg(ecgTime, egcSignal, timeRPeaks, rPeaks);
    }

    public static void computeHeartRate(double[] timeRPeaks) {
        // Print heart rate in bpm
        System.out.println("Heart Rate:");
        for (int i = 1; i < timeRPeaks.length; i++) {
            double heartRate = 60 / (timeRPeaks[i] - timeRPeaks[i - 1]);
            System.out.printf("%.2f bpm\n", heartRate);
        }
    }
}

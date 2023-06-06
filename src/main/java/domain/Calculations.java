package domain;

import java.util.*;

/**
 * Calculations is an abstract class with static methods for the various calculations performed by the LUCENZ program.
 * This includes those for kinetic model fitting and creating graph points.
 */
public abstract class Calculations {

    /**
     * Fits the Kinetic Model specified in the given Data object to the Data's input values.
     * Adds all relevant values to the Data object's textData field.
     *
     * @param data Data object containing all input and output data
     */
    public static void fit(Data data){
        // Extract values:
        double[] subsConc = data.getSubstrateCol().stream().mapToDouble(Double::doubleValue).toArray();
        double[][] reactRate = data.getTableData();
        int rows = data.getRows();
        int cols = data.getCols();

        // Initialise reciprocals:
        double[] subsConcRecip = new double[rows];
        double[][] reactRateRecip = new double[rows][cols];
        for (int row = 0; row < rows; row++){
            if (subsConc[row] == 0) throw new IllegalArgumentException("Substrate concentration is zero on row: " + row);
            subsConcRecip[row] = 1.0 / subsConc[row];

            for (int col = 0; col < cols; col++){
                if (reactRate[row][col] != 0){
                    reactRateRecip[row][col] = 1.0 / reactRate[row][col];
                }
            }
        }

        // Initialise parameters:
        int numParams;
        switch(data.getModelType()) {
            case UninhibitedOneSub:
                numParams = 2;
                break;
            case UninhibitedTwoSubOrderedBiBi:
                numParams = 4;
                break;
            case UninhibitedTwoSubPingPong:
                numParams = 3;
                break;
            case InhibitedCompetitive:
                numParams = 3;
                break;
            case InhibitedNonCompetitive:
                numParams = 4;
                break;
            case InhibitedUnCompetitive:
                numParams = 3;
                break;
            default:
                throw new IllegalArgumentException("Unrecognised model type: " + data.getModelType());
        }
        double[] par = new double[4];
        for (int i = 0; i < numParams; i++){
            par[i] = 1.0;
        }

        // Initialise weights:
        double[][] weights = new double[rows][cols];
        for (int row = 0; row < rows; row++){
            for (int col = 0; col < cols; col++){
                weights[row][col] = Math.pow(reactRate[row][col], 2);
            }
        }

        // Linear regression and error calculations:
        int numTests = 0;
        double[] result;
        double[][] S;
        double prevWeightedErrorSqr;
        double weightedErrorSqr = 0.0;
        double[][] theoreticalReactRate = new double[rows][cols]; // theory
        do { // loop until converged or number of tests reaches limit
            numTests += 1;
            prevWeightedErrorSqr = weightedErrorSqr; // ELS is the previous wES

            S = matrixInversion(data, par, subsConcRecip, reactRateRecip, weights, numParams);
            result = mySumSquaresRoutine(data, par, subsConcRecip, reactRateRecip, weights, numParams, theoreticalReactRate);

            weightedErrorSqr = result[2];
        } while (Math.abs((prevWeightedErrorSqr - weightedErrorSqr) / weightedErrorSqr) < 0.001 && numTests < 11);

        // Once finished converging:
        double chiSqr = result[1];

        for (int j = 0; j < numParams; j++){
            for (int k = 1; k < numParams + 1; k++){
                S[j][k] *= result[0];
            }
        }

        double[] SEP = new double[10];
        for (int j = 0; j < numParams; j++){
            SEP[j] = Math.sqrt(S[j][j + 1]);
        }

        calculateRegressionParameters(data, chiSqr, weightedErrorSqr, par, SEP, S);
        calculateGraphPoints(data, theoreticalReactRate, par);
    }

    /**
     * Finds the error squared value, chi squared value and weighted errors squared value
     *
     * @return returner[0] is errorSqr, returner[1] is chiSqr, returner[2] is weightedErrorSqr, returner[3] is sumOfWeights
     */
    private static double[] mySumSquaresRoutine(Data data, double[] par, double[] subsConcRecip, double[][] reactRateRecip, double[][] weights, int numParams, double[][] theoreticalReactRate) {
        double[][] reactRate = data.getTableData();
        int rows = data.getRows();
        int cols = data.getCols();
        int numCells = data.getNumCells();

        double[] returner = new double[4];
        double ts = 0;
        double sumOfWeights = 0.0;
        double sumWeightedErrorSqr = 0.0;
        double weightedErrorSqr = 0.0;
        int counter = 0;

        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                // 1/V = 1/Vm + Km/Vm * 1/[S]
                double YR;
                switch(data.getModelType()) {
                    case UninhibitedOneSub:
                        YR = par[0] + (par[1] * subsConcRecip[row]);
                        break;
                    case UninhibitedTwoSubOrderedBiBi:
                        YR = par[0] + par[1] * subsConcRecip[row] + par[2] / data.getCoSubInhibRow().get(col) + par[3] * subsConcRecip[row] / data.getCoSubInhibRow().get(col);
                        break;
                    case UninhibitedTwoSubPingPong:
                        YR = par[0] + par[1] * subsConcRecip[row] + par[2] / data.getCoSubInhibRow().get(col);
                        break;
                    case InhibitedCompetitive:
                        YR = par[0] + par[1] * subsConcRecip[row] + par[2] * data.getCoSubInhibRow().get(col) * subsConcRecip[row];
                        break;
                    case InhibitedNonCompetitive:
                        YR = par[0] + par[1] * subsConcRecip[row] + par[2] * data.getCoSubInhibRow().get(col) + par[3] * subsConcRecip[row] * data.getCoSubInhibRow().get(col);
                        break;
                    case InhibitedUnCompetitive:
                        YR = par[0] + par[1] * subsConcRecip[row] + par[2] * data.getCoSubInhibRow().get(col);
                        break;
                    default:
                        throw new IllegalArgumentException("Unrecognised model type: " + data.getModelType());
                }
                theoreticalReactRate[row][col] = 1.0/YR;
                double ty = reactRateRecip[row][col] - YR;
                ts += Math.pow(ty, 2) * weights[row][col];
                sumOfWeights += weights[row][col];

                if (reactRate[row][col] != 0.0){
                    double DY = (reactRateRecip[row][col] / YR) - 1.0;
                    weightedErrorSqr = Math.pow(DY, 2);
                    sumWeightedErrorSqr += weightedErrorSqr;
                }
                else {
                    counter += 1;
                    if (counter == 3){
                        counter = 0;
                        numCells -= 1;
                    }
                }
            }
        }

        ts /= (numCells - numParams + 1.0);
        returner[0] = ts; //errorSqr
        returner[1] = sumWeightedErrorSqr / (numCells - numParams + 1.0); //chiSqr
        returner[2] = ts * (numCells - numParams + 1.0) / sumOfWeights; //weightedErrorSqr
        returner[3] = sumOfWeights;
        return returner;
    }

    /**
     * Finds the best parameter values by fitting the point reciprocals to a line, and also calculates values that
     * aid with error and standard deviation/variance calculations.
     *
     * @param data Data object containing input data
     * @param par parameters
     * @param subsConcRecip substrate concentration reciprocals (xx)
     * @param reactRateRecip reaction rate reciprocals (yy)
     * @param weights weight matrix
     * @param numParams number of parameters
     * @return S matrix
     */
    private static double[][] matrixInversion(Data data, double[] par, double[] subsConcRecip, double[][] reactRateRecip, double[][] weights, int numParams){
        List<Double> cosubInhib = data.getCoSubInhibRow();
        int rows = data.getRows();
        int cols = data.getCols();

        // Clears all S arrays:
        double[] SS = new double[10];
        double[] SM = new double[10];
        double[][] S = new double[10][11];

        for (int col = 0; col < cols; col++){
            for (int row = 0; row < rows; row++){

                // Differentiation routine:
                double[] Q = new double[numParams+1];
                Q[0] = 1.0;
                Q[1] = subsConcRecip[row];
                switch(data.getModelType()) {
                    case UninhibitedOneSub:
                        Q[2] = reactRateRecip[row][col];
                        break;
                    case UninhibitedTwoSubOrderedBiBi:
                        Q[2] = 1.0 / cosubInhib.get(col);
                        Q[3] = subsConcRecip[row] / cosubInhib.get(col);
                        Q[4] = reactRateRecip[row][col];
                        break;
                    case UninhibitedTwoSubPingPong:
                        Q[2] = 1.0 / cosubInhib.get(col);
                        Q[3] = reactRateRecip[row][col];
                        break;
                    case InhibitedCompetitive:
                        Q[2] = subsConcRecip[row] * cosubInhib.get(col);
                        Q[3] = reactRateRecip[row][col];
                        break;
                    case InhibitedNonCompetitive:
                        Q[2] = cosubInhib.get(col);
                        Q[3] = subsConcRecip[row] * cosubInhib.get(col);
                        Q[4] = reactRateRecip[row][col];
                        break;
                    case InhibitedUnCompetitive:
                        Q[2] = cosubInhib.get(col);
                        Q[3] = reactRateRecip[row][col];
                        break;
                    default:
                        throw new IllegalArgumentException("Unrecognised model type: " + data.getModelType());
                }


                for (int m = 0; m < numParams + 1; m++){
                    for (int k = 0; k < numParams; k++){
                        S[k][m] += Q[k] * Q[m] * weights[row][col];
                    }
                }
            }
        }

        //aei
        for (int k = 0; k < numParams; k++){
            double TQ = Math.sqrt(S[k][k]);
            if (TQ == 0) TQ = 0.000001;
            SM[k] = 1.0 / TQ;
        }

        SM[numParams] = 1.0;

        for (int j = 0; j < numParams + 1; j++){
            for (int k = 0; k < numParams; k++){
                S[k][j] *= (SM[k] * SM[j]);
            }
        }

        SS[numParams] = -1.0;
        S[0][numParams + 1] = 1.0;

        for (int l = 0; l < numParams; l++){
            for (int k = 0; k < numParams; k++){
                SS[k] = S[k][0];
            }

            if (SS[0] == 0) SS[0] = 0.0000001;

            for (int j = 0; j < numParams + 1; j++){
                for (int k = 0; k < numParams; k++){
                    S[k][j] = S[k + 1][j + 1] - (SS[k + 1] * S[0][j + 1] / SS[0]);
                }
            }

        }

        for (int k = 0; k < numParams; k++){
            S[k][0] *= SM[k];
        }

        for (int j = 1; j < numParams + 1; j++){
            for (int k = 0; k < numParams; k++){
                S[k][j] *= (SM[k] * SM[j - 1]);
            }
        }

        for (int j = 0; j < numParams; j++){
            par[j] = S[j][0];
        }

        return S;
    }

    /**
     * Calculates the regression parameters for the chosen kinetic model, and outputs them to the Data object.
     * Corresponds to the "Regression Parameter Printout" section of the original LUCENZ code.
     *
     * @param data Data object to output parameter values to
     * @param chiSqr chi-square
     * @param weightedErrorSqr weighted error squared
     * @param par parameters
     * @param SEP SEP array
     * @param S S matrix
     */
    private static void calculateRegressionParameters(Data data, double chiSqr, double weightedErrorSqr, double[] par, double[] SEP, double[][] S){
        Map<String, Double> textData = new HashMap<>();

        // Calculate and output common parameters across all models:
        chiSqr = Math.sqrt(chiSqr);

        textData.put("wes", weightedErrorSqr);
        textData.put("chis", chiSqr);

        double vm = 1.0 / par[0];
        double sev = SEP[0] / Math.pow(par[0], 2) / vm;
        double km = par[1] / par[0];
        double varkm = (Math.pow(SEP[0], 2) * Math.pow(par[1], 2) / Math.pow(par[0], 4))
                + (Math.pow(SEP[1], 2) / Math.pow(par[0], 2))
                + (2.0 * S[0][2] * par[1] / Math.pow(par[0], 3));
        double sekm = Math.sqrt(varkm) / km;

        textData.put("vm", vm);
        textData.put("sevm", sev);

        // Calculate and output remaining parameters specific to the selected kinetic model:
        switch (data.getModelType()){
            case UninhibitedOneSub: // 1
                textData.put("km", km);
                textData.put("sekm", sekm);
                break;

            case UninhibitedTwoSubOrderedBiBi: // 2
                double kia = par[3] / par[2];
                double varkia = Math.pow(SEP[2], 2) * Math.pow(par[3], 2) / Math.pow(par[2], 4)
                        + Math.pow(SEP[3], 2) / Math.pow(par[2], 2)
                        + (2.0 * S[2][4] * par[3] / Math.pow(par[2], 3));
                double sekia = Math.sqrt(varkia) / kia;
                double kb = par[2] / par[0];
                double varkb = Math.pow(SEP[0], 2) * Math.pow(par[2], 2) / Math.pow(par[0], 4)
                        + Math.pow(SEP[2], 2) / Math.pow(par[0], 2)
                        + (2.0 * S[0][3] * par[2] / Math.pow(par[0], 3));
                double sekb = Math.sqrt(varkb) / kb;

                textData.put("ka", km);
                textData.put("seka", sekm);
                textData.put("kb", kb);
                textData.put("sekb", sekb);
                textData.put("kia", kia);
                textData.put("sekia", sekia);
                break;

            case UninhibitedTwoSubPingPong: // 3
                kb = par[2] / par[0];
                varkb = Math.pow(SEP[0], 2) * Math.pow(par[2], 2) / Math.pow(par[0], 4)
                        + Math.pow(SEP[2], 2) / Math.pow(par[0], 2)
                        + (2.0 * S[0][3] * par[2] / Math.pow(par[0], 3));
                sekb = Math.sqrt(varkb) / kb;

                textData.put("ka", km);
                textData.put("seka", sekm);
                textData.put("kb", kb);
                textData.put("sekb", sekb);
                break;

            case InhibitedCompetitive: // 4
                double kis = par[1] / par[2];
                double varkis = Math.pow(SEP[2], 2) * Math.pow(par[1], 2) / Math.pow(par[2], 4)
                        + Math.pow(SEP[1], 2) / Math.pow(par[2], 2)
                        + (2.0 * S[1][3] * par[1] / Math.pow(par[2], 3));
                double sekis = Math.sqrt(varkis) / kis;

                textData.put("km", km);
                textData.put("sekm", sekm);
                textData.put("kis", kis);
                textData.put("sekis", sekis);
                break;

            case InhibitedNonCompetitive: // 5
                kis = par[1] / par[3];
                varkis = Math.pow(SEP[3], 2) * Math.pow(par[1], 2) / Math.pow(par[3], 4)
                        + Math.pow(SEP[1], 2) / Math.pow(par[3], 2)
                        + (2.0 * S[1][4] * par[1] / Math.pow(par[3], 3));
                sekis = Math.sqrt(varkis) / kis;
                double kii = par[0] / par[2];
                double varkii = Math.pow(SEP[2], 2) * Math.pow(par[0], 2) / Math.pow(par[2], 4)
                        + Math.pow(SEP[0], 2) / Math.pow(par[2], 2)
                        + (2.0 * S[0][3] * par[0] / Math.pow(par[2], 3));
                double sekii = Math.sqrt(varkii) / kii;

                textData.put("km", km);
                textData.put("sekm", sekm);
                textData.put("kii", kii);
                textData.put("sekii", sekii);
                textData.put("kis", kis);
                textData.put("sekis", sekis);
                break;

            case InhibitedUnCompetitive: // 6
                kii = par[0] / par[2];
                varkii = Math.pow(SEP[2], 2) * Math.pow(par[0], 2) / Math.pow(par[2], 4)
                        + Math.pow(SEP[0], 2) / (par[2] * par[2])
                        + (2.0 * S[0][3] * par[0] / Math.pow(par[2], 3));
                sekii = Math.sqrt(varkii) / kii;

                textData.put("km", km);
                textData.put("sekm", sekm);
                textData.put("kii", kii);
                textData.put("sekii", sekii);
                break;

            default:
                throw new IllegalArgumentException("Unrecognised model type: " + data.getModelType());
        }
        data.setTextData(textData);
    }

    /**
     * Calculates the theoretical and experiment (raw data) graph points for the given input Data,
     * and outputs it to the Data object.
     *
     * @param data Data object containing input data
     * @param theoreticalReactRate y values for lines of best fit
     * @param par parameters for lines of best fit
     */
    private static void calculateGraphPoints(Data data, double[][] theoreticalReactRate, double[] par){
        double[] subsConc = data.getSubstrateCol().stream().mapToDouble(Double::doubleValue).toArray();
        double[][] reactRate = data.getTableData();
        List<Double> cosubInhib = data.getCoSubInhibRow();
        int rows = data.getRows();
        int cols = data.getCols();
        Map<String, Double> textData = data.getTextData();

        // Calculate Graph Values from selected Graph Type:
        List<List<Point>> theoreticalPointsList = new ArrayList<>(); // lines of best fit. Points = (xgraph, ycalc)
        List<List<Point>> experimentPointsList = new ArrayList<>(); // raw data points marked on graph. Points = (xexp, yexp)
        GraphType graphType = data.getGraphType(); // IP5%
        switch (graphType){

            case VelocityVS: // unin
                // For each inhibitor/co-substrate (or just once if only one substrate):
                for (int col = 0; col < cols; col++){
                    List<Point> theoreticalPoints = new ArrayList<>();
                    List<Point> experimentPoints = new ArrayList<>();

                    theoreticalPoints.add(new Point(0.0, 0.0));

                    for (int row = 0; row < rows; row++){
                        theoreticalPoints.add(new Point(subsConc[row], theoreticalReactRate[row][col]));
                        if (reactRate[row][col] != 0.0) experimentPoints.add(new Point(subsConc[row], reactRate[row][col]));
                        else experimentPoints.add(null);
                    }
                    theoreticalPointsList.add(theoreticalPoints);
                    experimentPointsList.add(experimentPoints);
                }
                break;

            case LineweaverBurke: // mm
                // For each inhibitor/co-substrate (or just once if only one substrate):
                for (int col = 0; col < cols; col++){
                    List<Point> theoreticalPoints = new ArrayList<>();
                    List<Point> experimentPoints = new ArrayList<>();

                    // Calculate the first theoretical point for this kinetic model:
                    Point firstPoint;
                    switch(data.getModelType()){
                        case UninhibitedOneSub:
                            firstPoint = new Point(
                                    -par[0] / par[1],
                                    0.0
                            );
                            break;
                        case UninhibitedTwoSubOrderedBiBi:
                            firstPoint = new Point(
                                    -(1.0 + textData.get("kb") / cosubInhib.get(col)) / (textData.get("ka") * (1.0 + textData.get("kia") * textData.get("kb") / (textData.get("ka") * cosubInhib.get(col)))),
                                    0.0
                            );
                            break;
                        case UninhibitedTwoSubPingPong:
                            firstPoint = new Point(
                                    -(1.0 + textData.get("kb")/ cosubInhib.get(col))/ textData.get("ka"),
                                    0.0
                            );
                            break;
                        case InhibitedCompetitive:
                            firstPoint = new Point(
                                    -par[0] / ((1.0 + cosubInhib.get(col) / textData.get("kis")) * par[1]),
                                    0.0
                            );
                            break;
                        case InhibitedNonCompetitive:
                            firstPoint = new Point(
                                    -(par[0] / par[1]) * (1.0 + cosubInhib.get(col) / textData.get("kii")) / (1.0 + cosubInhib.get(col) / textData.get("kis")),
                                    0.0
                            );
                            break;
                        case InhibitedUnCompetitive:
                            firstPoint = new Point(
                                    -par[0] * ((1.0 + cosubInhib.get(col) / textData.get("kii")) / par[1]),
                                    0.0
                            );
                            break;
                        default:
                            throw new IllegalArgumentException("Unrecognised model type: " + data.getModelType());
                    }
                    theoreticalPoints.add(firstPoint);

                    for (int row = 0; row < rows; row++){
                        theoreticalPoints.add(new Point(1.0 / subsConc[row], 1.0 / theoreticalReactRate[row][col]));
                        if (reactRate[row][col] != 0.0) experimentPoints.add(new Point(1.0 / subsConc[row], 1.0 / reactRate[row][col]));
                        else experimentPoints.add(null);
                    }
                    theoreticalPointsList.add(theoreticalPoints);
                    experimentPointsList.add(experimentPoints);
                }
                break;

            case Hanes:
                // For each inhibitor/co-substrate (or just once if only one substrate):
                for (int col = 0; col < cols; col++){
                    List<Point> theoreticalPoints = new ArrayList<>();
                    List<Point> experimentPoints = new ArrayList<>();

                    // Calculate the first theoretical point for this kinetic model:
                    Point firstPoint;
                    switch(data.getModelType()) {
                        case UninhibitedOneSub:
                            firstPoint = new Point(
                                    -par[1] / par[0],
                                    0.0
                            );
                            break;
                        case UninhibitedTwoSubOrderedBiBi:
                            firstPoint = new Point(
                                    -(textData.get("ka") * (1.0 + textData.get("kia") * textData.get("kb") / (textData.get("ka") * cosubInhib.get(col)))) / (1.0 + textData.get("kb") / cosubInhib.get(col)),
                                    0.0
                            );
                            break;
                        case UninhibitedTwoSubPingPong:
                            firstPoint = new Point(
                                    -par[1] / ((1.0 + textData.get("kb") / cosubInhib.get(col)) * par[0]),
                                    0.0
                            );
                            break;
                        case InhibitedCompetitive:
                            firstPoint = new Point(
                                    -((1.0 + cosubInhib.get(col) / textData.get("kis")) * par[1]) / par[0],
                                    0.0
                            );
                            break;
                        case InhibitedNonCompetitive:
                            firstPoint = new Point(
                                    -(par[1] / par[0]) * (1.0 + cosubInhib.get(col) / textData.get("kis")) / (1.0 + cosubInhib.get(col) / textData.get("kii")),
                                    0.0
                            );
                            break;
                        case InhibitedUnCompetitive:
                            firstPoint = new Point(
                                    -par[1] / (par[0] * (1.0 + cosubInhib.get(col) / textData.get("kii"))),
                                    0.0
                            );
                            break;
                        default:
                            throw new IllegalArgumentException("Unrecognised model type: " + data.getModelType());
                    }
                    theoreticalPoints.add(firstPoint);

                    for (int row = 0; row < rows; row++){
                        theoreticalPoints.add(new Point(subsConc[row], subsConc[row] / theoreticalReactRate[row][col]));
                        if (reactRate[row][col] != 0.0) experimentPoints.add(new Point(subsConc[row], subsConc[row] / reactRate[row][col]));
                        else experimentPoints.add(null);
                    }
                    theoreticalPointsList.add(theoreticalPoints);
                    experimentPointsList.add(experimentPoints);
                }
                break;

            case EadieHofstee: // Eadie
                // For each inhibitor/co-substrate (or just once if only one substrate):
                for (int col = 0; col < cols; col++){
                    List<Point> theoreticalPoints = new ArrayList<>();
                    List<Point> experimentPoints = new ArrayList<>();

                    // Calculate the first theoretical point for this kinetic model:
                    Point firstPoint;
                    switch (data.getModelType()){
                        case UninhibitedOneSub:
                            firstPoint = new Point(
                                    textData.get("vm") / textData.get("km"),
                                    0.0
                            );
                            break;
                        case UninhibitedTwoSubOrderedBiBi:
                            firstPoint = new Point(
                                    textData.get("vm") / (textData.get("ka") + textData.get("kia") * textData.get("kb") / cosubInhib.get(col)),
                                    0.0
                            );
                            break;
                        case UninhibitedTwoSubPingPong:
                            firstPoint = new Point(
                                    textData.get("vm") / textData.get("ka"),
                                    0.0
                            );
                            break;
                        case InhibitedCompetitive:
                            firstPoint = new Point(
                                    (textData.get("vm") / textData.get("km")) / (1.0 + cosubInhib.get(col) / textData.get("kis")),
                                    0.0
                            );
                            break;
                        case InhibitedNonCompetitive:
                            firstPoint = new Point(
                                    (textData.get("vm") / textData.get("km")) / (1.0 + cosubInhib.get(col) / textData.get("kis")),
                                    0.0
                            );
                            break;
                        case InhibitedUnCompetitive:
                            firstPoint = new Point(
                                    textData.get("vm") / textData.get("km"),
                                    0.0
                            );
                            break;
                        default:
                            throw new IllegalArgumentException("Unrecognised model type: " + data.getModelType());
                    }
                    theoreticalPoints.add(firstPoint);

                    for (int row = 0; row < rows; row++){
                        theoreticalPoints.add(new Point(theoreticalReactRate[row][col] / subsConc[row], theoreticalReactRate[row][col]));
                        if (reactRate[row][col] != 0.0) experimentPoints.add(new Point(reactRate[row][col] / subsConc[row], reactRate[row][col]));
                        else experimentPoints.add(null);
                    }

                    // Calculate the last theoretical point for this kinetic model:
                    Point lastPoint;
                    switch (data.getModelType()){
                        case UninhibitedOneSub:
                            lastPoint = new Point(
                                    0.0,
                                    textData.get("vm")
                            );
                            break;
                        case UninhibitedTwoSubOrderedBiBi:
                            lastPoint = new Point(
                                    0.0,
                                    textData.get("vm") / (1.0 + textData.get("kb") / cosubInhib.get(col))
                            );
                            break;
                        case UninhibitedTwoSubPingPong:
                            lastPoint = new Point(
                                    0.0,
                                    textData.get("vm") / (1.0 + textData.get("kb") / cosubInhib.get(col))
                            );
                            break;
                        case InhibitedCompetitive:
                            lastPoint = new Point(
                                    0.0,
                                    textData.get("vm")
                            );
                            break;
                        case InhibitedNonCompetitive:
                            lastPoint = new Point(
                                    0.0,
                                    textData.get("vm") / (1.0 + cosubInhib.get(col) / textData.get("kii"))
                            );
                            break;
                        case InhibitedUnCompetitive:
                            lastPoint = new Point(
                                    0.0,
                                    textData.get("vm") / (1.0 + cosubInhib.get(col) / textData.get("kii"))
                            );
                            break;
                        default:
                            throw new IllegalArgumentException("Unrecognised model type: " + data.getModelType());
                    }
                    theoreticalPoints.add(lastPoint);

                    theoreticalPointsList.add(theoreticalPoints);
                    experimentPointsList.add(experimentPoints);
                }
                break;

            case Dixon:
                // Calculate points along a theoretical line for each row:
                for (int row = 0; row < rows; row++) {
                    List<Point> theoreticalPoints = new ArrayList<>();

                    // Calculate the first theoretical point for this kinetic model:
                    Point firstPoint;
                    switch(data.getModelType()){
                        case InhibitedCompetitive:
                            firstPoint = new Point(
                                    -textData.get("kis") * (1.0 + subsConc[row] / textData.get("km")),
                                    0.0
                            );
                            break;
                        case InhibitedNonCompetitive:
                            firstPoint = new Point(
                                    -textData.get("kis") * (1.0 + textData.get("km") / subsConc[row]) / (textData.get("kis") / textData.get("kii") + textData.get("km") / subsConc[row]),
                                    0.0
                            );
                            break;
                        case InhibitedUnCompetitive:
                            firstPoint = new Point(
                                    -textData.get("kii") * (1.0 + textData.get("km") / subsConc[row]),
                                    0.0
                            );
                            break;
                        default:
                            throw new IllegalArgumentException("Unrecognised model type: " + data.getModelType() +
                                    " (Note that an Inhibited model should be selected when using a Dixon plot)");
                    }
                    theoreticalPoints.add(firstPoint);

                    // Calculate the following theoretical points:
                    for (int col = 0; col < cols; col++){
                        theoreticalPoints.add(new Point(cosubInhib.get(col), 1.0 / theoreticalReactRate[row][col]));
                    }
                    theoreticalPointsList.add(theoreticalPoints);
                }

                // For each inhibitor concentration, add the raw data points:
                for (int col = 0; col < cols; col++){
                    List<Point> experimentPoints = new ArrayList<>();
                    for (int row = 0; row < rows; row++){
                        if (reactRate[row][col] != 0.0) experimentPoints.add(new Point(cosubInhib.get(col), 1.0 / reactRate[row][col]));
                        else experimentPoints.add(null);
                    }
                    experimentPointsList.add(experimentPoints);
                }
                break;

            case HunterDowns: // Hunter
                // For each inhibitor concentration (skip the first column):
                for (int col = 1; col < cols; col++){
                    List<Point> theoreticalPoints = new ArrayList<>();
                    List<Point> experimentPoints = new ArrayList<>();

                    for (int row = 0; row < rows; row++){
                        double theoreticalAlpha = theoreticalReactRate[row][col] / theoreticalReactRate[row][0];
                        theoreticalPoints.add(new Point(
                                subsConc[row],
                                cosubInhib.get(col) * theoreticalAlpha / (1.0 - theoreticalAlpha)
                        ));
                        if (reactRate[row][0] != 0.0) {
                            double experimentAlpha = reactRate[row][col] / reactRate[row][0];
                            experimentPoints.add(new Point(
                                    subsConc[row],
                                    cosubInhib.get(col) * experimentAlpha / (1.0 - experimentAlpha)
                            ));
                        }
                        else experimentPoints.add(null);
                    }
                    theoreticalPointsList.add(theoreticalPoints);
                    experimentPointsList.add(experimentPoints);
                }
                break;

            default:
                throw new IllegalArgumentException("Unrecognised graph type: " + graphType);
        }

        // Set points:
        data.setGraphLine(theoreticalPointsList);
        data.setGraphPoints(experimentPointsList);
    }
}

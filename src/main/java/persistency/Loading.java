package persistency;

import domain.Data;
import domain.KineticModel;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Loading describes an abstract helper class with a single static method
 * for loading Data from a file.
 */
public abstract class Loading {

    /**
     * Loads the input file from the given filePath, and parses the data into a Data object.
     *
     * @param filePath file path of the input file to read data from
     * @return the parsed Data object
     */
    public static Data load(String filePath) throws Exception {
        Scanner scanner = new Scanner(Path.of(filePath));
        String[] tokens;

        //Split nextLine into tokens to parse depending on the file type
        if (filePath.endsWith(".csv")) { tokens = scanner.nextLine().split(",");
        } else { tokens = scanner.nextLine().split("             ");}

        Data data = new Data();
        data.setFilePath(filePath);
        System.out.println(data.getFilePath());
        
        // Read first row:
        int cols = Integer.parseInt(tokens[0].trim()); // 1st token is table columns
        int rows = Integer.parseInt(tokens[1].trim()); // 2nd token is table rows
        data.setCols(cols);
        data.setRows(rows);
        
        switch (Integer.parseInt(tokens[2].trim())){ // 3rd token is %UP, or the Kinetic Model selected
            case 1:
                data.setModelType(KineticModel.UninhibitedOneSub);
                break;
            case 2:
                data.setModelType(KineticModel.UninhibitedTwoSubOrderedBiBi);
                break;
            case 3:
                data.setModelType(KineticModel.UninhibitedTwoSubPingPong);
                break;
            case 4:
                data.setModelType(KineticModel.InhibitedCompetitive);
                break;
            case 5:
                data.setModelType(KineticModel.InhibitedNonCompetitive);
                break;
            case 6:
                data.setModelType(KineticModel.InhibitedUnCompetitive);
                break;
            default:
                throw new IllegalArgumentException();
        }
        
        // Read 2nd row (cosub/inhib row)
        List<Double> cosubInhibList = new ArrayList<>();

        //Split nextLine into tokens to parse depending on the file type
        if (filePath.endsWith(".csv")) { tokens = scanner.nextLine().split(",", -1);
        } else { tokens = scanner.nextLine().split("   ", -1);}
        
        for (String token : tokens){
            cosubInhibList.add(parseNumber(token));
        }
        data.setCoSubInhibRow(cosubInhibList);

        // Read the last rows (table data with substrate column on left):
        List<Double> substrateList = new ArrayList<>();
        double[][] tableData = new double[rows][cols];
        
        for (int row = 0; row < rows; row++){
            //Split nextLine into tokens to parse depending on the file type
            if (filePath.endsWith(".csv")) { tokens = scanner.nextLine().split(",", -1);
            } else { tokens = scanner.nextLine().split("   ", -1);}

            substrateList.add(parseNumber(tokens[0]));
            
            for (int col = 0; col < cols; col++){
                String token = tokens[col+1];
                tableData[row][col] = parseNumber(token);
            }
        }
        data.setSubstrateCol(substrateList);
        data.setTableData(tableData);
        return data;
    }

    /**
     * Parses a String into its double form, or Double.NaN if it is blank.
     * This also rounds the number to 3dp.
     * 
     * @param token input String
     * @return double
     */
    private static Double parseNumber(String token){
        return (token.trim().isEmpty()) ? Double.NaN : Math.round(Double.parseDouble(token) * 10000.0) / 10000.0;
    }
}

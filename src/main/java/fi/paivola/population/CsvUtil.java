package fi.paivola.population;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CsvUtil {
    public static double[] readDoubles(String filename) {
        return readDoubles(filename, 0);
    }
    
    /**
     * @param filename
     * @param rowNumber Which line from file
     * @return Returns an empty array on failure
     */
    public static double[] readDoubles(String filename, int rowNumber) {
        String csvFile = filename;
        BufferedReader br = null;
        String line = "";
        String separator = ",";
        
	try {
		br = new BufferedReader(new FileReader(csvFile));
                ArrayList<String> sValues = new ArrayList<String>();
		for (int rowIndex = 0; (line = br.readLine()) != null; ++rowIndex) {
                    if ( rowIndex == rowNumber )
                    {
                        // use comma as separator
                        String[] lineValues = line.split(separator);
                        for (String lineVal : lineValues)
                        {
                            sValues.add( lineVal );
                        }
                    }
		}
                double[] dValues = new double[sValues.size()];
                for (int i = 0; i != sValues.size(); ++i)
                {
                    dValues[i] = Double.parseDouble( sValues.get(i) );
                }
                return dValues;
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
        
        return new double[0];
  }
}

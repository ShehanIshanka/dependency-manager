package utils.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * File utilities class
 *
 */
public final class FileUtilities {
    /**
     * FileUtilities private constructor
     *
     */
    private FileUtilities() {
        //Nothing to initialize
    }

    /**
     * Method to read contents of the file in given path to a list.
     *
     * @param filePath path to the file
     * @return Content of the file in a list
     */
    public static List<String> readFile(String filePath) throws FileAccessException {
        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            throw new FileAccessException("Unable to read the file : " + filePath, e);
        }
    }

    public static String readFileString(String filePath) throws FileAccessException {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            throw new FileAccessException("Unable to read the file : " + filePath, e);
        }
    }


}
package org.itmo.mse.utils;

import java.io.InputStream;
import lombok.experimental.UtilityClass;

/**
 * Utility class to work with files
 */
@UtilityClass
public class FileUtils {
    /**
     * Opens file from resource folders
     * @param fileName name of file from resources to open
     * @return InputStream with content of file
     */
    public InputStream getFileFromResource(String fileName) {
        InputStream stream = FileUtils.class.getClassLoader().getResourceAsStream(fileName);
        if (stream == null) {
            throw new IllegalArgumentException("File not found: " + fileName);
        }
        return stream;
    }
}

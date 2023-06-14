package org.itmo.mse.utils;

import lombok.experimental.UtilityClass;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility class to work with files
 */
@UtilityClass
public class FileUtils {
    /**
     * Opens file from resource folders
     *
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
    
    /**
     * Get file by name for input
     *
     * @param fileName name of file from resources to open
     * @return InputStream with content of file
     */
    public InputStream getFile(String fileName) throws IOException {
        return Files.newInputStream(Path.of(fileName));
    }
    
    /**
     * Get file by name for output
     *
     * @return OutputStream for write to file
     */
    public OutputStream getFileForWrite(String fileName) throws FileNotFoundException {
        return new FileOutputStream(fileName);
    }
}

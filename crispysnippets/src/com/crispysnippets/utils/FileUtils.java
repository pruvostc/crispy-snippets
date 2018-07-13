package com.crispysnippets.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
//import java.util.logging.Logger;

public class FileUtils {

  //private static final Logger LOGGER = Logger.getLogger(FileUtils.class.getName());
  
  /**
   * Method to read the contents of a file into a string.
   * @param fileName The name of the file to read with its associated path
   * @return A string containing the contents of the file
   * @throws FileNotFoundException when file location fails
   * @throws IOException when file read fails
   */
  public static String fileToString(String fileName)
      throws FileNotFoundException, IOException {

    FileReader fr = null;
    String fileContents = new String();
    fr = new FileReader(fileName);
    int ch = 0;
    ch = fr.read();
    while (ch != -1) {
      fileContents = fileContents + (char) ch;
      ch = fr.read();
    }
    fr.close();

    return fileContents;
  }
  
}

import java.util.*;
import java.io.*;
interface FileOperations {
    List<String> readFile(String fileName) throws IOException;
    void writeFile(String fileName, List<String> data) throws IOException;
    void appendToFile(String fileName, String data) throws IOException;
}

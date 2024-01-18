package agh.ics.oop.model.statistics;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVWriter {
    private final String filePath;

    public CSVWriter(String filePath){
        this.filePath = filePath;
    }

    public void write(List<String> data){
        try (FileWriter writer = new FileWriter(filePath, true)){
            for (String row : data){
//                writer.append(String.join(";",row));
                writer.append(row);
                writer.append(";");
            }
            writer.append("\n");
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}

package subprocesses;

import java.io.FileWriter;
import java.util.ArrayList;

public class SaveMaker {
    
    public SaveMaker() {
    }

    public void createSave(ArrayList<String> content) {     
        // Open file
        String path = "save.txt";
        //try {
            //this.file = new File(path);
            //this.file.createNewFile();
        //} catch (Exception e) {
        //}

        // Format content to String
        String fileContent = "---\n"+String.join("\n", content)+"\n";


        // Write Contents
        try{
            FileWriter writer = new FileWriter(path, true);
            writer.write(fileContent);
            writer.close();
        } catch (Exception e) {
        }
    }
}

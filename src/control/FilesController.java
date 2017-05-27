package control;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;

import java.io.File;

public class FilesController {

    public FilesController(){

    }

    @FXML
     String importFile() {

        File musicFile;
        String filePath = "";

        try {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(".midi","*.mid"));

            musicFile = fc.showOpenDialog(null);
            if (musicFile != null) {
                filePath = musicFile.getAbsolutePath();
                filePath = filePath.replace("\\", "/");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return filePath;
    }

}

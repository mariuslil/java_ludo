package no.ntnu.imt3281.ludo.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class WaitDialogController {

        @FXML
        private TextArea textArea;

        @FXML
        private Text lastMsg;

        public void updateTextArea(String text){
            setLastMsg(text);
            this.textArea.setText(textArea.getText()+"\n"+text);
        }

        private void setLastMsg(String text){
            this.lastMsg.setText(text);
        }
}

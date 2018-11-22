package no.ntnu.imt3281.ludo.gui;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ChatRoomsDialog {

    @FXML
    private ListView<String> roomList;

    private ObservableList<String> rooms = FXCollections.observableArrayList();

}

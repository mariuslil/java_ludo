package no.ntnu.imt3281.ludo.gui;


import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomsDialog {

    private LudoController ludoController;
    private ObservableList<String> roomList = FXCollections.observableArrayList();

    protected ChatRoomsDialog(LudoController ludoController){
        this.ludoController = ludoController;
    }

    @FXML
    public void initialize(){
        ludoController.sendRoomRequestToServer();
        listView.setItems(roomList);
    }

    @FXML
    private ListView<String> listView;

    @FXML
    private TextField newRoomText;

    @FXML
    private Button newRoomBtn;

    @FXML
    void serverAddRoom(ActionEvent event) {
        if(newRoomText.getText()!=null && !newRoomText.getText().equals("")) {
            ludoController.requestCreateChat(newRoomText.getText());
            ludoController.sendRoomRequestToServer();
            roomList.removeAll();
        }
    }

    @FXML
    public void addRoom(String roomName){
        Platform.runLater(()-> roomList.add(roomName));
    }
}

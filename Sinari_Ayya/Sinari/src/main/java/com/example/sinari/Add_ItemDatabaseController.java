package com.example.sinari;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Add_ItemDatabaseController {

    public class Table_AddItem {

        private StringProperty itemCode;
        private StringProperty vehicleCode;
        private StringProperty itemName;
        private SimpleDoubleProperty unitPrice;
        private SimpleIntegerProperty quantity;

        public Table_AddItem(String itemCode, String vehicleCode, String itemName, Double unitPrice, int quantity) {
            this.itemCode = new SimpleStringProperty(itemCode);
            this.vehicleCode = new SimpleStringProperty(vehicleCode);
            this.itemName = new SimpleStringProperty(itemName);
            this.unitPrice = new SimpleDoubleProperty(unitPrice);
            this.quantity = new SimpleIntegerProperty(quantity);
        }

        public String getItemCode() {
            return itemCode.get();
        }

        public StringProperty itemCodeProperty() {
            return itemCode;
        }

        public String getVehicleCode() {
            return vehicleCode.get();
        }

        public StringProperty vehicleCodeProperty() {
            return vehicleCode;
        }

        public String getItemName() {
            return itemName.get();
        }

        public StringProperty itemNameProperty() {
            return itemName;
        }

        public double getUnitPrice() {
            return unitPrice.get();
        }

        public SimpleDoubleProperty unitPriceProperty() {
            return unitPrice;
        }

        public int getQuantity() {
            return quantity.get();
        }

        public SimpleIntegerProperty quantityProperty() {
            return quantity;
        }

        public void setItemCode(String itemCode) {
            this.itemCode.set(itemCode);
        }

        public void setVehicleCode(String vehicleCode) {
            this.vehicleCode.set(vehicleCode);
        }

        public void setItemName(String itemName) {
            this.itemName.set(itemName);
        }

        public void setUnitPrice(double unitPrice) {
            this.unitPrice.set(unitPrice);
        }

        public void setQuantity(int quantity) {
            this.quantity.set(quantity);
        }



    }

    @FXML
    private Button bt_Add;

    @FXML
    private Button bt_Delete;

    @FXML
    private Button bt_AddVehicle;

    @FXML
    private Button bt_Edit;

    @FXML
    private Button bt_Search;

    @FXML
    private TableColumn<Table_AddItem, String> col1_ItemCode;

    @FXML
    private TableColumn<Table_AddItem, String> col2_VehicleCode;

    @FXML
    private TableColumn<Table_AddItem, String> col3_ItemName;

    @FXML
    private TableColumn<Table_AddItem,Double> col4_UnitPrice;

    @FXML
    private TableColumn<Table_AddItem,Integer> col5_Quantity;

    @FXML
    private TextField tf_VehicleCode;

    @FXML
    private TextField tf_Quantity;

    @FXML
    private TableView<Table_AddItem> tb_main;

    @FXML
    private TextField tf_Item;

    @FXML
    private TextField tf_ItemName;

    @FXML
    private TextField tf_UnitPrice;
    @FXML
    private ContextMenu ContexMenu_VehicleCode;
    private String jdbcURL = "jdbc:postgresql://localhost:5410/uniquely_auto_parts";
    private String username = "postgres";
    private String password = "Sinari2000";
    private Connection connection = DriverManager.getConnection(jdbcURL, username, password);

    public Add_ItemDatabaseController() throws SQLException {
    }

    private Double unit_price=0.0;
    private int qty =0;
    //get vehicle code use in database
    private ArrayList<String> vehicleCodes = new ArrayList<>();


    public  void initialize (){

        //get  available vehicle code

        try {
            String sql = "SELECT vehicle_code FROM parts.vehicle_type";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String vehicleCode = resultSet.getString("vehicle_code");
                vehicleCodes.add(vehicleCode);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Can't load vehicle code list.");
        }

        // Set up auto-completion to vehicle code text box
        tf_VehicleCode.textProperty().addListener((observable, oldValue, newValue) -> {
            ContexMenu_VehicleCode.getItems().clear();
            if (!newValue.isEmpty()) {
                for (String code : vehicleCodes) {
                    if (code.startsWith(newValue)) {
                        MenuItem suggestionItem = new MenuItem(code);
                        suggestionItem.setOnAction(event -> {
                            tf_VehicleCode.setText(suggestionItem.getText());
                            tf_VehicleCode.positionCaret(suggestionItem.getText().length());
                            ContexMenu_VehicleCode.hide();
                        });
                        ContexMenu_VehicleCode.getItems().add(suggestionItem);
                    }
                }
                if (!ContexMenu_VehicleCode.getItems().isEmpty()) {
                    ContexMenu_VehicleCode.show(tf_VehicleCode, Side.BOTTOM, 0, 0);
                } else {
                    ContexMenu_VehicleCode.hide();
                }
            } else {
                ContexMenu_VehicleCode.hide();
            }
        });

        //fill textFields
        tf_Item.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!tf_Item.getText().isEmpty()) {
                    tf_VehicleCode.requestFocus();
                }
            }

        });
        tf_VehicleCode.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!tf_VehicleCode.getText().isEmpty()) {
                    boolean goNext_textBox =false;
                    for (int i=0;i<=vehicleCodes.size()-1;i++) {
                        if (tf_VehicleCode.getText().trim().equals(vehicleCodes.get(i).trim())) {
                            goNext_textBox=true;
                            break;
                        }
                    }
                    if (goNext_textBox){
                        tf_ItemName.requestFocus();
                    }else {
                        tf_VehicleCode.clear();
                        tf_VehicleCode.requestFocus();
                    }

                }
            }
        });
        tf_ItemName.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!tf_ItemName.getText().isEmpty()) {
                    tf_UnitPrice.requestFocus();
                }
            }
        });
        tf_UnitPrice.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!tf_UnitPrice.getText().isEmpty()) {
                    tf_Quantity.requestFocus();
                }
                try{
                    unit_price = Double.parseDouble(tf_UnitPrice.getText());
                }catch (Exception e){
                    tf_UnitPrice.clear();
                    tf_UnitPrice.requestFocus();
                }
            }
        });
        tf_Quantity.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {

                try{
                    qty = Integer.parseInt(tf_Quantity.getText());
                }catch (Exception e){
                    tf_Quantity.clear();
                    tf_Quantity.requestFocus();
                }
                if (!tf_Quantity.getText().isEmpty()) {
                    Add_to_Database();
                }
            }
        });

        //edit item
        tb_main.setOnMouseClicked(event -> {
            // Get the selected item
            Add_ItemDatabaseController.Table_AddItem selectedItem = tb_main.getSelectionModel().getSelectedItem();

            if (selectedItem != null) {
                // Populate the item's properties into the input fields
                tf_Item.setText(selectedItem.getItemCode());
                tf_VehicleCode.setText(selectedItem.getVehicleCode());
                tf_ItemName.setText(selectedItem.getItemName());
                tf_UnitPrice.setText(Double.toString(selectedItem.getUnitPrice()));
                tf_Quantity.setText(Integer.toString(selectedItem.getQuantity()));

            }
        });

    }
    public void Add_to_Database(){
        if (tf_Item.getText().isEmpty()) {
            tf_Item.requestFocus();
            return;
        }
        if (tf_VehicleCode.getText().isEmpty()) {
            tf_VehicleCode.requestFocus();
            return;

        }else {
            boolean goNext_textBox =false;
            for (int i=0;i<=vehicleCodes.size()-1;i++) {
                if (tf_VehicleCode.getText().trim().equals(vehicleCodes.get(i).trim())) {
                    goNext_textBox=true;
                    break;
                }
            }
            if (!goNext_textBox){
                tf_VehicleCode.clear();
                tf_VehicleCode.requestFocus();
                return;
            }
        }
        if (tf_ItemName.getText().isEmpty()) {
            tf_ItemName.requestFocus();
            return;
        }
        if (!tf_UnitPrice.getText().isEmpty()) {
            try{
                unit_price = Double.parseDouble(tf_UnitPrice.getText());
            }catch (Exception e){
                tf_UnitPrice.clear();
                tf_UnitPrice.requestFocus();
                return;
            }
        }else {
            tf_UnitPrice.clear();
            tf_UnitPrice.requestFocus();
            return;
        }
        if (!tf_Quantity.getText().isEmpty()|| qty==0) {
            try{
                qty = Integer.parseInt(tf_Quantity.getText());
                if (qty==0){
                    tf_Quantity.clear();
                    tf_Quantity.requestFocus();
                    return;
                }
            }catch (Exception e){
                tf_Quantity.clear();
                tf_Quantity.requestFocus();
                return;
            }
        }else {
            tf_Quantity.clear();
            tf_Quantity.requestFocus();
            return;
        }



        //check item code use in database
        ArrayList<String> itemCodes = new ArrayList<>();

        try {
            String sql = "SELECT item_code FROM parts.parts_store";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String itemCde = resultSet.getString("item_code");
                itemCodes.add(itemCde);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Can't load Item code list.");
        }
        for (int i=0;i<=itemCodes.size()-1;i++){

            if (tf_Item.getText().trim().equals(itemCodes.get(i).trim())){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Item Code Check");
                alert.setHeaderText(null);
                alert.setContentText("The Item code " + tf_Item.getText() + " exists in the list.");
                alert.showAndWait();

                return;
            }
        }

        //add item to database


        try {


            // Perform database operations...
            String sql = "INSERT INTO parts.parts_store (item_code,vehicle_code,item,unit_price,qty) VALUES (?,?,?,?,?)";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, tf_Item.getText());
            statement.setString(2, tf_VehicleCode.getText());
            statement.setString(3, tf_ItemName.getText());
            statement.setDouble(4, unit_price);
            statement.setInt(5,qty);


            int rowsAffected = statement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);

            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connection failed! Error: " + e.getMessage());
            return;
        }




        col1_ItemCode.setCellValueFactory(cellData -> cellData.getValue().itemCodeProperty());
        col2_VehicleCode.setCellValueFactory(cellData -> cellData.getValue().vehicleCodeProperty());
        col3_ItemName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
        col4_UnitPrice.setCellValueFactory(cellData -> cellData.getValue().unitPriceProperty().asObject());
        col5_Quantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());

        // Create a DecimalFormat object with the desired format pattern
        DecimalFormat df = new DecimalFormat("#.##");

        // Use the format() method to round the number
        String roundedRoundedNumber = df.format(unit_price);

        Add_ItemDatabaseController.Table_AddItem addItems = new Add_ItemDatabaseController.Table_AddItem(tf_Item.getText(),tf_VehicleCode.getText(),tf_ItemName.getText(),Double.parseDouble(roundedRoundedNumber),qty);
        tb_main.getItems().add(addItems);




        unit_price=0.0;
        qty=0;
        tf_Item.clear();
        tf_VehicleCode.clear();
        tf_ItemName.clear();
        tf_UnitPrice.clear();
        tf_Quantity.clear();
        tf_Item.requestFocus();



    }

    @FXML
    private void editSelectedRow(ActionEvent event) {
        if (tf_Item.getText().isEmpty()) {
            tf_Item.requestFocus();
            return;
        }
        if (tf_VehicleCode.getText().isEmpty()) {
            tf_VehicleCode.requestFocus();
            return;
        }
        if (tf_ItemName.getText().isEmpty()) {
            tf_ItemName.requestFocus();
            return;
        }
        if (tf_UnitPrice.getText().isEmpty()) {
            try{
                unit_price = Double.parseDouble(tf_UnitPrice.getText());
            }catch (Exception e){
                tf_UnitPrice.clear();
                tf_UnitPrice.requestFocus();
                return;
            }
        }
        if (tf_Quantity.getText().isEmpty()) {
            try{
                qty = Integer.parseInt(tf_Quantity.getText());
            }catch (Exception e){
                tf_Quantity.clear();
                tf_Quantity.requestFocus();
                return;
            }
        }
        //Update Database

        try{
            String updateSql = "UPDATE parts.parts_store SET item_code = ?, vehicle_code = ?,item = ?, unit_price = ?,qty = ? WHERE item_code = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateSql);

            updateStatement.setString(1, tf_Item.getText());
            updateStatement.setString(2, tf_VehicleCode.getText());
            updateStatement.setString(3, tf_ItemName.getText());
            updateStatement.setDouble(4, Double.parseDouble(tf_UnitPrice.getText()));
            updateStatement.setInt(5,Integer.parseInt(tf_Quantity.getText()));

            // Set the value for the WHERE clause
            updateStatement.setString(6, tf_Item.getText());


            // Execute the update
            int rowsAffected = updateStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Vehicle information updated successfully.");
            } else {
                System.out.println("Vehicle not found or could not be updated.");
                return;
            }

            // Close the statement and connection
            updateStatement.close();

        }catch (Exception e){
            System.out.println("Database not updated.");
        }



        // Get the selected item
        Add_ItemDatabaseController.Table_AddItem selectedItem = tb_main.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            // Update the item with the modified values
            selectedItem.setItemCode(tf_Item.getText());
            selectedItem.setVehicleCode(tf_VehicleCode.getText());
            selectedItem.setItemName(tf_ItemName.getText());
            selectedItem.setUnitPrice(Double.parseDouble(tf_UnitPrice.getText()));
            selectedItem.setQuantity(Integer.parseInt(tf_Quantity.getText()));

            // Refresh the table to reflect the changes
            tb_main.refresh();
        }






        unit_price=0.0;
        qty=0;
        tf_Item.clear();
        tf_VehicleCode.clear();
        tf_ItemName.clear();
        tf_UnitPrice.clear();
        tf_Quantity.clear();
        tf_Item.requestFocus();
    }

    @FXML
    private void removeSelectedRow(ActionEvent event) {
        // Get the selected item
        Add_ItemDatabaseController.Table_AddItem selectedItem = tb_main.getSelectionModel().getSelectedItem();

        // Check if an item is selected
        if (selectedItem != null) {
            // Remove the selected item from the data source
            tb_main.getItems().remove(selectedItem);
        }

        //remove item from database

        String sql = tf_Item.getText();
        try {
            /*PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "Delete");

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Item deleted successfully.");
            } else {
                System.out.println("Item not found or could not be deleted.");
            }*/

            // Prepare the SQL statement
            String deleteSql = "DELETE FROM parts.parts_store WHERE item_code = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
            deleteStatement.setString(1, tf_Item.getText());

            // Execute the deletion
            int rowsAffected = deleteStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Vehicle deleted successfully.");
            } else {
                System.out.println("Vehicle not found or could not be deleted.");
                return;
            }

            // Close the statement and connection
            deleteStatement.close();


        }catch (Exception e){
            System.out.println("Item not remove from database");
            return;
        }




        unit_price=0.0;
        qty=0;
        tf_Item.clear();
        tf_VehicleCode.clear();
        tf_ItemName.clear();
        tf_UnitPrice.clear();
        tf_Quantity.clear();
        tf_Item.requestFocus();
    }

    public  void  open_AddVehicleWindow(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Add_To_VehicleDataBase.fxml"));
            Parent root = fxmlLoader.load();
            Stage addVehicleWindow = new Stage();
            addVehicleWindow.setScene(new Scene(root));
            addVehicleWindow.setTitle("Add Vehicle");
            addVehicleWindow.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public  void  open_SearchWindow(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Search.fxml"));
            Parent root = fxmlLoader.load();
            Stage addVehicleWindow = new Stage();
            addVehicleWindow.setScene(new Scene(root));
            addVehicleWindow.setTitle("Search");
            addVehicleWindow.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}


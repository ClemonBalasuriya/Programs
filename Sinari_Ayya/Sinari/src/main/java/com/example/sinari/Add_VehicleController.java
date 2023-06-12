package com.example.sinari;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;




import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Add_VehicleController implements Initializable {
    public Add_VehicleController() throws SQLException {
    }

    public class Table_AddVehicle {

        private StringProperty vehicleCode;
        private StringProperty brand;
        private StringProperty model;
        private SimpleIntegerProperty modelYear;
        private StringProperty vehicleType;
        private StringProperty gasolineType;

        public Table_AddVehicle(String vehicleCode, String brand, String model, int modelYear, String gasolineType, String vehicleType) {
            this.vehicleCode = new SimpleStringProperty(vehicleCode);
            this.brand = new SimpleStringProperty(brand);
            this.model = new SimpleStringProperty(model);
            this.modelYear = new SimpleIntegerProperty(modelYear);
            this.vehicleType = new SimpleStringProperty(vehicleType);
            this.gasolineType = new SimpleStringProperty(gasolineType);
        }


        public String getVehicleCode() {
            return vehicleCode.get();
        }

        public StringProperty vehicleCodeProperty() {
            return vehicleCode;
        }

        public void setVehicleCode(String vehicleCode) {
            this.vehicleCode.set(vehicleCode);
        }

        public String getBrand() {
            return brand.get();
        }

        public StringProperty brandProperty() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand.set(brand);
        }

        public String getModel() {
            return model.get();
        }

        public StringProperty modelProperty() {
            return model;
        }

        public void setModel(String model) {
            this.model.set(model);
        }

        public int getModelYear() {
            return modelYear.get();
        }

        public SimpleIntegerProperty modelYearProperty() {
            return modelYear;
        }

        public void setModelYear(int modelYear) {
            this.modelYear.set(modelYear);
        }

        public String getVehicleType() {
            return vehicleType.get();
        }

        public StringProperty vehicleTypeProperty() {
            return vehicleType;
        }

        public void setVehicleType(String vehicleType) {
            this.vehicleType.set(vehicleType);
        }

        public String getGasolineType() {
            return gasolineType.get();
        }

        public StringProperty gasolineTypeProperty() {
            return gasolineType;
        }

        public void setGasolineType(String gasolineType) {
            this.gasolineType.set(gasolineType);
        }

    }


    @FXML
    private Button bt_Add;

    @FXML
    private Button bt_Delete;

    @FXML
    private Button bt_Edit;

    @FXML
    private Button bt_Search;

    @FXML
    private ChoiceBox<String> choiceBox_GasolineType;

    @FXML
    private TableColumn<Table_AddVehicle, String> col1_VehicleCode;

    @FXML
    private TableColumn<Table_AddVehicle, String> col2_Brand;

    @FXML
    private TableColumn<Table_AddVehicle, String> col3_Model;

    @FXML
    private TableColumn<Table_AddVehicle, Integer> col4_ModelYear;

    @FXML
    private TableColumn<Table_AddVehicle, String> col5_VehicleType;

    @FXML
    private TableColumn<Table_AddVehicle, String> col6_GasolineType;

    @FXML
    private TableView<Table_AddVehicle> tb_main;

    @FXML
    private TextField tf_Brand;

    @FXML
    private TextField tf_Model;

    @FXML
    private TextField tf_ModelYear;

    @FXML
    private TextField tf_VehicleCode;

    @FXML
    private TextField tf_VehicleType;

    private String jdbcURL = "jdbc:postgresql://localhost:5410/uniquely_auto_parts";
    private String username = "postgres";
    private String password = "Sinari2000";
    private Connection connection = DriverManager.getConnection(jdbcURL, username, password);



    final private String[] gasolineTypes = {"Petrol","Diesel","Electric","Hybrid"};
    private String gasolineType;
    private int modelYear;
    public void getGasolineType(ActionEvent event){
        gasolineType = choiceBox_GasolineType.getValue();
        tf_VehicleType.requestFocus();
    }

    public void initialize(URL arg0, ResourceBundle arg1){

        choiceBox_GasolineType.getItems().addAll(gasolineTypes);
        choiceBox_GasolineType.setOnAction(this::getGasolineType);

        tf_VehicleCode.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!tf_VehicleCode.getText().isEmpty()) {
                    tf_Brand.requestFocus();
                }
            }

        });
        tf_Brand.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!tf_Brand.getText().isEmpty()) {
                    tf_Model.requestFocus();
                }
            }
        });
        tf_Model.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!tf_Model.getText().isEmpty()) {
                    tf_ModelYear.requestFocus();
                }
            }
        });
        tf_ModelYear.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!tf_ModelYear.getText().isEmpty()) {
                    choiceBox_GasolineType.requestFocus();
                }
                try{
                    modelYear = Integer.parseInt(tf_ModelYear.getText());
                }catch (Exception e){
                    tf_ModelYear.clear();
                    tf_ModelYear.requestFocus();
                }
            }
        });
        tf_VehicleType.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!tf_VehicleType.getText().isEmpty()) {

                    Add_to_Database();

                }
            }
        });

        //edit item
        tb_main.setOnMouseClicked(event -> {
            // Get the selected item
            Table_AddVehicle selectedItem = tb_main.getSelectionModel().getSelectedItem();

            if (selectedItem != null) {
                // Populate the item's properties into the input fields
                tf_VehicleCode.setText(selectedItem.getVehicleCode());
                tf_Brand.setText(selectedItem.getBrand());
                tf_Model.setText(selectedItem.getModel());
                tf_ModelYear.setText(Integer.toString(selectedItem.getModelYear()));
                choiceBox_GasolineType.setValue(selectedItem.getGasolineType());
                tf_VehicleType.setText(selectedItem.getVehicleType());
            }
        });

    }

    public void Add_to_Database(){
        if (tf_VehicleCode.getText().isEmpty()) {
            tf_VehicleCode.requestFocus();
            return;
        }
        if (tf_Brand.getText().isEmpty()) {
            tf_Brand.requestFocus();
            return;
        }
        if (modelYear==0 || tf_ModelYear.getText().isEmpty()) {
            try{
                modelYear = Integer.parseInt(tf_ModelYear.getText());
            }catch (Exception e){
                tf_ModelYear.clear();
                tf_ModelYear.requestFocus();
                return;
            }
        }
        if (tf_Model.getText().isEmpty()) {
            tf_Model.requestFocus();
            return;
        }
        if (gasolineType== null) {
            choiceBox_GasolineType.requestFocus();
            return;
        }
        if (tf_VehicleType.getText().isEmpty()) {
            tf_VehicleType.requestFocus();
            return;
        }


        //check vehicle code use in database
        ArrayList<String> vehicleCodes = new ArrayList<>();

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
        for (int i=0;i<=vehicleCodes.size()-1;i++){

            if (tf_VehicleCode.getText().trim().equals(vehicleCodes.get(i).trim())){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Vehicle Code Check");
                alert.setHeaderText(null);
                alert.setContentText("The vehicle code " + tf_VehicleCode.getText() + " exists in the list.");
                alert.showAndWait();

                return;
            }
        }

        try {


            // Perform database operations...
            String sql = "INSERT INTO parts.vehicle_type (vehicle_code,brand,model,model_year,vehicle_type,gasoline_type) VALUES (?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, tf_VehicleCode.getText());
            statement.setString(2, tf_Brand.getText());
            statement.setString(3, tf_Model.getText());
            statement.setInt(4, modelYear);
            statement.setString(5,tf_VehicleType.getText());
            statement.setString(6, choiceBox_GasolineType.getValue());


            int rowsAffected = statement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);


            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connection failed! Error: " + e.getMessage());
            return;
        }




        col1_VehicleCode.setCellValueFactory(cellData -> cellData.getValue().vehicleCodeProperty());
        col2_Brand.setCellValueFactory(cellData -> cellData.getValue().brandProperty());
        col3_Model.setCellValueFactory(cellData -> cellData.getValue().modelProperty());
        col4_ModelYear.setCellValueFactory(cellData -> cellData.getValue().modelYearProperty().asObject());
        col5_VehicleType.setCellValueFactory(cellData -> cellData.getValue().vehicleTypeProperty());
        col6_GasolineType.setCellValueFactory(cellData -> cellData.getValue().gasolineTypeProperty());

        Table_AddVehicle addItems = new Table_AddVehicle(tf_VehicleCode.getText(),tf_Brand.getText(),tf_Model.getText(),modelYear,choiceBox_GasolineType.getValue(),tf_VehicleType.getText());
        tb_main.getItems().add(addItems);

        //add item to database





        modelYear=0;
        tf_VehicleCode.clear();
        tf_Brand.clear();
        tf_Model.clear();
        tf_ModelYear.clear();
        tf_VehicleType.clear();
        tf_VehicleCode.requestFocus();



    }
    @FXML
    private void editSelectedRow(ActionEvent event) {
        if (tf_VehicleCode.getText().isEmpty()) {
            tf_VehicleCode.requestFocus();
            return;
        }
        if (tf_Brand.getText().isEmpty()) {
            tf_Brand.requestFocus();
            return;
        }
        if (modelYear==0 || tf_ModelYear.getText().isEmpty()) {
            try{
                modelYear = Integer.parseInt(tf_ModelYear.getText());
            }catch (Exception e){
                tf_ModelYear.clear();
                tf_ModelYear.requestFocus();
                return;
            }
        }
        if (tf_Model.getText().isEmpty()) {
            tf_Model.requestFocus();
            return;
        }
        if (gasolineType== null) {
            choiceBox_GasolineType.requestFocus();
            return;
        }
        if (tf_VehicleType.getText().isEmpty()) {
            tf_VehicleType.requestFocus();
            return;
        }

        //Update Database

        try{
            String updateSql = "UPDATE parts.vehicle_type SET vehicle_code = ?, brand = ?,model = ?, model_year = ?,vehicle_type = ?, gasoline_type = ? WHERE vehicle_code = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateSql);
            updateStatement.setString(1, tf_VehicleCode.getText());
            updateStatement.setString(2, tf_Brand.getText());
            updateStatement.setString(3, tf_Model.getText());
            updateStatement.setDouble(4, modelYear);
            updateStatement.setString(5,tf_VehicleType.getText());
            updateStatement.setString(6, choiceBox_GasolineType.getValue());

            // Set the value for the WHERE clause
            updateStatement.setString(7, tf_VehicleCode.getText());


            // Execute the update
            int rowsAffected = updateStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Vehicle information updated successfully.");
            } else {
                System.out.println("Vehicle not found or could not be updated.");
            }

            // Close the statement and connection
            updateStatement.close();

        }catch (Exception e){
            System.out.println("Database not updated.");
            return;
        }



        // Get the selected item
        Table_AddVehicle selectedItem = tb_main.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            // Update the item with the modified values
            selectedItem.setVehicleCode(tf_VehicleCode.getText());
            selectedItem.setBrand(tf_Brand.getText());
            selectedItem.setModel(tf_Model.getText());
            selectedItem.setModelYear(Integer.parseInt(tf_ModelYear.getText()));
            selectedItem.setGasolineType(choiceBox_GasolineType.getValue());
            selectedItem.setVehicleType(tf_VehicleType.getText());

            // Refresh the table to reflect the changes
            tb_main.refresh();
        }






        modelYear=0;
        tf_VehicleCode.clear();
        tf_Brand.clear();
        tf_Model.clear();
        tf_ModelYear.clear();
        tf_VehicleType.clear();
        tf_VehicleCode.requestFocus();
    }

    @FXML
    private void removeSelectedRow(ActionEvent event) {
        // Get the selected item
        Table_AddVehicle selectedItem = tb_main.getSelectionModel().getSelectedItem();

        // Check if an item is selected
        if (selectedItem != null) {
            // Remove the selected item from the data source
            tb_main.getItems().remove(selectedItem);
        }

        //remove item from database

        String sql = tf_VehicleCode.getText();
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
            String deleteSql = "DELETE FROM parts.vehicle_type WHERE vehicle_code = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
            deleteStatement.setString(1, tf_VehicleCode.getText());

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
            System.out.println("Vehicle not remove from database");
            return;
        }




        modelYear=0;
        tf_VehicleCode.clear();
        tf_Brand.clear();
        tf_Model.clear();
        tf_ModelYear.clear();
        tf_VehicleType.clear();
        tf_VehicleCode.requestFocus();
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


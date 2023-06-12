package com.example.sinari;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;

public class SearchController {
    public class Search_Table {

        private StringProperty VehicleCode;
        private StringProperty ItemCode;
        private StringProperty Brand;
        private StringProperty Model;
        private StringProperty ItemName;
        private StringProperty VehicleType;
        private SimpleIntegerProperty ModelYear;
        private SimpleDoubleProperty UnitPrice;
        private StringProperty GasolineType;
        private SimpleIntegerProperty Quantity;
        public Search_Table(){}

        public Search_Table(String VehicleCode,String ItemCode,String Brand,String Model,String ItemName,String VehicleType,int ModelYear,Double UnitPrice,String GasolineType,int Quantity) {
            this.VehicleCode = new SimpleStringProperty(VehicleCode);
            this.ItemCode = new SimpleStringProperty(ItemCode);
            this.Brand = new SimpleStringProperty(Brand);
            this.Model = new SimpleStringProperty(Model);
            this.ItemName = new SimpleStringProperty(ItemName);
            this.VehicleType = new SimpleStringProperty(VehicleType);
            this.ModelYear = new SimpleIntegerProperty(ModelYear);
            this.UnitPrice = new SimpleDoubleProperty(UnitPrice);
            this.GasolineType = new SimpleStringProperty(GasolineType);
            this.Quantity = new SimpleIntegerProperty(Quantity);
        }

        public String getVehicleCode() {
            return VehicleCode.get();
        }

        public StringProperty vehicleCodeProperty() {
            return VehicleCode;
        }

        public void setVehicleCode(String vehicleCode) {
            this.VehicleCode.set(vehicleCode);
        }

        public String getItemCode() {
            return ItemCode.get();
        }

        public StringProperty itemCodeProperty() {
            return ItemCode;
        }

        public void setItemCode(String itemCode) {
            this.ItemCode.set(itemCode);
        }

        public String getBrand() {
            return Brand.get();
        }

        public StringProperty brandProperty() {
            return Brand;
        }

        public void setBrand(String brand) {
            this.Brand.set(brand);
        }

        public String getModel() {
            return Model.get();
        }

        public StringProperty modelProperty() {
            return Model;
        }

        public void setModel(String model) {
            this.Model.set(model);
        }

        public String getItemName() {
            return ItemName.get();
        }

        public StringProperty itemNameProperty() {
            return ItemName;
        }

        public void setItemName(String itemName) {
            this.ItemName.set(itemName);
        }

        public String getVehicleType() {
            return VehicleType.get();
        }

        public StringProperty vehicleTypeProperty() {
            return VehicleType;
        }

        public void setVehicleType(String vehicleType) {
            this.VehicleType.set(vehicleType);
        }

        public int getModelYear() {
            return ModelYear.get();
        }

        public SimpleIntegerProperty modelYearProperty() {
            return ModelYear;
        }

        public void setModelYear(int modelYear) {
            this.ModelYear.set(modelYear);
        }

        public double getUnitPrice() {
            return UnitPrice.get();
        }

        public SimpleDoubleProperty unitPriceProperty() {
            return UnitPrice;
        }

        public void setUnitPrice(double unitPrice) {
            this.UnitPrice.set(unitPrice);
        }

        public String getGasolineType() {
            return GasolineType.get();
        }

        public StringProperty gasolineTypeProperty() {
            return GasolineType;
        }

        public void setGasolineType(String gasolineType) {
            this.GasolineType.set(gasolineType);
        }

        public int getQuantity() {
            return Quantity.get();
        }

        public SimpleIntegerProperty quantityProperty() {
            return Quantity;
        }

        public void setQuantity(int quantity) {
            this.Quantity.set(quantity);
        }
    }

    @FXML
    private ChoiceBox<String> CB_GasolineType;

    @FXML
    private ContextMenu CM_Brand;

    @FXML
    private ContextMenu CM_Item;

    @FXML
    private ContextMenu CM_ItemCode;

    @FXML
    private ContextMenu CM_Model;

    @FXML
    private ContextMenu CM_ModelYear;

    @FXML
    private ContextMenu CM_VehicleCode;

    @FXML
    private ContextMenu CM_VehicleType;

    @FXML
    private TableColumn<Search_Table, Integer> col10_Quantity;

    @FXML
    private TableColumn<Search_Table, String> col1_VehicleCode;

    @FXML
    private TableColumn<Search_Table, String> col2_ItemCode;

    @FXML
    private TableColumn<Search_Table, String> col3_Brand;

    @FXML
    private TableColumn<Search_Table, String> col4_Model;

    @FXML
    private TableColumn<Search_Table, String> col5_ItemName;

    @FXML
    private TableColumn<Search_Table, String> col6_VehicleType;

    @FXML
    private TableColumn<Search_Table, Integer> col7_ModelYear;

    @FXML
    private TableColumn<Search_Table, Double> col8_UnitPrice;

    @FXML
    private TableColumn<Search_Table, String> col9_GasolineType;

    @FXML
    private TableView<Search_Table> tb_main;

    @FXML
    private TextField tf_Brand;

    @FXML
    private TextField tf_Item;

    @FXML
    private TextField tf_ItemCode;

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

    public SearchController() throws SQLException {
    }

    @FXML
    void clearTable(ActionEvent event) {
        load_database();
        tb_main.getItems().clear();
        tf_ItemCode.clear();
        tf_Item.clear();
        tf_VehicleCode.clear();
        tf_Brand.clear();
        tf_Model.clear();
        tf_ModelYear.clear();
        tf_VehicleType.clear();
        CB_GasolineType.setValue(null);
        refreshGasolineList();



    }
    char notAdd ='Y';

    @FXML
    void refreshArrayLists() throws SQLException {
        if (!tf_ItemCode.getText().isEmpty()){
            findItemCode();
            notAdd ='N';
            return;
        }
        if (!tf_Item.getText().isEmpty()){
            findItemName();
        }
        if (!tf_VehicleCode.getText().isEmpty()){
            findVehicleCode();
        }
        if (!tf_Brand.getText().isEmpty()){
            findBrand();
        }
        if (!tf_Model.getText().isEmpty()){
            findModel();
        }
        if (!tf_ModelYear.getText().isEmpty()){
            findModelYear();
        }
        if (CB_GasolineType.getValue() != null){
            findGasolineType();
        }
        if (!tf_VehicleType.getText().isEmpty()){
            findVehicleType();
        }


    }
    private ArrayList<String> itemCodeList = new ArrayList<>();
    private ArrayList<String> vehicleCodeList = new ArrayList<>();
    public void findItemCode() throws SQLException {
        for (String itemCode:itemCodeList){
            if (itemCode.trim().equals(tf_ItemCode.getText().trim())){
                String selectQuery = "SELECT * FROM parts.parts_store WHERE item_code = ?";
                PreparedStatement statement = connection.prepareStatement(selectQuery);
                statement.setString(1, itemCode);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String item_Code = resultSet.getString("item_Code");
                    String vehicle_code = resultSet.getString("vehicle_code");
                    String item = resultSet.getString("item");
                    Double unit_price = resultSet.getDouble("unit_price");
                    int qty = resultSet.getInt("qty");
                    // Retrieve other details as needed

                    // Do something with the retrieved details
                    String selectQuery2 = "SELECT * FROM parts.vehicle_type WHERE vehicle_code = ?";
                    PreparedStatement statement2 = connection.prepareStatement(selectQuery2);
                    statement2.setString(1, vehicle_code);

                    ResultSet resultSet2 = statement2.executeQuery();

                    String brand = null;
                    String model = null;
                    int model_year = 0;
                    String vehicle_type = null;
                    String gasoline_type = null;


                    if (resultSet2.next()) {
                        brand = resultSet2.getString("brand");
                        model = resultSet2.getString("model");
                        model_year = resultSet2.getInt("model_year");
                        vehicle_type = resultSet2.getString("vehicle_type");
                        gasoline_type = resultSet2.getString("gasoline_type");
                    }
                    col1_VehicleCode.setCellValueFactory(cellData -> cellData.getValue().vehicleCodeProperty());
                    col2_ItemCode.setCellValueFactory(cellData -> cellData.getValue().itemCodeProperty());
                    col3_Brand.setCellValueFactory(cellData -> cellData.getValue().brandProperty());
                    col4_Model.setCellValueFactory(cellData -> cellData.getValue().modelProperty());
                    col5_ItemName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
                    col6_VehicleType.setCellValueFactory(cellData -> cellData.getValue().vehicleTypeProperty());
                    col7_ModelYear.setCellValueFactory(cellData -> cellData.getValue().modelYearProperty().asObject());
                    col8_UnitPrice.setCellValueFactory(cellData -> cellData.getValue().unitPriceProperty().asObject());
                    col9_GasolineType.setCellValueFactory(cellData -> cellData.getValue().gasolineTypeProperty());
                    col10_Quantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
                    // Do something with the retrieved values
                    SearchController.Search_Table addItems = new SearchController.Search_Table(vehicle_code,item_Code,brand,model,item,vehicle_type,model_year,unit_price,gasoline_type,qty);
                    tb_main.getItems().add(addItems);
                    return;

                } else {
                    // Item with the given item code not found
                    System.out.println("Item not found");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Item Not Found");
                    alert.setHeaderText(null);
                    alert.setContentText("The item with the given item code was not found.");
                    alert.showAndWait();
                    return;
                }

            }
        }
    }

    public void findItemName() throws SQLException{

        String selectQuery = "SELECT * FROM parts.parts_store WHERE item LIKE ?";
        PreparedStatement statement = connection.prepareStatement(selectQuery);
        statement.setString(1, "%" + tf_Item.getText() + "%"); // Use "%" to search for similar item names

        ResultSet resultSet = statement.executeQuery();

        ArrayList<String> temp_itemCodeList = new ArrayList<>();

        while (resultSet.next()) {
            String item_Code = resultSet.getString("item_code");

            temp_itemCodeList.add(item_Code);
        }
        ArrayList<String> secondTempList = new ArrayList<>();
        for (String vehicleCodeList : itemCodeList){
            for (String i: temp_itemCodeList){
                if (vehicleCodeList.trim().equals(i.trim())){
                    secondTempList.add(i);
                }
            }
        }
        itemCodeList=secondTempList;
        if (!itemCodeList.isEmpty()){
            ArrayList<String> temp_vehicleCodeList = new ArrayList<>();
            for (String item: itemCodeList){
                String selectQuery2 = "SELECT * FROM parts.parts_store WHERE item_code LIKE ?";
                PreparedStatement statement2 = connection.prepareStatement(selectQuery2);
                statement2.setString(1, "%" + item + "%"); // Use "%" to search for similar item names

                ResultSet resultSet2 = statement2.executeQuery();



                while (resultSet2.next()) {
                    String item_Code = resultSet2.getString("vehicle_code");

                    temp_vehicleCodeList.add(item_Code);
                }

            }
            ArrayList<String> secondTempList2 = new ArrayList<>();
            for (String vehicleCodeList : vehicleCodeList){
                for (String i: temp_vehicleCodeList){
                    if (vehicleCodeList.trim().equals(i.trim())){
                        secondTempList2.add(i);
                    }
                }
            }
            vehicleCodeList=secondTempList2;

            // Convert ArrayList to HashSet to remove duplicates
            HashSet<String> set = new HashSet<>(vehicleCodeList);

            // Convert HashSet back to ArrayList
            vehicleCodeList = new ArrayList<>(set);


        }



       /* String selectQuery = "SELECT * FROM parts.parts_store WHERE item LIKE ?";
        PreparedStatement statement = connection.prepareStatement(selectQuery);
        statement.setString(1, "%" + tf_Item.getText() + "%"); // Use "%" to search for similar item names

        ResultSet resultSet = statement.executeQuery();

        ArrayList<String> temp_itemCodeList = new ArrayList<>();

        while (resultSet.next()) {
            String item_Code = resultSet.getString("item_Code");

            temp_itemCodeList.add(item_Code);
        }
        ArrayList<String> temp = new ArrayList<>();
        for (String i: itemCodeList){
            for (String j: temp_itemCodeList){
                if (i.trim().equals(j.trim())) {
                    temp.add(j);

                }
            }
        }
        itemCodeList = temp;

        if (!itemCodeList.isEmpty()) {


            String selectQuery2 = "SELECT DISTINCT vehicle_code FROM parts.parts_store WHERE item_code IN (";
            for (int i = 0; i < itemCodeList.size(); i++) {
                selectQuery2 += "?,";
            }
            selectQuery2 = selectQuery2.substring(0, selectQuery2.length() - 1); // Remove the trailing comma
            selectQuery2 += ")";

            PreparedStatement statement2 = connection.prepareStatement(selectQuery2);

            // Set the item codes in the query
            for (int i = 0; i < itemCodeList.size(); i++) {
                statement2.setString(i + 1, itemCodeList.get(i));
            }

            ResultSet resultSet2 = statement2.executeQuery();

            ArrayList<String> temp_vehicleCodeList = new ArrayList<>();

            while (resultSet2.next()) {
                String vehicleCode = resultSet2.getString("vehicle_code");
                temp_vehicleCodeList.add(vehicleCode);
            }
            ArrayList<String> temp2 = new ArrayList<>();
            for (String i: vehicleCodeList){
                for (String j: temp_vehicleCodeList){
                    if (i.trim().equals(j.trim())) {
                        temp.add(j);

                    }
                }
            }
            vehicleCodeList = temp2;


        }*/

    }

    public void findVehicleCode() throws SQLException{
        ArrayList<String> tempVehicleCodeList = new ArrayList<>();
        ArrayList<String> tempItemCodeList = new ArrayList<>();

        for (String vehicle : vehicleCodeList) {
            if (vehicle.trim().equals(tf_VehicleCode.getText().trim())) {
                tempVehicleCodeList.add(tf_VehicleCode.getText().trim());

                String selectQuery = "SELECT * FROM parts.parts_store WHERE vehicle_code LIKE ?";
                PreparedStatement statement = connection.prepareStatement(selectQuery);
                statement.setString(1, "%" + tf_VehicleCode.getText() + "%");

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    String itemCode = resultSet.getString("item_Code");
                    tempItemCodeList.add(itemCode);
                }
            }
        }

        vehicleCodeList = tempVehicleCodeList;
        itemCodeList = tempItemCodeList;
    }

    public void findBrand() throws SQLException{
        String selectQuery = "SELECT * FROM parts.vehicle_type WHERE brand LIKE ?";
        PreparedStatement statement = connection.prepareStatement(selectQuery);
        statement.setString(1, "%" + tf_Brand.getText() + "%"); // Use "%" to search for similar item names

        ResultSet resultSet = statement.executeQuery();

        ArrayList<String> temp_vehicleCodeList = new ArrayList<>();

        while (resultSet.next()) {
            String item_Code = resultSet.getString("vehicle_code");

            temp_vehicleCodeList.add(item_Code);
        }
        ArrayList<String> secondTempList = new ArrayList<>();
        for (String vehicleCodeList : vehicleCodeList){
            for (String i: temp_vehicleCodeList){
                if (vehicleCodeList.trim().equals(i.trim())){
                    secondTempList.add(i);
                }
            }
        }
        vehicleCodeList=secondTempList;


        if (!vehicleCodeList.isEmpty()) {
            String selectQuery2 = "SELECT DISTINCT item_code FROM parts.parts_store WHERE vehicle_code IN (";
            for (int i = 0; i < vehicleCodeList.size(); i++) {
                selectQuery2 += "?,";
            }
            selectQuery2 = selectQuery2.substring(0, selectQuery2.length() - 1); // Remove the trailing comma
            selectQuery2 += ")";

            PreparedStatement statement2 = connection.prepareStatement(selectQuery2);

            // Set the vehicle codes in the query
            for (int i = 0; i < vehicleCodeList.size(); i++) {
                statement2.setString(i + 1, vehicleCodeList.get(i));
            }

            ResultSet resultSet2 = statement2.executeQuery();

            ArrayList<String> temp_ItemCodeList = new ArrayList<>();

            while (resultSet2.next()) {
                String itemCode = resultSet2.getString("item_code");
                temp_ItemCodeList.add(itemCode);
            }

            ArrayList<String> temp = new ArrayList<>();
            for (String itemCode : itemCodeList) {
                if (temp_ItemCodeList.contains(itemCode.trim())) {
                    temp.add(itemCode);
                }
            }
            itemCodeList = temp;
        }



    }
    public void findModel() throws SQLException{
        String selectQuery = "SELECT * FROM parts.vehicle_type WHERE model LIKE ?";
        PreparedStatement statement = connection.prepareStatement(selectQuery);
        statement.setString(1, "%" + tf_Model.getText() + "%"); // Use "%" to search for similar item names

        ResultSet resultSet = statement.executeQuery();

        ArrayList<String> temp_VehicleCodeList = new ArrayList<>();

        while (resultSet.next()) {
            String item_Code = resultSet.getString("vehicle_code");

            temp_VehicleCodeList.add(item_Code);
        }
        ArrayList<String> secondTempList = new ArrayList<>();
        for (String vehicleCodeList : vehicleCodeList){
            for (String i: temp_VehicleCodeList){
                if (vehicleCodeList.trim().equals(i.trim())){
                    secondTempList.add(i);
                }
            }
        }
        vehicleCodeList=secondTempList;

        if (!vehicleCodeList.isEmpty()) {
            StringBuilder selectQuery2 = new StringBuilder("SELECT DISTINCT item_code FROM parts.parts_store WHERE vehicle_code IN (");
            for (int i = 0; i < vehicleCodeList.size(); i++) {
                selectQuery2.append("?,");
            }
            selectQuery2.deleteCharAt(selectQuery2.length() - 1); // Remove the trailing comma
            selectQuery2.append(")");

            PreparedStatement statement2 = connection.prepareStatement(selectQuery2.toString());

            // Set the vehicle codes in the query
            for (int i = 0; i < vehicleCodeList.size(); i++) {
                statement2.setString(i + 1, vehicleCodeList.get(i));
            }

            ResultSet resultSet2 = statement2.executeQuery();

            ArrayList<String> temp_ItemCodeList = new ArrayList<>();

            while (resultSet2.next()) {
                String itemCode = resultSet2.getString("item_code");
                temp_ItemCodeList.add(itemCode);
            }

            ArrayList<String> temp = new ArrayList<>();

            for (String i : itemCodeList) {
                for (String j : temp_ItemCodeList) {
                    if (i.trim().equals(j.trim())) {
                        temp.add(j);
                    }
                }
            }

            itemCodeList = temp;
        }




    }
    public void findModelYear() throws SQLException {
        String selectQuery = "SELECT * FROM parts.vehicle_type WHERE model_year::text LIKE ?";
        PreparedStatement statement = connection.prepareStatement(selectQuery);
        statement.setString(1, "%" + tf_ModelYear.getText() + "%"); // Use "%" to search for similar model years

        ResultSet resultSet = statement.executeQuery();

        ArrayList<String> temp_VehicleCodeList = new ArrayList<>();

        while (resultSet.next()) {
            String item_Code = resultSet.getString("vehicle_code");
            temp_VehicleCodeList.add(item_Code);
        }

        ArrayList<String> secondTempList = new ArrayList<>();
        for (String vehicleCodeList : vehicleCodeList) {
            for (String i : temp_VehicleCodeList) {
                if (vehicleCodeList.trim().equals(i.trim())) {
                    secondTempList.add(i);
                }
            }
        }
        vehicleCodeList = secondTempList;

        if (!vehicleCodeList.isEmpty()) {
            String selectQuery2 = "SELECT DISTINCT item_code FROM parts.parts_store WHERE vehicle_code IN (";
            for (int i = 0; i < vehicleCodeList.size(); i++) {
                selectQuery2 += "?,";
            }
            selectQuery2 = selectQuery2.substring(0, selectQuery2.length() - 1); // Remove the trailing comma
            selectQuery2 += ")";

            PreparedStatement statement2 = connection.prepareStatement(selectQuery2);

            // Set the vehicle codes in the query
            for (int i = 0; i < vehicleCodeList.size(); i++) {
                statement2.setString(i + 1, vehicleCodeList.get(i));
            }

            ResultSet resultSet2 = statement2.executeQuery();

            ArrayList<String> temp_ItemCodeList = new ArrayList<>();

            while (resultSet2.next()) {
                String itemCode = resultSet2.getString("item_code");
                temp_ItemCodeList.add(itemCode);
            }

            ArrayList<String> temp = new ArrayList<>();
            for (String i : itemCodeList) {
                for (String j : temp_ItemCodeList) {
                    if (i.trim().equals(j.trim())) {
                        temp.add(j);
                    }
                }
            }
            itemCodeList = temp;
        }

    }
    public void findGasolineType() throws SQLException {
        String selectQuery = "SELECT * FROM parts.vehicle_type WHERE gasoline_type::text LIKE ?";
        PreparedStatement statement = connection.prepareStatement(selectQuery);
        statement.setString(1,  CB_GasolineType.getValue()); // Use "%" to search for similar model years

        ResultSet resultSet = statement.executeQuery();

        ArrayList<String> temp_VehicleCodeList = new ArrayList<>();

        while (resultSet.next()) {
            String item_Code = resultSet.getString("vehicle_code");
            temp_VehicleCodeList.add(item_Code);
        }

        ArrayList<String> secondTempList = new ArrayList<>();
        for (String vehicleCodeList : vehicleCodeList) {
            for (String i : temp_VehicleCodeList) {
                if (vehicleCodeList.trim().equals(i.trim())) {
                    secondTempList.add(i);
                }
            }
        }
        vehicleCodeList = secondTempList;

        if (!vehicleCodeList.isEmpty()) {
            String selectQuery2 = "SELECT DISTINCT item_code FROM parts.parts_store WHERE vehicle_code IN (";
            for (int i = 0; i < vehicleCodeList.size(); i++) {
                selectQuery2 += "?,";
            }
            selectQuery2 = selectQuery2.substring(0, selectQuery2.length() - 1); // Remove the trailing comma
            selectQuery2 += ")";

            PreparedStatement statement2 = connection.prepareStatement(selectQuery2);

            // Set the vehicle codes in the query
            for (int i = 0; i < vehicleCodeList.size(); i++) {
                statement2.setString(i + 1, vehicleCodeList.get(i));
            }

            ResultSet resultSet2 = statement2.executeQuery();

            ArrayList<String> temp_ItemCodeList = new ArrayList<>();

            while (resultSet2.next()) {
                String itemCode = resultSet2.getString("item_code");
                temp_ItemCodeList.add(itemCode);
            }

            ArrayList<String> temp = new ArrayList<>();
            for (String i : itemCodeList) {
                for (String j : temp_ItemCodeList) {
                    if (i.trim().equals(j.trim())) {
                        temp.add(j);
                    }
                }
            }
            itemCodeList = temp;
        }

    }
    public void findVehicleType() throws SQLException {
        String selectQuery = "SELECT * FROM parts.vehicle_type WHERE vehicle_type::text LIKE ?";
        PreparedStatement statement = connection.prepareStatement(selectQuery);
        statement.setString(1, "%" + tf_VehicleType.getText() + "%"); // Use "%" to search for similar model years

        ResultSet resultSet = statement.executeQuery();

        ArrayList<String> temp_VehicleCodeList = new ArrayList<>();

        while (resultSet.next()) {
            String item_Code = resultSet.getString("vehicle_code");
            temp_VehicleCodeList.add(item_Code);
        }

        ArrayList<String> secondTempList = new ArrayList<>();
        for (String vehicleCodeList : vehicleCodeList) {
            for (String i : temp_VehicleCodeList) {
                if (vehicleCodeList.trim().equals(i.trim())) {
                    secondTempList.add(i);
                }
            }
        }
        vehicleCodeList = secondTempList;

        if (!vehicleCodeList.isEmpty()) {
            String selectQuery2 = "SELECT DISTINCT item_code FROM parts.parts_store WHERE vehicle_code IN (";
            for (int i = 0; i < vehicleCodeList.size(); i++) {
                selectQuery2 += "?,";
            }
            selectQuery2 = selectQuery2.substring(0, selectQuery2.length() - 1); // Remove the trailing comma
            selectQuery2 += ")";

            PreparedStatement statement2 = connection.prepareStatement(selectQuery2);

            // Set the vehicle codes in the query
            for (int i = 0; i < vehicleCodeList.size(); i++) {
                statement2.setString(i + 1, vehicleCodeList.get(i));
            }

            ResultSet resultSet2 = statement2.executeQuery();

            ArrayList<String> temp_ItemCodeList = new ArrayList<>();

            while (resultSet2.next()) {
                String itemCode = resultSet2.getString("item_code");
                temp_ItemCodeList.add(itemCode);
            }

            ArrayList<String> temp = new ArrayList<>();
            for (String i : itemCodeList) {
                for (String j : temp_ItemCodeList) {
                    if (i.trim().equals(j.trim())) {
                        temp.add(j);
                    }
                }
            }
            itemCodeList = temp;
        }

    }





    public void load_database(){
        // Prepare the SQL query to retrieve item codes
        String selectQuery = "SELECT item_code FROM parts.parts_store";

        try {
            // Create a statement
            Statement statement = connection.createStatement();

            // Execute the query
            ResultSet resultSet = statement.executeQuery(selectQuery);

            // Iterate over the result set and add item codes to the list
            while (resultSet.next()) {
                String itemCode = resultSet.getString("item_code");
                itemCodeList.add(itemCode);
            }

            // Close the statement and result set
            statement.close();
            resultSet.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        // Prepare the SQL query to retrieve item codes
        String selectQuery2 = "SELECT vehicle_code FROM parts.vehicle_type";

        try {
            // Create a statement
            Statement statement = connection.createStatement();

            // Execute the query
            ResultSet resultSet = statement.executeQuery(selectQuery2);

            // Iterate over the result set and add item codes to the list
            while (resultSet.next()) {
                String itemCode = resultSet.getString("vehicle_code");
                vehicleCodeList.add(itemCode);
            }

            // Close the statement and result set
            statement.close();
            resultSet.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    private ArrayList<String> itemNameList = new ArrayList<>();
    private ArrayList<String> brandList = new ArrayList<>();
    private ArrayList<String> modelList = new ArrayList<>();
    private ArrayList<String> modelYearList = new ArrayList<>();
    private ArrayList<String> gasolineTypeList = new ArrayList<>();
    private ArrayList<String> vehicleTypeList = new ArrayList<>();

    public void getGasolineType(ActionEvent event){
        String gasolineTypes = CB_GasolineType.getValue();
        tf_VehicleType.requestFocus();
    }
    public  void initialize (){

        load_database();
        refreshItemNameList();
        refreshBrandList();
        refreshModelList();
        refreshModeYearList();
        refreshGasolineList();
        refreshVehicleTypeList();

        CB_GasolineType.getItems().addAll(gasolineTypeList);
        CB_GasolineType.setOnAction(this::getGasolineType);




        tf_ItemCode.textProperty().addListener((observable, oldValue, newValue) -> {
            CM_ItemCode.getItems().clear();
            if (!newValue.isEmpty()) {
                for (String code : itemCodeList) {
                    if (code.startsWith(newValue)) {
                        MenuItem suggestionItem = new MenuItem(code);
                        suggestionItem.setOnAction(event -> {
                            tf_ItemCode.setText(suggestionItem.getText());
                            tf_ItemCode.positionCaret(suggestionItem.getText().length());
                            CM_ItemCode.hide();
                        });
                        CM_ItemCode.getItems().add(suggestionItem);
                    }
                }
                if (!CM_ItemCode.getItems().isEmpty()) {
                    CM_ItemCode.show(tf_ItemCode, Side.BOTTOM, 0, 0);
                } else {
                    CM_ItemCode.hide();
                }
            } else {
                CM_ItemCode.hide();
            }
        });
        tf_ItemCode.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!tf_ItemCode.getText().isEmpty()) {
                    try {
                        findItemCode();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        });

        tf_Item.setOnMouseClicked(e -> {
            try {
                refreshArrayLists();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            refreshItemNameList();
            refreshGasolineList();
            CB_GasolineType.getItems().clear();
            CB_GasolineType.getItems().addAll(gasolineTypeList);

        });
        tf_Item.textProperty().addListener((observable, oldValue, newValue) -> {
            CM_Item.getItems().clear();
            if (!newValue.isEmpty()) {
                for (String code : itemNameList) {
                    if (code.startsWith(newValue)) {
                        MenuItem suggestionItem = new MenuItem(code);
                        suggestionItem.setOnAction(event -> {
                            tf_Item.setText(suggestionItem.getText());
                            tf_Item.positionCaret(suggestionItem.getText().length());
                            CM_Item.hide();
                        });
                        CM_Item.getItems().add(suggestionItem);
                    }
                }
                if (!CM_Item.getItems().isEmpty()) {
                    CM_Item.show(tf_Item, Side.BOTTOM, 0, 0);
                } else {
                    CM_Item.hide();
                }
            } else {
                CM_Item.hide();
            }
        });

        tf_VehicleCode.setOnMouseClicked(e -> {
            try {
                refreshArrayLists();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            refreshGasolineList();
            CB_GasolineType.getItems().clear();
            CB_GasolineType.getItems().addAll(gasolineTypeList);
        });
        tf_VehicleCode.textProperty().addListener((observable, oldValue, newValue) -> {
            CM_VehicleCode.getItems().clear();
            if (!newValue.isEmpty()) {
                for (String code : vehicleCodeList) {
                    if (code.startsWith(newValue)) {
                        MenuItem suggestionItem = new MenuItem(code);
                        suggestionItem.setOnAction(event -> {
                            tf_VehicleCode.setText(suggestionItem.getText());
                            tf_VehicleCode.positionCaret(suggestionItem.getText().length());
                            CM_VehicleCode.hide();
                        });
                        CM_VehicleCode.getItems().add(suggestionItem);
                    }
                }
                if (!CM_VehicleCode.getItems().isEmpty()) {
                    CM_VehicleCode.show(tf_VehicleCode, Side.BOTTOM, 0, 0);
                } else {
                    CM_VehicleCode.hide();
                }
            } else {
                CM_VehicleCode.hide();
            }
        });

        tf_Brand.setOnMouseClicked(e -> {
            try {
                refreshArrayLists();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            refreshBrandList();
            refreshGasolineList();
            CB_GasolineType.getItems().clear();
            CB_GasolineType.getItems().addAll(gasolineTypeList);

        });
        tf_Brand.textProperty().addListener((observable, oldValue, newValue) -> {
            CM_Brand.getItems().clear();
            if (!newValue.isEmpty()) {
                for (String code : brandList) {
                    if (code==null) continue;
                    if (code.startsWith(newValue)) {
                        MenuItem suggestionItem = new MenuItem(code);
                        suggestionItem.setOnAction(event -> {
                            tf_Brand.setText(suggestionItem.getText());
                            tf_Brand.positionCaret(suggestionItem.getText().length());
                            CM_Brand.hide();
                        });
                        CM_Brand.getItems().add(suggestionItem);
                    }
                }
                if (!CM_Brand.getItems().isEmpty()) {
                    CM_Brand.show(tf_Brand, Side.BOTTOM, 0, 0);
                } else {
                    CM_Brand.hide();
                }
            } else {
                CM_Brand.hide();
            }
        });

        tf_Model.setOnMouseClicked(e -> {
            try {
                refreshArrayLists();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            refreshModelList();
            refreshGasolineList();
            CB_GasolineType.getItems().clear();
            CB_GasolineType.getItems().addAll(gasolineTypeList);

        });
        tf_Model.textProperty().addListener((observable, oldValue, newValue) -> {
            CM_Model.getItems().clear();
            if (!newValue.isEmpty()) {
                for (String code : modelList) {
                    if (code==null) continue;
                    if (code.startsWith(newValue)) {
                        MenuItem suggestionItem = new MenuItem(code);
                        suggestionItem.setOnAction(event -> {
                            tf_Model.setText(suggestionItem.getText());
                            tf_Model.positionCaret(suggestionItem.getText().length());
                            CM_Model.hide();
                        });
                        CM_Model.getItems().add(suggestionItem);
                    }
                }
                if (!CM_Model.getItems().isEmpty()) {
                    CM_Model.show(tf_Model, Side.BOTTOM, 0, 0);
                } else {
                    CM_Model.hide();
                }
            } else {
                CM_Model.hide();
            }
        });

        tf_ModelYear.setOnMouseClicked(e -> {
            try {
                refreshArrayLists();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            refreshModeYearList();
            refreshGasolineList();
            CB_GasolineType.getItems().clear();
            CB_GasolineType.getItems().addAll(gasolineTypeList);

        });
        tf_ModelYear.textProperty().addListener((observable, oldValue, newValue) -> {
            CM_ModelYear.getItems().clear();
            if (!newValue.isEmpty()) {
                for (String code : modelYearList) {
                    if (code==null) continue;
                    if (code.startsWith(newValue)) {
                        MenuItem suggestionItem = new MenuItem(code);
                        suggestionItem.setOnAction(event -> {
                            tf_ModelYear.setText(suggestionItem.getText());
                            tf_ModelYear.positionCaret(suggestionItem.getText().length());
                            CM_ModelYear.hide();
                        });
                        CM_ModelYear.getItems().add(suggestionItem);
                    }
                }
                if (!CM_ModelYear.getItems().isEmpty()) {
                    CM_ModelYear.show(tf_ModelYear, Side.BOTTOM, 0, 0);
                } else {
                    CM_ModelYear.hide();
                }
            } else {
                CM_ModelYear.hide();
            }
        });

       /* CB_GasolineType.setOnAction(e -> {
            try {
                refreshArrayLists();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            refreshGasolineList();
            CB_GasolineType.getItems().clear();
            CB_GasolineType.getItems().addAll(gasolineTypeList);


        });*/

        tf_VehicleType.setOnMouseClicked(e -> {
            try {
                refreshArrayLists();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            refreshVehicleTypeList();
            refreshGasolineList();
            CB_GasolineType.getItems().clear();
            CB_GasolineType.getItems().addAll(gasolineTypeList);

        });
        tf_VehicleType.textProperty().addListener((observable, oldValue, newValue) -> {
            CM_VehicleType.getItems().clear();
            if (!newValue.isEmpty()) {
                for (String code : vehicleTypeList) {
                    if (code==null) continue;
                    if (code.startsWith(newValue)) {
                        MenuItem suggestionItem = new MenuItem(code);
                        suggestionItem.setOnAction(event -> {
                            tf_VehicleType.setText(suggestionItem.getText());
                            tf_VehicleType.positionCaret(suggestionItem.getText().length());
                            CM_VehicleType.hide();
                        });
                        CM_VehicleType.getItems().add(suggestionItem);
                    }
                }
                if (!CM_VehicleType.getItems().isEmpty()) {
                    CM_VehicleType.show(tf_VehicleType, Side.BOTTOM, 0, 0);
                } else {
                    CM_VehicleType.hide();
                }
            } else {
                CM_VehicleType.hide();
            }
        });
    }
    public void refreshItemNameList(){
        itemNameList.clear();
        // Prepare the select statement
        String selectQuery = "SELECT  item FROM parts.parts_store WHERE item_code = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(selectQuery);
            // Iterate over the item codes and retrieve the quantities
            for (String itemCode : itemCodeList) {
                // Set the item code in the query
                statement.setString(1, itemCode);

                // Execute the query and retrieve the result set
                ResultSet resultSet = statement.executeQuery();

                // Retrieve the quantity from the result set
                String temp = null;
                if (resultSet.next()) {
                    temp = resultSet.getString("item");
                }

                // Add the quantity to the quantityList
                itemNameList.add(temp);
            }
            // Convert ArrayList to HashSet to remove duplicates
            HashSet<String> set = new HashSet<>(itemNameList);

            // Convert HashSet back to ArrayList
            itemNameList = new ArrayList<>(set);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    public void refreshBrandList(){
        brandList.clear();
        // Prepare the select statement
        String selectQuery = "SELECT  brand FROM parts.vehicle_type WHERE vehicle_code = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(selectQuery);
            // Iterate over the item codes and retrieve the quantities
            for (String itemCode : vehicleCodeList) {
                // Set the item code in the query
                statement.setString(1, itemCode);

                // Execute the query and retrieve the result set
                ResultSet resultSet = statement.executeQuery();

                // Retrieve the quantity from the result set
                String temp = null;
                if (resultSet.next()) {
                    temp = resultSet.getString("brand");
                }

                // Add the quantity to the quantityList
                brandList.add(temp);
            }
            // Convert ArrayList to HashSet to remove duplicates
            HashSet<String> set = new HashSet<>(brandList);

            // Convert HashSet back to ArrayList
            brandList = new ArrayList<>(set);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    public void refreshModelList(){
        modelList.clear();
        // Prepare the select statement
        String selectQuery = "SELECT  model FROM parts.vehicle_type WHERE vehicle_code = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(selectQuery);
            // Iterate over the item codes and retrieve the quantities
            for (String itemCode : vehicleCodeList) {
                // Set the item code in the query
                statement.setString(1, itemCode);

                // Execute the query and retrieve the result set
                ResultSet resultSet = statement.executeQuery();

                // Retrieve the quantity from the result set
                String temp = null;
                if (resultSet.next()) {
                    temp = resultSet.getString("model");
                }

                // Add the quantity to the quantityList
                modelList.add(temp);


            }
            // Convert ArrayList to HashSet to remove duplicates
            HashSet<String> set = new HashSet<>(modelList);

            // Convert HashSet back to ArrayList
            modelList = new ArrayList<>(set);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    public void refreshModeYearList(){
        modelYearList.clear();
        // Prepare the select statement
        String selectQuery = "SELECT  model_year FROM parts.vehicle_type WHERE vehicle_code = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(selectQuery);
            // Iterate over the item codes and retrieve the quantities
            for (String itemCode : vehicleCodeList) {
                // Set the item code in the query
                statement.setString(1, itemCode);

                // Execute the query and retrieve the result set
                ResultSet resultSet = statement.executeQuery();

                // Retrieve the quantity from the result set
                String temp = null;
                if (resultSet.next()) {
                    temp = resultSet.getString("model_year");
                }

                // Add the quantity to the quantityList
                modelYearList.add(temp);
            }
            // Convert ArrayList to HashSet to remove duplicates
            HashSet<String> set = new HashSet<>(modelYearList);

            // Convert HashSet back to ArrayList
            modelYearList = new ArrayList<>(set);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    public void refreshGasolineList() {
        gasolineTypeList.clear();
        // Prepare the select statement
        String selectQuery = "SELECT  gasoline_type FROM parts.vehicle_type WHERE vehicle_code = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(selectQuery);
            // Iterate over the item codes and retrieve the quantities
            for (String vehicleCode : vehicleCodeList) {
                // Set the item code in the query
                statement.setString(1, vehicleCode);

                // Execute the query and retrieve the result set
                resultSet = statement.executeQuery();

                // Retrieve the quantity from the result set
                String temp = null;
                if (resultSet.next()) {
                    temp = resultSet.getString("gasoline_type");
                }

                // Add the quantity to the quantityList
                gasolineTypeList.add(temp);

                // Close the ResultSet
                resultSet.close();
            }
            // Convert ArrayList to HashSet to remove duplicates
            HashSet<String> set = new HashSet<>(gasolineTypeList);

            // Convert HashSet back to ArrayList
            gasolineTypeList = new ArrayList<>(set);

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void refreshVehicleTypeList(){
        vehicleTypeList.clear();
        // Prepare the select statement
        String selectQuery = "SELECT  vehicle_type FROM parts.vehicle_type WHERE vehicle_code = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(selectQuery);
            // Iterate over the item codes and retrieve the quantities
            for (String itemCode : vehicleCodeList) {
                // Set the item code in the query
                statement.setString(1, itemCode);

                // Execute the query and retrieve the result set
                ResultSet resultSet = statement.executeQuery();

                // Retrieve the quantity from the result set
                String temp = null;
                if (resultSet.next()) {
                    temp = resultSet.getString("vehicle_type");
                }

                // Add the quantity to the quantityList
                vehicleTypeList.add(temp);
            }
            // Convert ArrayList to HashSet to remove duplicates
            HashSet<String> set = new HashSet<>(vehicleTypeList);

            // Convert HashSet back to ArrayList
            vehicleTypeList = new ArrayList<>(set);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    public void search() throws SQLException {
        refreshArrayLists();

        // Convert ArrayList to HashSet to remove duplicates
        HashSet<String> set = new HashSet<>(itemCodeList);

        // Convert HashSet back to ArrayList
        itemCodeList= new ArrayList<>(set);

        // Convert ArrayList to HashSet to remove duplicates
        HashSet<String> set2 = new HashSet<>(vehicleCodeList);

        // Convert HashSet back to ArrayList
        vehicleCodeList = new ArrayList<>(set2);

        col1_VehicleCode.setCellValueFactory(cellData -> cellData.getValue().vehicleCodeProperty());
        col2_ItemCode.setCellValueFactory(cellData -> cellData.getValue().itemCodeProperty());
        col3_Brand.setCellValueFactory(cellData -> cellData.getValue().brandProperty());
        col4_Model.setCellValueFactory(cellData -> cellData.getValue().modelProperty());
        col5_ItemName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
        col6_VehicleType.setCellValueFactory(cellData -> cellData.getValue().vehicleTypeProperty());
//        col7_ModelYear.setCellValueFactory(cellData -> cellData.getValue().modelYearProperty().asObject());
        col7_ModelYear.setCellValueFactory(cellData -> {
            SimpleIntegerProperty modelYearProperty = cellData.getValue().modelYearProperty();
            return modelYearProperty != null ? modelYearProperty.asObject() : new SimpleIntegerProperty().asObject();
        });

//        col8_UnitPrice.setCellValueFactory(cellData -> cellData.getValue().unitPriceProperty().asObject());
        col8_UnitPrice.setCellValueFactory(cellData -> {
            SimpleDoubleProperty unitPriceProperty = cellData.getValue().unitPriceProperty();
            return unitPriceProperty != null ? unitPriceProperty.asObject() : new SimpleDoubleProperty().asObject();
        });

        col9_GasolineType.setCellValueFactory(cellData -> cellData.getValue().gasolineTypeProperty());
//        col10_Quantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        col10_Quantity.setCellValueFactory(cellData -> {
            SimpleIntegerProperty quantityProperty = cellData.getValue().quantityProperty();
            return quantityProperty != null ? quantityProperty.asObject() : new SimpleIntegerProperty().asObject();
        });

        ArrayList<String> printedVehicleCodeList = new ArrayList<>();



        if (notAdd=='Y'){

            for (String items: itemCodeList){

                String selectQuery = "SELECT * FROM parts.parts_store WHERE item_code = ?";
                PreparedStatement statement = connection.prepareStatement(selectQuery);
                statement.setString(1, items);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String item_Code = resultSet.getString("item_Code");
                    String vehicle_code = resultSet.getString("vehicle_code");
                    printedVehicleCodeList.add(vehicle_code);
                    String item = resultSet.getString("item");
                    Double unit_price = resultSet.getDouble("unit_price");
                    int qty = resultSet.getInt("qty");
                    // Retrieve other details as needed

                    // Do something with the retrieved details
                    String selectQuery2 = "SELECT * FROM parts.vehicle_type WHERE vehicle_code = ?";
                    PreparedStatement statement2 = connection.prepareStatement(selectQuery2);
                    statement2.setString(1, vehicle_code);

                    ResultSet resultSet2 = statement2.executeQuery();

                    String brand = null;
                    String model = null;
                    int model_year = 0;
                    String vehicle_type = null;
                    String gasoline_type = null;


                    if (resultSet2.next()) {
                        brand = resultSet2.getString("brand");
                        model = resultSet2.getString("model");
                        model_year = resultSet2.getInt("model_year");
                        vehicle_type = resultSet2.getString("vehicle_type");
                        gasoline_type = resultSet2.getString("gasoline_type");
                    }
                    /*col1_VehicleCode.setCellValueFactory(cellData -> cellData.getValue().vehicleCodeProperty());
                    col2_ItemCode.setCellValueFactory(cellData -> cellData.getValue().itemCodeProperty());
                    col3_Brand.setCellValueFactory(cellData -> cellData.getValue().brandProperty());
                    col4_Model.setCellValueFactory(cellData -> cellData.getValue().modelProperty());
                    col5_ItemName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
                    col6_VehicleType.setCellValueFactory(cellData -> cellData.getValue().vehicleTypeProperty());
                    col7_ModelYear.setCellValueFactory(cellData -> cellData.getValue().modelYearProperty().asObject());
                    col8_UnitPrice.setCellValueFactory(cellData -> cellData.getValue().unitPriceProperty().asObject());
                    col9_GasolineType.setCellValueFactory(cellData -> cellData.getValue().gasolineTypeProperty());
                    col10_Quantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());*/
                    // Do something with the retrieved values
                    SearchController.Search_Table addItems = new SearchController.Search_Table(vehicle_code,item_Code,brand,model,item,vehicle_type,model_year,unit_price,gasoline_type,qty);
                    tb_main.getItems().add(addItems);

                }

            }

            vehicleCodeList.removeIf(printedVehicleCodeList::contains);

        }



        //add blank space
/*        SearchController.Search_Table addItems = new SearchController.Search_Table();
        tb_main.getItems().add(addItems);*/


        for (String vehicleCode:vehicleCodeList){
            String selectQuery2 = "SELECT * FROM parts.vehicle_type WHERE vehicle_code = ?";
            PreparedStatement statement2 = connection.prepareStatement(selectQuery2);
            statement2.setString(1, vehicleCode);

            ResultSet resultSet2 = statement2.executeQuery();

            String brand = null;
            String model = null;
            int model_year = 0;
            String vehicle_type = null;
            String gasoline_type = null;


            if (resultSet2.next()) {
                brand = resultSet2.getString("brand");
                model = resultSet2.getString("model");
                model_year = resultSet2.getInt("model_year");
                vehicle_type = resultSet2.getString("vehicle_type");
                gasoline_type = resultSet2.getString("gasoline_type");
            }

            // Do something with the retrieved values
            SearchController.Search_Table addItems2 = new SearchController.Search_Table("-","-",brand,model,"-",vehicle_type,model_year,0.0,gasoline_type,1);
            tb_main.getItems().add(addItems2);
        }




    }



}

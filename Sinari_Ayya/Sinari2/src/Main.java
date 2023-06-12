import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        /*Scanner scanner = new Scanner(System.in);
        String item_code = scanner.next();
        String vehicle_code = scanner.next();
        String item = scanner.next();
        Double unit_price = scanner.nextDouble();
        int qty = scanner.nextInt();*/

        String jdbcURL = "jdbc:postgresql://localhost:5410/uniquely_auto_parts";
        String username = "postgres";
        String password = "Sinari2000";

        try {
            Connection connection = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connected to the PostgreSQL database!");

            // Perform database operations...
            String sql = "INSERT INTO parts.body_parts (item_code, vehicle_code, item,unit_price,qty) VALUES (?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, "fdddddfffffffff");
            statement.setString(2, "fffffff");
            statement.setString(3, "fff lamp");
            statement.setDouble(4, 60000);
            statement.setInt(5, 12);

            int rowsAffected = statement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);


            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Connection failed! Error: " + e.getMessage());
        }






        try {
            String sql = "SELECT item_code FROM parts.parts_store";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String vehicleCode = resultSet.getString("item_code");
                ItemCode_list.add(vehicleCode);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Can't load Item code list.");
            return;
        }



        if (!tf_VehicleCode.getText().isEmpty()){
            vehicleCode=tf_VehicleCode.getText();
        }
        if (vehicleCode!=null){
            String t_ItemCode=null;
            String t_VehicleCode =null;
            String t_ItemName=null;
            Double t_UnitPrice=null;
            int t_Qty=0;
            String t_Brand=null;
            String t_Model=null;
            int t_ModelYear=0;
            String t_VehicleType=null;
            String t_GasolineType=null;

            try  {
                // Prepare the select statement
                String selectQuery = "SELECT * FROM parts.parts_store WHERE item_code = ?";
                PreparedStatement statement = connection.prepareStatement(selectQuery);
                statement.setString(1, itemCode);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    t_ItemCode = resultSet.getString("item_code");
                    t_VehicleCode = resultSet.getString("vehicle_code");
                    t_ItemName = resultSet.getString("item");
                    t_UnitPrice = resultSet.getDouble("unit_price");
                    t_Qty = resultSet.getInt("qty");

                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Item not loaded.");
                return;
            }
            try  {
                // Prepare the select statement
                String selectQuery = "SELECT * FROM parts.vehicle_type WHERE vehicle_code = ?";
                PreparedStatement statement = connection.prepareStatement(selectQuery);
                statement.setString(1, t_VehicleCode);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    t_Brand = resultSet.getString("brand");
                    t_Model = resultSet.getString("model");
                    t_ModelYear = resultSet.getInt("model_year");
                    t_VehicleType = resultSet.getString("vehicle_type");
                    t_GasolineType = resultSet.getString("gasoline_type");

                }
            } catch (SQLException e) {
                e.printStackTrace();
                return;
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


            SearchController.Search_Table addItems = new SearchController.Search_Table(t_VehicleCode,t_ItemCode,t_Brand,t_Model,t_ItemName,t_VehicleType,t_ModelYear,t_UnitPrice,t_GasolineType,t_Qty);
            tb_main.getItems().add(addItems);

        }






















    }

}



























package com.example.sinari;

        import javafx.beans.property.SimpleDoubleProperty;
        import javafx.beans.property.SimpleIntegerProperty;
        import javafx.beans.property.SimpleStringProperty;
        import javafx.beans.property.StringProperty;
        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.geometry.Side;
        import javafx.scene.control.*;
        import javafx.scene.input.KeyCode;
        import javafx.scene.input.KeyEvent;

        import java.sql.*;
        import java.util.ArrayList;
        import java.util.HashSet;
        import java.util.Objects;

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
    private ContextMenu CM_VehicleCode;
    @FXML
    private ContextMenu CM_ModelYear;

    @FXML
    private ContextMenu CM_VehicleType;



    @FXML
    private TextField tf_Item;
    @FXML
    private TextField tf_ModelYear;

    @FXML
    private TextField tf_Brand;

    @FXML
    private TextField tf_ItemCode;

    @FXML
    private TextField tf_Model;

    @FXML
    private TextField tf_VehicleCode;

    @FXML
    private TextField tf_VehicleType;


    private ArrayList<String> ItemCode_list = new ArrayList<>();
    private ArrayList<String> temp_ItemNameList = new ArrayList<>();
    private ArrayList<String> matchingItems = new ArrayList<>();
    private ArrayList<String> matchingItems2 = new ArrayList<>();
    private ArrayList<String> vehicleCode_list = new ArrayList<>();
    private ArrayList<String> brand_list = new ArrayList<>();
    private ArrayList<String> model_list = new ArrayList<>();
    private ArrayList<String> modelYear_list = new ArrayList<>();
    private ArrayList<String> gasolineType_list = new ArrayList<>();
    private ArrayList<String> vehicleType_list = new ArrayList<>();
    private String gasolineType;
    final private String[] gasolineTypes = {"Petrol","Diesel","Electric","Hybrid"};

    public SearchController() throws SQLException {
    }
    public void loadItemCodes() {
        try {
            String sql = "SELECT vehicle_code FROM parts.vehicle_type";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String vehicleCode = resultSet.getString("vehicle_code");
                vehicleCode_list.add(vehicleCode);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Can't load Vehicle code list.");
            return;
        }
        try {
            String sql = "SELECT item_code FROM parts.parts_store";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String vehicleCode = resultSet.getString("item_code");
                ItemCode_list.add(vehicleCode);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Can't load Item code list.");
            return;
        }


    }


    public  void initialize (){
        loadItemCodes();









        tf_Item.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!tf_Item.getText().isEmpty()) if_codeItem_Add();
            }

        });









        /*tf_VehicleCode.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!tf_VehicleCode.getText().isEmpty()) if_codeItem_Add();
            }

        });*/

      /*  tf_Brand.setOnMouseClicked(e -> {
            create_itemCodeList();

            // Prepare the select statement
            String selectQuery = "SELECT  brand FROM parts.vehicle_type WHERE vehicle_code = ?";
            PreparedStatement statement = null;
            try {
                statement = connection.prepareStatement(selectQuery);
                // Iterate over the item codes and retrieve the quantities
                for (String vehicleCode : vehicleCode_list) {
                    // Set the item code in the query
                    statement.setString(1, vehicleCode);

                    // Execute the query and retrieve the result set
                    ResultSet resultSet = statement.executeQuery();

                    // Retrieve the quantity from the result set
                    String temp = null;
                    if (resultSet.next()) {
                        temp = resultSet.getString("brand");
                    }

                    // Add the quantity to the quantityList
                    brand_list.add(temp);
                }
                // Create a HashSet from the ArrayList to remove duplicates
                HashSet<String> uniqueBrand = new HashSet<>(brand_list);
                // Create a new ArrayList from the HashSet
                ArrayList<String> listWithoutDuplicates = new ArrayList<>(uniqueBrand);

                brand_list=listWithoutDuplicates;

            } catch (SQLException ex) {
                System.out.println("brand list can't load.");
                throw new RuntimeException(ex);

            }
            if (!tf_ItemCode.getText().isEmpty()){
                if_codeItem_Add();
            }
        });*/

        /*tf_Brand.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!tf_Brand.getText().isEmpty()) if_codeItem_Add();
            }

        });*/


        /*tf_Model.setOnMouseClicked(e -> {
            create_itemCodeList();
            // Prepare the select statement
            String selectQuery = "SELECT  model FROM parts.vehicle_type WHERE vehicle_code = ?";
            PreparedStatement statement = null;
            try {
                statement = connection.prepareStatement(selectQuery);
                // Iterate over the item codes and retrieve the quantities
                for (String vehicleCode : vehicleCode_list) {
                    // Set the item code in the query
                    statement.setString(1, vehicleCode);

                    // Execute the query and retrieve the result set
                    ResultSet resultSet = statement.executeQuery();

                    // Retrieve the quantity from the result set
                    String temp = null;
                    if (resultSet.next()) {
                        temp = resultSet.getString("model");
                    }

                    // Add the quantity to the quantityList
                    model_list.add(temp);
                }
                // Create a HashSet from the ArrayList to remove duplicates
                HashSet<String> uniqueModel = new HashSet<>(model_list);
                // Create a new ArrayList from the HashSet
                ArrayList<String> listWithoutDuplicates = new ArrayList<>(uniqueModel);

                model_list=listWithoutDuplicates;

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            if (!tf_ItemCode.getText().isEmpty()){
                if_codeItem_Add();
            }
        });*/

        /*tf_Model.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!tf_Model.getText().isEmpty()) if_codeItem_Add();
            }

        });*/


        /*tf_ModelYear.setOnMouseClicked(e -> {
            create_itemCodeList();

            // Prepare the select statement
            String selectQuery = "SELECT model_year FROM parts.vehicle_type WHERE vehicle_code = ?";
            PreparedStatement statement = null;
            try {
                statement = connection.prepareStatement(selectQuery);
                // Iterate over the item codes and retrieve the quantities
                for (String vehicleCode : vehicleCode_list) {
                    // Set the item code in the query
                    statement.setString(1, vehicleCode);

                    // Execute the query and retrieve the result set
                    ResultSet resultSet = statement.executeQuery();

                    // Retrieve the quantity from the result set
                    int temp = 0;
                    if (resultSet.next()) {
                        temp = resultSet.getInt("model_year");
                    }

                    // Add the quantity to the quantityList
                    modelYear_list.add(String.valueOf(temp));
                }
                // Create a HashSet from the ArrayList to remove duplicates
                HashSet<String> uniqueModelYear = new HashSet<>(modelYear_list);
                // Create a new ArrayList from the HashSet
                ArrayList<String> listWithoutDuplicates = new ArrayList<>(uniqueModelYear);

                modelYear_list=listWithoutDuplicates;

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            if (!tf_ItemCode.getText().isEmpty()){
                if_codeItem_Add();
            }
        });*/

        /*tf_ModelYear.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!tf_ModelYear.getText().isEmpty()) if_codeItem_Add();
            }

        });*/


        /*CB_GasolineType.setOnMouseClicked(e -> {
            create_itemCodeList();
            // Prepare the select statement
            String selectQuery = "SELECT  gasoline_type FROM parts.vehicle_type WHERE vehicle_code = ?";
            PreparedStatement statement = null;
            try {
                statement = connection.prepareStatement(selectQuery);

                // Clear the gasolineType_list before populating it
                gasolineType_list.clear();


                // Iterate over the item codes and retrieve the quantities
                for (String vehicleCode : vehicleCode_list) {
                    // Set the item code in the query
                    statement.setString(1, vehicleCode);

                    // Execute the query and retrieve the result set
                    ResultSet resultSet = statement.executeQuery();

                    // Retrieve the quantity from the result set
                    String temp = null;
                    if (resultSet.next()) {
                        temp = resultSet.getString("gasoline_type");
                    }

                    // Add the quantity to the quantityList
                    gasolineType_list.add(temp);
                }
                // Create a HashSet from the ArrayList to remove duplicates
                HashSet<String> uniqueGasolineType_list = new HashSet<>(gasolineType_list);
                // Create a new ArrayList from the HashSet
                ArrayList<String> listWithoutDuplicates = new ArrayList<>(uniqueGasolineType_list);
                // Remove null values from the list
                listWithoutDuplicates.removeIf(Objects::isNull);
                // Update the gasolineType_list with the list without duplicates and null values
                gasolineType_list = listWithoutDuplicates;

                // Clear the choice box items and add the updated gasolineType_list
                CB_GasolineType.getItems().clear();
                CB_GasolineType.getItems().addAll(gasolineType_list);







            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            if (!tf_ItemCode.getText().isEmpty()){
                if_codeItem_Add();
            }
        });*/


        /*tf_VehicleType.setOnMouseClicked(e -> {
            create_itemCodeList();
            // Prepare the select statement
            String selectQuery = "SELECT  vehicle_type FROM parts.vehicle_type WHERE vehicle_code = ?";
            PreparedStatement statement = null;
            try {
                statement = connection.prepareStatement(selectQuery);
                // Iterate over the item codes and retrieve the quantities
                for (String vehicleCode : vehicleCode_list) {
                    // Set the item code in the query
                    statement.setString(1, vehicleCode);

                    // Execute the query and retrieve the result set
                    ResultSet resultSet = statement.executeQuery();

                    // Retrieve the quantity from the result set
                    String temp = null;
                    if (resultSet.next()) {
                        temp = resultSet.getString("vehicle_type");
                    }

                    // Add the quantity to the quantityList
                    vehicleType_list.add(temp);
                }
                // Create a HashSet from the ArrayList to remove duplicates
                HashSet<String> uniqueVehicleType_list = new HashSet<>(vehicleType_list);
                // Create a new ArrayList from the HashSet
                ArrayList<String> listWithoutDuplicates = new ArrayList<>(uniqueVehicleType_list);

                vehicleType_list=listWithoutDuplicates;

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            if (!tf_ItemCode.getText().isEmpty()){
                if_codeItem_Add();
            }
        });*/

        /*tf_VehicleType.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!tf_VehicleType.getText().isEmpty()) if_codeItem_Add();
            }

        });*/


        public Search_Table


    }

    /*public void updateCodeList(){
        // Prepare the select statement
        String selectQuery = "SELECT  vehicle_code FROM parts.parts_store WHERE item_code = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(selectQuery);
            // Iterate over the item codes and retrieve the quantities
            for (String itemCode : ItemCode_list) {
                // Set the item code in the query
                statement.setString(1, itemCode);

                // Execute the query and retrieve the result set
                ResultSet resultSet = statement.executeQuery();

                // Retrieve the quantity from the result set
                String temp = null;
                if (resultSet.next()) {
                    temp = resultSet.getString("vehicle_code");
                }

                // Add the quantity to the quantityList
                vehicleCode_list.add(temp);
            }
            // Create a HashSet from the ArrayList to remove duplicates
            HashSet<String> uniqueNumbers = new HashSet<>(vehicleCode_list);
            // Create a new ArrayList from the HashSet
            ArrayList<String> listWithoutDuplicates = new ArrayList<>(uniqueNumbers);

            vehicleCode_list=listWithoutDuplicates;

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }*/       //update vehicleCode


    /*public void if_codeItem_Add(){
        String vehicleCode =null;
        //check item code correct
        boolean notContinue_rest=true;
        for (String item:ItemCode_list){
            if (tf_ItemCode.getText().trim().equals(item.trim())){
                notContinue_rest=false;
            }
        }
        if (notContinue_rest)return;

    } */
    /*public  void if_itemCode_or_itemName_add(){
        ArrayList<String> itemName_ItemCodeList = new ArrayList<>();
        if(tf_ItemCode.getText().isEmpty() && tf_Item.getText().isEmpty())return;

        if (tf_ItemCode.getText().isEmpty()) {
            try {
                String query = "SELECT item_code FROM parts.parts_store WHERE item = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, tf_Item.getText());
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    String code = resultSet.getString("item_code");

                    itemName_ItemCodeList.add(code);
                }
                ItemCode_list =itemName_ItemCodeList;

            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
        }else {
            for (String item:ItemCode_list){
                if (tf_ItemCode.getText().trim().equals(item.trim())){
                    // vaguvata detail tika pura vanna ///////////////////////////////////////////
                }
            }
        }
    }           //iem code eken vehicle code eka aran yanna issarahata

    public  void ifNot_itemCode_or_itemName_add(){
        ArrayList<String> allCell_ItemCodeList = new ArrayList<>();
        ArrayList<String> allCell_VehicleCodeList = new ArrayList<>();

        if (!tf_VehicleCode.getText().isEmpty()){
            boolean continueRest = false;
            for (String item:vehicleCode_list){
                if (tf_VehicleCode.getText().trim().equals(item.trim())){
                    continueRest =true;
                    if (tf_Brand.getText().isEmpty() && tf_ModelYear.getText().isEmpty() && tf_Model.getText().isEmpty() && CB_GasolineType.getValue().isEmpty() && tf_VehicleType.getText().isEmpty() ) allCell_VehicleCodeList.add(tf_VehicleCode.getText());
                    break;
                }
            }
            if (!continueRest) {
                tf_VehicleCode.clear();
                return;
            }


            try {
                // Prepare the select statement
                String selectQuery = "SELECT item_code FROM parts.parts_store WHERE vehicle_code = ?";
                PreparedStatement statement = connection.prepareStatement(selectQuery);
                statement.setString(1, tf_VehicleCode.getText());

                // Execute the query and retrieve the result set
                ResultSet resultSet = statement.executeQuery();



                // Iterate over the result set and retrieve the item codes
                while (resultSet.next()) {
                    String itemCode = resultSet.getString("item_code");
                    allCell_ItemCodeList.add(itemCode);
                }



            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (!tf_Brand.getText().isEmpty()){
            boolean continueRest = false;
            for (String item:brand_list){
                if (tf_Brand.getText().trim().equals(item.trim())){
                    continueRest =true;
                    break;
                }
            }
            if (!continueRest) {
                tf_Brand.clear();
                return;
            }
            ArrayList<String> brand_VehicleCodeList = new ArrayList<>();

            try {
                String query = "SELECT brand FROM parts.vehicle_type WHERE vehicle_code = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, tf_Item.getText());
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    String code = resultSet.getString("vehicle_code");

                    brand_VehicleCodeList.add(code);
                }
                vehicleCode_list =brand_VehicleCodeList;

            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }



            try {
                // Prepare the select statement
                statement.setString(1, tf_Brand.getText());

                // Execute the query and retrieve the result set
                ResultSet resultSet = statement.executeQuery();



                // Iterate over the result set and retrieve the item codes
                while (resultSet.next()) {
                    String itemCode = resultSet.getString("vehicle_code");
                    allCell_VehicleCodeList.add(itemCode);
                }



            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (!tf_Model.getText().isEmpty()){
            boolean continueRest = false;
            for (String item:model_list){
                if (tf_Model.getText().trim().equals(item.trim())){
                    continueRest =true;
                    break;
                }
            }
            if (!continueRest) {
                tf_Model.clear();
                return;
            }



            try {
                // Prepare the select statement
                String selectQuery = "SELECT model FROM parts.vehicle_type WHERE vehicle_code = ?";
                PreparedStatement statement = connection.prepareStatement(selectQuery);
                statement.setString(1, tf_Model.getText());

                // Execute the query and retrieve the result set
                ResultSet resultSet = statement.executeQuery();



                // Iterate over the result set and retrieve the item codes
                while (resultSet.next()) {
                    String itemCode = resultSet.getString("vehicle_code");
                    allCell_VehicleCodeList.add(itemCode);
                }



            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (!tf_Model.getText().isEmpty()){
            boolean continueRest = false;
            for (String item:model_list){
                if (tf_Model.getText().trim().equals(item.trim())){
                    continueRest =true;
                    break;
                }
            }
            if (!continueRest) {
                tf_Model.clear();
                return;
            }



            try {
                // Prepare the select statement
                String selectQuery = "SELECT model FROM parts.vehicle_type WHERE vehicle_code = ?";
                PreparedStatement statement = connection.prepareStatement(selectQuery);
                statement.setString(1, tf_Model.getText());

                // Execute the query and retrieve the result set
                ResultSet resultSet = statement.executeQuery();



                // Iterate over the result set and retrieve the item codes
                while (resultSet.next()) {
                    String itemCode = resultSet.getString("vehicle_code");
                    allCell_VehicleCodeList.add(itemCode);
                }



            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (!tf_Model.getText().isEmpty()){
            boolean continueRest = false;
            for (String item:modelYear_list){
                if (tf_ModelYear.getText().trim().equals(item.trim())){
                    continueRest =true;
                    break;
                }
            }
            if (!continueRest) {
                tf_ModelYear.clear();
                return;
            }



            try {
                // Prepare the select statement
                String selectQuery = "SELECT model_year FROM parts.vehicle_type WHERE vehicle_code = ?";
                PreparedStatement statement = connection.prepareStatement(selectQuery);
                statement.setString(1, tf_ModelYear.getText());

                // Execute the query and retrieve the result set
                ResultSet resultSet = statement.executeQuery();



                // Iterate over the result set and retrieve the item codes
                while (resultSet.next()) {
                    String itemCode = resultSet.getString("vehicle_code");
                    allCell_VehicleCodeList.add(itemCode);
                }



            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (!tf_VehicleType.getText().isEmpty()){
            boolean continueRest = false;
            for (String item:vehicleType_list){
                if (tf_VehicleType.getText().trim().equals(item.trim())){
                    continueRest =true;
                    break;
                }
            }
            if (!continueRest) {
                tf_VehicleType.clear();
                return;
            }



            try {
                // Prepare the select statement
                String selectQuery = "SELECT vehicle_type FROM parts.vehicle_type WHERE vehicle_code = ?";
                PreparedStatement statement = connection.prepareStatement(selectQuery);
                statement.setString(1, tf_VehicleType.getText());

                // Execute the query and retrieve the result set
                ResultSet resultSet = statement.executeQuery();



                // Iterate over the result set and retrieve the item codes
                while (resultSet.next()) {
                    String itemCode = resultSet.getString("vehicle_code");
                    allCell_VehicleCodeList.add(itemCode);
                }



            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }*/





    public void if_itemNot_available(){
        try {

            // Prepare the select statement
            StringBuilder selectQuery = new StringBuilder("SELECT * FROM parts.vehicle_type WHERE 1=1");
            ArrayList<Object> queryParams = new ArrayList<>();

            if (!tf_VehicleCode.getText().isEmpty()) {
                selectQuery.append(" AND vehicle_code = ?");
                queryParams.add(tf_VehicleCode.getText());
            }
            if (!tf_Brand.getText().isEmpty()) {
                selectQuery.append(" AND brand = ?");
                queryParams.add(tf_Brand.getText());
            }
            if (!tf_Model.getText().isEmpty()) {
                selectQuery.append(" AND model = ?");
                queryParams.add(tf_Model.getText());
            }
            if (!tf_ModelYear.getText().isEmpty()) {
                selectQuery.append(" AND model_year = ?");
                queryParams.add(tf_ModelYear.getText());
            }
            if (!CB_GasolineType.getValue().isEmpty()) {
                selectQuery.append(" AND gasoline_type = ?");
                queryParams.add(CB_GasolineType.getValue());
            }
            if (!tf_VehicleType.getText().isEmpty()) {
                selectQuery.append(" AND vehicle_type = ?");
                queryParams.add(tf_VehicleType);
            }

            PreparedStatement statement = connection.prepareStatement(selectQuery.toString());
            for (int i = 0; i < queryParams.size(); i++) {
                statement.setObject(i + 1, queryParams.get(i));
            }

            // Execute the query and retrieve the result set
            ResultSet resultSet = statement.executeQuery();
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

            // Process the result set
            while (resultSet.next()) {
                // Retrieve the item details from the result set
                String vehicle_code = resultSet.getString("vehicle_code");
                String brand = resultSet.getString("brand");
                String model = resultSet.getString("model");
                String vehicle_type = resultSet.getString("vehicle_type");
                String model_year = resultSet.getString("model_year");
                String gasoline_type = resultSet.getString("gasoline_type");

                SearchController.Search_Table addItems = new SearchController.Search_Table(vehicle_code,"-",brand,model,"-",vehicle_type,Integer.parseInt(model_year),0.0,gasoline_type,0);
                tb_main.getItems().add(addItems);

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void search(){
        clearTable();
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

        if (!tf_ItemCode.getText().isEmpty()){
            for (String item: ItemCode_list){
                if (tf_ItemCode.getText().trim().equals(item.trim())){
                    try {

                        // Prepare the select statement
                        String selectQuery = "SELECT * FROM parts.parts_store WHERE item_code = ?";
                        PreparedStatement statement = connection.prepareStatement(selectQuery);

                        // Set the item code parameter in the query
                        statement.setString(1, tf_ItemCode.getText());

                        // Execute the query and retrieve the result set
                        ResultSet resultSet = statement.executeQuery();

                        // Process the result set
                        if (resultSet.next()) {
                            // Retrieve the values from the result set
                            String item_code = resultSet.getString("item_code");
                            String vehicle_code = resultSet.getString("vehicle_code");
                            String itemName = resultSet.getString("item");
                            String unit_price = resultSet.getString("unit_price");
                            String qty = resultSet.getString("qty");
                            try {

                                // Prepare the select statement
                                String selectQuery2 = "SELECT * FROM parts.vehicle_type WHERE vehicle_code = ?";
                                PreparedStatement statement2 = connection.prepareStatement(selectQuery2);

                                // Set the item code parameter in the query
                                statement2.setString(1, vehicle_code);

                                // Execute the query and retrieve the result set
                                ResultSet resultSet2 = statement2.executeQuery();

                                // Process the result set
                                if (resultSet2.next()) {
                                    // Retrieve the values from the result set
                                    String brand = resultSet2.getString("brand");
                                    String model = resultSet2.getString("model");
                                    String model_year = resultSet2.getString("model_year");
                                    String vehicle_type = resultSet2.getString("vehicle_type");
                                    String gasoline_type = resultSet2.getString("gasoline_type");

                                    SearchController.Search_Table addItems = new SearchController.Search_Table(vehicle_code,item_code,brand,model,itemName,vehicle_type,Integer.parseInt(model_year),Double.parseDouble(unit_price),gasoline_type,Integer.parseInt(qty));
                                    tb_main.getItems().add(addItems);
                                } else {
                                    System.out.println("Item not found in the database.");
                                }
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }


                        } else {
                            System.out.println("Item not found in the database.");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            return;
        }

        else {
            try {

                // Prepare the select statement
                StringBuilder selectQuery = new StringBuilder("SELECT * FROM parts.parts_store pt JOIN parts.vehicle_type vp ON pt.vehicle_code = vp.vehicle_code WHERE 1=1");

                ArrayList<Object> parameters = new ArrayList<>();

                // Add conditions based on the provided inputs
                if (!tf_Item.getText().isEmpty()) {
                    selectQuery.append(" AND pt.item = ?");
                    parameters.add(tf_Item.getText());
                }

                if (!tf_VehicleCode.getText().isEmpty()) {
                    selectQuery.append(" AND vp.vehicle_code = ?");
                    parameters.add(tf_VehicleCode.getText());
                }

                if (!tf_Brand.getText().isEmpty()) {
                    selectQuery.append(" AND vp.brand = ?");
                    parameters.add(tf_Brand.getText());
                }
                if (!tf_Model.getText().isEmpty()) {
                    selectQuery.append(" AND vp.model = ?");
                    parameters.add(tf_Model.getText());
                }
                if (!tf_ModelYear.getText().isEmpty()) {
                    selectQuery.append(" AND vp.model_year = ?");
                    parameters.add(tf_ModelYear.getText());
                }
                if (!CB_GasolineType.getValue().isEmpty()) {
                    selectQuery.append(" AND vp.gasolineType = ?");
                    parameters.add(CB_GasolineType.getValue());
                }
                if (!tf_VehicleType.getText().isEmpty()) {
                    selectQuery.append(" AND vp.vehicle_type = ?");
                    parameters.add(tf_VehicleType.getText());
                }

                PreparedStatement statement = connection.prepareStatement(selectQuery.toString());

                // Set the parameters in the query
                for (int i = 0; i < parameters.size(); i++) {
                    statement.setObject(i + 1, parameters.get(i));
                }

                // Execute the query and retrieve the result set
                ResultSet resultSet = statement.executeQuery();

                // Process the result set
                while (resultSet.next()) {
                    // Retrieve the values from the result set
                    String Item = resultSet.getString("pt.item");
                    String VehicleCode = resultSet.getString("vt.vehicle_code");
                    String Brand = resultSet.getString("vt.brand");
                    String Model = resultSet.getString("vt.model");
                    String ModelYear = resultSet.getString("vt.model_year");
                    String GasolineType = resultSet.getString("vt.gasoline_type");
                    String VehicleType = resultSet.getString("vt.vehicle_type");

                    String ItemCode = resultSet.getString("pt.item_code");
                    String UnitPrice = resultSet.getString("pt.unit_price");
                    String Qty = resultSet.getString("pt.qty");



                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }


    /* public void create_itemCodeList() {
         if (tf_Item.getText().isEmpty() && tf_ItemCode.getText().isEmpty() ) return;

         String item;
         String vehicleCode;
         String brand;
         String model;
         int modeYear;
         String vehicleType ;

         if (!tf_ItemCode.getText().isEmpty()) if_codeItem_Add();

         if (!tf_Item.getText().isEmpty()) {
             item = tf_Item.getText();
             try {
                 String query = "SELECT * FROM parts.parts_store WHERE item = ?";
                 PreparedStatement statement = connection.prepareStatement(query);
                 statement.setString(1, item);
                 ResultSet resultSet = statement.executeQuery();

                 while (resultSet.next()) {
                     String code = resultSet.getString("item_code");

                     matchingItems.add(code);
                 }
                 ItemCode_list = matchingItems;

             } catch (SQLException e) {
                 e.printStackTrace();
                 return;
             }

         }
         if (!tf_VehicleCode.getText().isEmpty()) {
             vehicleCode = tf_VehicleCode.getText();
             try {
                 String query = "SELECT * FROM parts.parts_store WHERE vehicle_code = ?";
                 PreparedStatement statement = connection.prepareStatement(query);
                 statement.setString(1, vehicleCode);
                 ResultSet resultSet = statement.executeQuery();

                 while (resultSet.next()) {
                     String code = resultSet.getString("item_code");
                     for (String i : matchingItems) {
                         if (code.equals(i)) matchingItems2.add(i);
                     }

                 }
                 matchingItems = matchingItems2;
                 ItemCode_list = matchingItems;
                 matchingItems2.clear();

             } catch (SQLException e) {
                 e.printStackTrace();
                 return;
             }

         }
         if (!tf_Brand.getText().isEmpty()) {
             brand = tf_Brand.getText();
             try {
                 String query = "SELECT * FROM parts.vehicle_type WHERE brand = ?";
                 PreparedStatement statement = connection.prepareStatement(query);
                 statement.setString(1, brand);
                 ResultSet resultSet = statement.executeQuery();

                 while (resultSet.next()) {
                     String code = resultSet.getString("item_code");
                     for (String i : matchingItems) {
                         if (code.equals(i)) matchingItems2.add(i);
                     }

                 }
                 matchingItems = matchingItems2;
                 ItemCode_list = matchingItems;
                 matchingItems2.clear();

             } catch (SQLException e) {
                 e.printStackTrace();
                 return;
             }


         }
         if (!tf_Model.getText().isEmpty()){
             model = tf_Model.getText();
             try{
                 String query = "SELECT * FROM parts.vehicle_type WHERE model = ?";
                 PreparedStatement statement = connection.prepareStatement(query);
                 statement.setString(1, model);
                 ResultSet resultSet = statement.executeQuery();

                 while (resultSet.next()) {
                     String code = resultSet.getString("item_code");
                     for (String i: matchingItems){
                         if (code.equals(i)) matchingItems2.add(i);
                     }

                 }
                 matchingItems=matchingItems2;
                 ItemCode_list = matchingItems;
                 matchingItems2.clear();

             }catch (SQLException e) {
                 e.printStackTrace();
                 return;
             }




         }
         if (!tf_ModelYear.getText().isEmpty()){
             modeYear = Integer.parseInt(tf_ModelYear.getText());
             try{
                 String query = "SELECT * FROM parts.vehicle_type WHERE model_year = ?";
                 PreparedStatement statement = connection.prepareStatement(query);
                 statement.setString(1, String.valueOf(modeYear));
                 ResultSet resultSet = statement.executeQuery();

                 while (resultSet.next()) {
                     String code = resultSet.getString("item_code");
                     for (String i: matchingItems){
                         if (code.equals(i)) matchingItems2.add(i);
                     }

                 }
                 matchingItems=matchingItems2;
                 ItemCode_list = matchingItems;
                 matchingItems2.clear();

             }catch (SQLException e) {
                 e.printStackTrace();
                 return;
             }




         }
         if (!tf_VehicleType.getText().isEmpty()){
             vehicleType = tf_VehicleType.getText();
             try{
                 String query = "SELECT * FROM parts.vehicle_type WHERE vehicle_type = ?";
                 PreparedStatement statement = connection.prepareStatement(query);
                 statement.setString(1, String.valueOf(vehicleType));
                 ResultSet resultSet = statement.executeQuery();

                 while (resultSet.next()) {
                     String code = resultSet.getString("item_code");
                     for (String i: matchingItems){
                         if (code.equals(i)) matchingItems2.add(i);
                     }

                 }
                 matchingItems=matchingItems2;
                 ItemCode_list = matchingItems;
                 matchingItems2.clear();

             }catch (SQLException e) {
                 e.printStackTrace();
                 return;
             }




         }


     }*/
    public void clearTable() {
        tb_main.getItems().clear();
    }

}


public class Item{
    private String VehicleCode;
    private String ItemCode;
    private String Brand;
    private String Model;
    private String ItemName;
    private String VehicleType;
    private int ModelYear;
    private Double UnitPrice;
    private String GasolineType;
    private int Quantity;

    public Item(String vehicleCode, String itemCode, String brand, String model, String itemName, String vehicleType, int modelYear, Double unitPrice, String gasolineType, int quantity) {
        VehicleCode = vehicleCode;
        ItemCode = itemCode;
        Brand = brand;
        Model = model;
        ItemName = itemName;
        VehicleType = vehicleType;
        ModelYear = modelYear;
        UnitPrice = unitPrice;
        GasolineType = gasolineType;
        Quantity = quantity;
    }

    public String getVehicleCode() {
        return VehicleCode;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public String getBrand() {
        return Brand;
    }

    public String getModel() {
        return Model;
    }

    public String getItemName() {
        return ItemName;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public int getModelYear() {
        return ModelYear;
    }

    public Double getUnitPrice() {
        return UnitPrice;
    }

    public String getGasolineType() {
        return GasolineType;
    }

    public int getQuantity() {
        return Quantity;
    }
}

    public void findItemName() throws SQLException{
        String selectQuery = "SELECT * FROM parts.parts_store WHERE item_name LIKE ?";
        PreparedStatement statement = connection.prepareStatement(selectQuery);
        statement.setString(1, "%" + tf_Item.getText() + "%"); // Use "%" to search for similar item names

        ResultSet resultSet = statement.executeQuery();

        ArrayList<Item> itemList = new ArrayList<>();

        while (resultSet.next()) {
            String item_Code = resultSet.getString("item_Code");
            String vehicle_code = resultSet.getString("vehicle_code");
            String item = resultSet.getString("item");
            Double unit_price = resultSet.getDouble("unit_price");
            int qty = resultSet.getInt("qty");
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


            if (resultSet.next()) {
                brand = resultSet2.getString("brand");
                model = resultSet2.getString("model");
                model_year = resultSet2.getInt("model_year");
                vehicle_type = resultSet2.getString("vehicle_type");
                gasoline_type = resultSet2.getString("gasoline_type");
            }

            // Retrieve other details as needed

            // Create an Item object and add it to the itemList
            Item items = new Item(vehicle_code,item_Code,brand,model,item,vehicle_type,model_year,unit_price,gasoline_type,qty);
            itemList.add(items);
        }

        if (itemList.isEmpty()) {
            // No items found with similar item names, display an alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Items Found");
            alert.setHeaderText(null);
            alert.setContentText("No items found with similar item names.");
            alert.showAndWait();
        } else {
            // Do something with the retrieved item list
        }
    }
    Item items = new Item(vehicle_code,item_Code,brand,model,item,vehicle_type,model_year,unit_price,gasoline_type,qty);
itemList.add(items);
















        gasolineTypeList.clear();
        // Prepare the select statement
        String selectQuery = "SELECT  gasoline_type FROM parts.vehicle_type WHERE vehicle_code = ?";
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
        temp = resultSet.getString("gasoline_type");
        }

        // Add the quantity to the quantityList
        gasolineTypeList.add(temp);
        }
        } catch (SQLException ex) {
        throw new RuntimeException(ex);
        }
        // Convert ArrayList to HashSet to remove duplicates
        HashSet<String> set = new HashSet<>(gasolineTypeList);

        // Convert HashSet back to ArrayList
        gasolineTypeList = new ArrayList<>(set);






public void findGasolineType() throws SQLException{
        String selectQuery = "SELECT * FROM parts.vehicle_type WHERE gasoline_type LIKE ?";
        PreparedStatement statement = connection.prepareStatement(selectQuery);
        statement.setString(1, "%" + CB_GasolineType.getValue() + "%"); // Use "%" to search for similar item names

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

        String selectQuery2 = "SELECT DISTINCT item_code FROM parts.parts_store WHERE vehicle_code IN (";
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

        ArrayList<String> temp_ItemCodeList = new ArrayList<>();

        while (resultSet2.next()) {
        String vehicleCode = resultSet2.getString("item_code");
        temp_ItemCodeList.add(vehicleCode);
        }
        ArrayList<String> temp = new ArrayList<>();
        for (String i: itemCodeList){
        for (String j: temp_ItemCodeList){
        if (i.trim().equals(j.trim())){
        temp.add(j);
        }
        }
        }
        itemCodeList=temp;

        }

        }














public void findVehicleType() throws SQLException{
        String selectQuery = "SELECT * FROM parts.vehicle_type WHERE vehicle_type LIKE ?";
        PreparedStatement statement = connection.prepareStatement(selectQuery);
        statement.setString(1, "%" + tf_VehicleType.getText() + "%"); // Use "%" to search for similar item names

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

        String selectQuery2 = "SELECT DISTINCT item_code FROM parts.parts_store WHERE vehicle_code IN (";
        for (int i = 0; i < itemCodeList.size(); i++) {
        selectQuery += "?,";
        }
        selectQuery2 = selectQuery2.substring(0, selectQuery2.length() - 1); // Remove the trailing comma
        selectQuery2 += ")";

        PreparedStatement statement2 = connection.prepareStatement(selectQuery2);

        // Set the item codes in the query
        for (int i = 0; i < itemCodeList.size(); i++) {
        statement2.setString(i + 1, itemCodeList.get(i));
        }

        ResultSet resultSet2 = statement2.executeQuery();

        ArrayList<String> temp_ItemCodeList = new ArrayList<>();

        while (resultSet2.next()) {
        String vehicleCode = resultSet2.getString("item_code");
        temp_ItemCodeList.add(vehicleCode);
        }
        ArrayList<String> temp = new ArrayList<>();
        for (String i: itemCodeList){
        for (String j: temp_ItemCodeList){
        if (i.trim().equals(j.trim())){
        temp.add(j);
        }
        }
        }
        itemCodeList=temp;


        }

        }











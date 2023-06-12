package com.example.sinari;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
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

public class MainController {
    public class Main_Table {

        private StringProperty itemCode;
        private StringProperty item;
        private SimpleIntegerProperty qty;
        private SimpleDoubleProperty unitPrice;
        private SimpleDoubleProperty discount;
        private SimpleDoubleProperty price;

        public Main_Table(String itemCode, String item, int qty, Double unitPrice, Double discount,Double price) {
            this.itemCode = new SimpleStringProperty(itemCode);
            this.item = new SimpleStringProperty(item);
            this.qty = new SimpleIntegerProperty(qty);
            this.unitPrice = new SimpleDoubleProperty(unitPrice);
            this.discount = new SimpleDoubleProperty(discount);
            this.price = new SimpleDoubleProperty(price);
        }

        public String getItemCode() {
            return itemCode.get();
        }

        public StringProperty itemCodeProperty() {
            return itemCode;
        }

        public void setItemCode(String itemCode) {
            this.itemCode.set(itemCode);
        }

        public String getItem() {
            return item.get();
        }

        public StringProperty itemProperty() {
            return item;
        }

        public void setItem(String item) {
            this.item.set(item);
        }

        public int getQty() {
            return qty.get();
        }

        public SimpleIntegerProperty qtyProperty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty.set(qty);
        }

        public double getUnitPrice() {
            return unitPrice.get();
        }

        public SimpleDoubleProperty unitPriceProperty() {
            return unitPrice;
        }

        public void setUnitPrice(double unitPrice) {
            this.unitPrice.set(unitPrice);
        }

        public double getDiscount() {
            return discount.get();
        }

        public SimpleDoubleProperty discountProperty() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount.set(discount);
        }

        public double getPrice() {
            return price.get();
        }

        public SimpleDoubleProperty priceProperty() {
            return price;
        }

        public void setPrice(double price) {
            this.price.set(price);
        }
    }

    @FXML
    private Button bt_Add;

    @FXML
    private Button bt_AddItem;

    @FXML
    private Button bt_Delete;

    @FXML
    private Button bt_Edit;

    @FXML
    private Button bt_Search;

    @FXML
    private TableColumn<Main_Table, String> col1_ItemCode;

    @FXML
    private TableColumn<Main_Table, String> col2_Item;

    @FXML
    private TableColumn<Main_Table, Integer> col3_Qty;

    @FXML
    private TableColumn<Main_Table, Double> col4_UnitPrice;

    @FXML
    private TableColumn<Main_Table, Double> col5_Discount;

    @FXML
    private TableColumn<Main_Table, Double> col6_Price;

    @FXML
    private ContextMenu contextMenu_Item;

    @FXML
    private Label lb_AvailableQty;

    @FXML
    private Label lb_Total;

    @FXML
    private Label lb_TotalDiscount;

    @FXML
    private TableView<Main_Table> tb_main;

    @FXML
    private TextField tf_Discount;

    @FXML
    private TextField tf_Item;

    @FXML
    private TextField tf_Qty;
    private String jdbcURL = "jdbc:postgresql://localhost:5410/uniquely_auto_parts";
    private String username = "postgres";
    private String password = "Sinari2000";
    private Connection connection = DriverManager.getConnection(jdbcURL, username, password);
    private ArrayList<String> itemCodes = new ArrayList<>();

    public MainController() throws SQLException {
    }
    private int available_quantity;
    private int qty;
    private double discountPercentage;
    private double total;
    private double price;
    private String itemName;
    private double totalDiscount;
    private double discount;
    private  double itemsPrice;
    private double discountPrice;
    private double tempDiscountPercentage;
    private int tempQty;
    private int temp_quantity;

    public  void initialize (){

        try {
            String sql = "SELECT item_code FROM parts.parts_store";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String itemCode = resultSet.getString("item_code");
                itemCodes.add(itemCode);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Can't load item code list.");
        }

        // Set up auto-completion to vehicle code text box
        tf_Item.textProperty().addListener((observable, oldValue, newValue) -> {
            contextMenu_Item.getItems().clear();
            if (!newValue.isEmpty()) {
                for (String code : itemCodes) {
                    if (code.startsWith(newValue)) {
                        MenuItem suggestionItem = new MenuItem(code);
                        suggestionItem.setOnAction(event -> {
                            tf_Item.setText(suggestionItem.getText());
                            tf_Item.positionCaret(suggestionItem.getText().length());
                            contextMenu_Item.hide();
                        });
                        contextMenu_Item.getItems().add(suggestionItem);
                    }
                }
                if (!contextMenu_Item.getItems().isEmpty()) {
                    contextMenu_Item.show(tf_Item, Side.BOTTOM, 0, 0);
                } else {
                    contextMenu_Item.hide();
                }
            } else {
                contextMenu_Item.hide();
            }
        });

        tf_Item.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!tf_Item.getText().isEmpty()) {
                    boolean goNext_textBox =false;
                    for (int i=0;i<=itemCodes.size()-1;i++) {
                        if (tf_Item.getText().trim().equals(itemCodes.get(i).trim())) {
                            goNext_textBox=true;
                            break;
                        }
                    }
                    if (goNext_textBox){

                        check_availableQty();

                        tf_Qty.requestFocus();

                    }else {
                        tf_Item.clear();
                        tf_Item.requestFocus();
                    }

                }
            }

        });

//        tf_Qty.setOnMouseClicked(e -> check_availableQty());
        tf_Qty.setOnKeyPressed((KeyEvent event) -> {

            if (event.getCode() == KeyCode.ENTER) {

                try{
                    qty = Integer.parseInt(tf_Qty.getText());
                }catch (Exception e){
                    tf_Qty.clear();
                    tf_Qty.requestFocus();
                }
                if (!tf_Qty.getText().isEmpty() && qty<=available_quantity && qty!=0) {
                    tf_Discount.requestFocus();
                }
            }
        });
        tf_Discount.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {

                try{
                    discountPercentage = Double.parseDouble(tf_Discount.getText());
                }catch (Exception e){
                    tf_Discount.clear();
                    tf_Discount.requestFocus();
                }
                if (!tf_Discount.getText().isEmpty()) {
                    Add_to_Database();
                }
            }
        });

        //edit item
        tb_main.setOnMouseClicked(event -> {
            // Get the selected item
            MainController.Main_Table selectedItem = tb_main.getSelectionModel().getSelectedItem();

            if (selectedItem != null) {
                // Populate the item's properties into the input fields
                tf_Item.setText(selectedItem.getItemCode());
                temp_quantity=selectedItem.getQty();
                check_availableQty();
                available_quantity =Integer.parseInt(lb_AvailableQty.getText());
                lb_AvailableQty.setText(String.valueOf(available_quantity));
                tf_Qty.setText(String.valueOf(selectedItem.getQty()));
                tf_Discount.setText(String.valueOf(selectedItem.getDiscount()));
                tempDiscountPercentage= selectedItem.getDiscount();
                tempQty = selectedItem.getQty();

            }
        });


    }
    public void check_availableQty(){
        try  {
            String sql = "SELECT qty FROM parts.parts_store WHERE item_code = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, tf_Item.getText());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                available_quantity = rs.getInt("qty");
                lb_AvailableQty.setText(Integer.toString(available_quantity));
            } else {
                lb_AvailableQty.setText("--");
            }

        } catch (SQLException e) {
            System.out.println("Failed to check item quantity: " + e.getMessage());
        }
    }




    public void Add_to_Database(){

        if (tf_Item.getText().isEmpty()) {
            tf_Item.requestFocus();
            return;
        }else {
            boolean goNext_textBox =false;
            for (int i=0;i<=itemCodes.size()-1;i++) {
                if (tf_Item.getText().trim().equals(itemCodes.get(i).trim())) {
                    goNext_textBox=true;
                    break;
                }
            }
            if (goNext_textBox){
                tf_Qty.requestFocus();
            }else {
                tf_Item.requestFocus();
                return;
            }
        }
        if((available_quantity-qty)<=0||(available_quantity-Integer.parseInt(tf_Qty.getText()))<=0) {
            tf_Qty.requestFocus();
            return;
        }
        if (tf_Qty.getText().isEmpty() || qty==0 || qty>available_quantity){
            try{
                qty = Integer.parseInt(tf_Qty.getText());
                if (qty==0){
                    tf_Qty.clear();
                    tf_Qty.requestFocus();
                    return;
                }
            }catch (Exception e){
                tf_Qty.clear();
                tf_Qty.requestFocus();
                return;
            }

        }
        if (tf_Discount.getText().isEmpty() ){
            try{
                discountPercentage = Double.parseDouble(tf_Discount.getText());

            }catch (Exception e){
                tf_Discount.clear();
                tf_Discount.requestFocus();
                return;
            }

        }

        available_quantity-=qty;

        //update qty to data base

        try {

            // Prepare the update statement
            String updateQuery = "UPDATE parts.parts_store SET qty = ? WHERE item_code = ?";
            PreparedStatement statement = connection.prepareStatement(updateQuery);
            statement.setInt(1, available_quantity);
            statement.setString(2, tf_Item.getText());

            // Execute the update statement
            int rowsUpdated = statement.executeUpdate();

            // Check if the update was successful
            if (rowsUpdated > 0) {
                System.out.println("Quantity updated successfully");
            } else {
                System.out.println("Failed to update quantity");
                return;
            }
            // Close the statement and connection
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //get price from database

        try {

            // Prepare the select statement
            String selectQuery = "SELECT unit_price FROM parts.parts_store WHERE item_code = ?";
            PreparedStatement statement = connection.prepareStatement(selectQuery);
            statement.setString(1, tf_Item.getText());

            // Execute the select statement
            ResultSet resultSet = statement.executeQuery();

            // Check if the result set has any rows
            if (resultSet.next()) {
                // Retrieve the price from the result set
                price = resultSet.getDouble("unit_price");
//                System.out.println("Item Price: $" + price);
            } else {
                System.out.println("Item not found to add price");
                return;
            }

            // Close the result set, statement, and connection
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("fail to find price.");
            return;
        }

        //get Item Name from database

        try {

            // Prepare the select statement
            String selectQuery = "SELECT item FROM parts.parts_store WHERE item_code = ?";
            PreparedStatement statement = connection.prepareStatement(selectQuery);
            statement.setString(1, tf_Item.getText());

            // Execute the select statement
            ResultSet resultSet = statement.executeQuery();

            // Check if the result set has any rows
            if (resultSet.next()) {
                // Retrieve the price from the result set
                itemName = resultSet.getString("item");
//                System.out.println("Item Price: $" + price);
            } else {
                System.out.println("Item not found to add Item Name");
                return;
            }

            // Close the result set, statement, and connection
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("fail to find item name.");
            return;
        }
        discountPercentage=Double.parseDouble(tf_Discount.getText());

        discountPrice = price*(discountPercentage/100);
        itemsPrice=(price-discountPrice)*qty;
        // Create a DecimalFormat object with the desired format pattern
        DecimalFormat df = new DecimalFormat("#.##");

        // Use the format() method to round the number
        String roundedNumber_itemsPrice = df.format(itemsPrice);
        String roundedNumber_discountPrice = df.format(discountPrice);



        totalDiscount+=discountPrice;
        total+=itemsPrice;
        String roundedNumber_total = df.format(total);
        String roundedNumber_totalDiscount = df.format(totalDiscount);

        lb_Total.setText(String.valueOf(roundedNumber_total));
        lb_TotalDiscount.setText(String.valueOf(roundedNumber_totalDiscount));


        col1_ItemCode.setCellValueFactory(cellData -> cellData.getValue().itemCodeProperty());
        col2_Item.setCellValueFactory(cellData -> cellData.getValue().itemProperty());
        col3_Qty.setCellValueFactory(cellData -> cellData.getValue().qtyProperty().asObject());
        col4_UnitPrice.setCellValueFactory(cellData -> cellData.getValue().unitPriceProperty().asObject());
        col5_Discount.setCellValueFactory(cellData -> cellData.getValue().discountProperty().asObject());
        col6_Price.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());


        MainController.Main_Table addItems = new MainController.Main_Table(tf_Item.getText(),itemName,qty,price,Double.parseDouble(roundedNumber_discountPrice),Double.parseDouble(roundedNumber_itemsPrice));
        tb_main.getItems().add(addItems);

        available_quantity =0;
        qty=0;
        price=0;
        itemName=null;
        itemsPrice=0;
        discountPrice=0;




        tf_Item.clear();
        tf_Qty.clear();
        lb_AvailableQty.setText("");

        tf_Item.requestFocus();



    }

    @FXML
    private void editSelectedRow(ActionEvent event) {
        if (tf_Item.getText().isEmpty()) {
            tf_Item.requestFocus();
            return;
        }else {
            boolean goNext_textBox =false;
            for (int i=0;i<=itemCodes.size()-1;i++) {
                if (tf_Item.getText().trim().equals(itemCodes.get(i).trim())) {
                    goNext_textBox=true;
                    break;
                }
            }
            if (goNext_textBox){
                tf_Qty.requestFocus();
            }else {
                tf_Item.requestFocus();
                return;
            }
        }
        if (tf_Qty.getText().isEmpty() || qty==0 || qty>available_quantity || Integer.parseInt(tf_Qty.getText())==0){
            try{
                qty = Integer.parseInt(tf_Qty.getText());
                if (qty==0){
                    tf_Qty.clear();
                    tf_Qty.requestFocus();
                    return;
                }
            }catch (Exception e){
                tf_Qty.clear();
                tf_Qty.requestFocus();
                return;
            }

        }
        if (tf_Discount.getText().isEmpty() ){
            try{
                discountPercentage = Double.parseDouble(tf_Discount.getText());

            }catch (Exception e){
                tf_Discount.clear();
                tf_Discount.requestFocus();
                return;
            }

        }

        available_quantity += (temp_quantity-(Integer.parseInt(tf_Qty.getText())));

        //update qty to data base

        try {

            // Prepare the update statement
            String updateQuery = "UPDATE parts.parts_store SET qty = ? WHERE item_code = ?";
            PreparedStatement statement = connection.prepareStatement(updateQuery);
            statement.setInt(1, available_quantity);
            statement.setString(2, tf_Item.getText());

            // Execute the update statement
            int rowsUpdated = statement.executeUpdate();

            // Check if the update was successful
            if (rowsUpdated > 0) {
                System.out.println("Quantity updated successfully");
            } else {
                System.out.println("Failed to update quantity");
                return;
            }
            // Close the statement and connection
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        //get Item Name from database

        try {

            // Prepare the select statement
            String selectQuery = "SELECT item FROM parts.parts_store WHERE item_code = ?";
            PreparedStatement statement = connection.prepareStatement(selectQuery);
            statement.setString(1, tf_Item.getText());

            // Execute the select statement
            ResultSet resultSet = statement.executeQuery();

            // Check if the result set has any rows
            if (resultSet.next()) {
                // Retrieve the price from the result set
                itemName = resultSet.getString("item");
//                System.out.println("Item Price: $" + price);
            } else {
                System.out.println("Item not found to add Item Name");
                return;
            }

            // Close the result set, statement, and connection
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("fail to find item name.");
            return;
        }
        //get price from database

        try {

            // Prepare the select statement
            String selectQuery = "SELECT unit_price FROM parts.parts_store WHERE item_code = ?";
            PreparedStatement statement = connection.prepareStatement(selectQuery);
            statement.setString(1, tf_Item.getText());

            // Execute the select statement
            ResultSet resultSet = statement.executeQuery();

            // Check if the result set has any rows
            if (resultSet.next()) {
                // Retrieve the price from the result set
                price = resultSet.getDouble("unit_price");
//                System.out.println("Item Price: $" + price);
            } else {
                System.out.println("Item not found to add price");
                return;
            }

            // Close the result set, statement, and connection
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("fail to find price.");
            return;
        }
        double temp = price*(tempDiscountPercentage/100);
        totalDiscount -= temp;
        total -= ((price-temp)*tempQty);

        discountPrice = price*(discountPercentage/100);
        itemsPrice=(price-discountPrice)*qty;

        totalDiscount+=discountPrice;
        total+=itemsPrice;

        lb_Total.setText(String.valueOf(total));
        lb_TotalDiscount.setText(String.valueOf(totalDiscount));




        // Get the selected item
        MainController.Main_Table selectedItem = tb_main.getSelectionModel().getSelectedItem();



        if (selectedItem != null) {
            // Update the item with the modified values
            selectedItem.setItemCode(tf_Item.getText());
            selectedItem.setItem(itemName);
            selectedItem.setQty(Integer.parseInt(tf_Qty.getText()));
            selectedItem.setUnitPrice(price);
            selectedItem.setDiscount(Double.parseDouble(tf_Discount.getText()));
            selectedItem.setPrice(price*qty);

            // Refresh the table to reflect the changes
            tb_main.refresh();
        }

        available_quantity =0;
        qty=0;
        price=0;
        itemName=null;
        itemsPrice=0;
        discountPrice=0;
        tempQty=0;
        tempDiscountPercentage=0.0;

        tf_Item.clear();
        tf_Qty.clear();
        lb_AvailableQty.setText("");

        tf_Item.requestFocus();


    }


    @FXML
    private void removeSelectedRow(ActionEvent event) {
        // Get the selected item
        MainController.Main_Table selectedItem = tb_main.getSelectionModel().getSelectedItem();

        // Check if an item is selected
        if (selectedItem != null) {
            // Remove the selected item from the data source
            tb_main.getItems().remove(selectedItem);
        }

        //get available Qty


        String sql = tf_Item.getText();
        available_quantity = Integer.parseInt(lb_AvailableQty.getText())+Integer.parseInt(tf_Qty.getText());
        //update qty to data base


        try {

            // Prepare the update statement
            String updateQuery = "UPDATE parts.parts_store SET qty = ? WHERE item_code = ?";
            PreparedStatement statement = connection.prepareStatement(updateQuery);
            statement.setInt(1, available_quantity);
            statement.setString(2, tf_Item.getText());

            // Execute the update statement
            int rowsUpdated = statement.executeUpdate();

            // Check if the update was successful
            if (rowsUpdated > 0) {
                System.out.println("Quantity updated successfully");
            } else {
                System.out.println("Failed to update quantity");
                return;
            }
            // Close the statement and connection
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }





        available_quantity =0;
        qty=0;
        price=0;
        itemName=null;
        itemsPrice=0;
        discountPrice=0;




        tf_Item.clear();
        tf_Qty.clear();
        lb_AvailableQty.setText("");

        tf_Item.requestFocus();
    }


    public void open_AddItemWindow(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Add_T0_ItemDatabase.fxml"));
            Parent root = fxmlLoader.load();
            Stage addItemStage = new Stage();
            addItemStage.setScene(new Scene(root));
            addItemStage.setTitle("Add Items");
            addItemStage.show();

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

    public void clearTable() {
        tb_main.getItems().clear();
    }

}


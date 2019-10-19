package com.microservices.database;

import com.microservices.controller.OrderController;
import com.microservices.model.Item;
import com.microservices.model.Order;
import com.microservices.model.OrderDTO;
import com.microservices.model.OrderStatus;
import com.mysql.fabric.jdbc.FabricMySQLDriver;
import jdk.internal.jline.internal.Nullable;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;


public class DBHelper {
    private static final String ID = "order_id";
    private static final String BD_NAME = "orders";
    private static final String TABLE_ORDER_INFO = "order_info";
    private static final String TABLE_ORDER_ITEM = "order_item";
    private static final String STATUS = "order_status";
    private static final String TOTAL_AMOUNT = "total_amount";
    private static final String TOTAL_PRICE = "total_cost";
    private static final String USERNAME = "username";
    private static final String ITEM_ID = "item_id";
    private static final String ITEM_NAME = "item_name";
    private static final String ITEM_AMOUNT = "item_amount";
    private static final String ITEM_PRICE = "item_price";

    private static final String URL = "jdbc:mysql://localhost:3306/" + BD_NAME;
    private static final String USER = "root";
    private static final String PASSWORD = "1234";
    private static Connection connection;

    private void getConnection() {
        try {
            Driver driver = new FabricMySQLDriver();
            DriverManager.registerDriver(driver);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Failed to load driver class");
        }
    }

    public OrderDTO changeOrderStatus (int id, OrderStatus status) throws SQLException {
        getConnection();
        Statement statement = connection.createStatement();
        String sql = "UPDATE " + TABLE_ORDER_INFO + " SET " + STATUS + "='" + status + "' WHERE " + ID + "=" + id + ";";
        statement.execute(sql);
        connection.close();
        return new OrderDTO(id, status);
    }

    private ArrayList<Order> getOrder(String sql) throws SQLException {
        getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        ArrayList<Order> orders = new ArrayList<>();
        if (resultSet != null){
            while (resultSet.next()){
                ArrayList<Item> items = new ArrayList<>();
                int id = resultSet.getInt(ID);
                OrderStatus mStatus = OrderStatus.valueOf(resultSet.getString(STATUS));
                float mPrice = resultSet.getFloat(TOTAL_PRICE);
                int mAmount = resultSet.getInt(TOTAL_AMOUNT);
                String mUser = resultSet.getString(USERNAME);
                sql = "SELECT " + ITEM_ID + ", " + ITEM_NAME + ", " + ITEM_AMOUNT + ", " + ITEM_PRICE + " FROM "
                        + TABLE_ORDER_ITEM + " WHERE " + ID + " = " + id;
                Statement another = connection.createStatement();
                ResultSet rs = another.executeQuery(sql);
                if (rs != null) {
                    while (rs.next()){
                        Item item = new Item(rs.getInt(ITEM_ID), rs.getString(ITEM_NAME), rs.getInt(ITEM_AMOUNT),
                                rs.getFloat(ITEM_PRICE));
                        items.add(item);
                    }
                }
                Order order = new Order(id, mStatus, mPrice, mAmount, mUser, items);
                orders.add(order);
            }
        }
        connection.close();
        return orders;
    }

    public ArrayList<Order> getAllOrders() throws SQLException {
        String sql = "SELECT * FROM " + TABLE_ORDER_INFO;
        return getOrder(sql);
    }

    public Order getOrderById(int order_id) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_ORDER_INFO + " WHERE " + ID + " = " + order_id;
        return getOrder(sql).get(0);
    }

    private int createNewOrder(int item_id, int item_amount, String username) throws SQLException {
        getConnection();
        int id = 0;
        Statement statement = connection.createStatement();
        String sql = "INSERT INTO " + TABLE_ORDER_INFO + " (" + STATUS + "," + TOTAL_PRICE + "," + TOTAL_AMOUNT + ","
                + USERNAME + ") VALUES ('Collecting', 0, " + item_amount + ",'"  + username + "')";
        statement.execute(sql);
        sql = "SELECT * FROM " + TABLE_ORDER_INFO + " WHERE " + ID + " = (SELECT MAX(" + ID + " ) FROM "
                + TABLE_ORDER_INFO + ")";
        ResultSet rs = statement.executeQuery(sql);
        if (rs != null) {
            rs.absolute(1);
            id = rs.getInt(ID);
            sql = "INSERT INTO " + TABLE_ORDER_ITEM + " (" + ID + "," + ITEM_ID + "," + ITEM_AMOUNT + ","
                        + ITEM_PRICE + ") VALUES (" + id + ", " + item_id + "," + item_amount + ", 0)";
            statement.execute(sql);
            }
        connection.close();
        return id;
    }

    private void addOneMoreItem(int order_id, int item_id, int item_amount) throws SQLException {
        final Logger log = Logger.getLogger(OrderController.class);
        getConnection();
        Statement statement = connection.createStatement();
        log.info("1");
        String sql = "SELECT * FROM " + TABLE_ORDER_ITEM + " WHERE " + ID + " = " + order_id + " AND " +ITEM_ID + " = "
                + item_id;
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next()) {
           int current_amount = rs.getInt(ITEM_AMOUNT);
           float current_price = rs.getFloat(ITEM_PRICE);
           float new_price = current_price / current_amount;
           int new_amount = current_amount + item_amount;
           new_price *= new_amount;
           sql = "UPDATE " + TABLE_ORDER_ITEM + " SET " + ITEM_AMOUNT + " = " + new_amount + ", " + ITEM_PRICE
                   + " = " + new_price + " WHERE " + ID + "=" + order_id + " AND " + ITEM_ID + "=" + item_id + ";";
           statement.execute(sql);
        } else {
            log.info("j");
            sql = "INSERT INTO " + TABLE_ORDER_ITEM + " (" + ID + "," + ITEM_ID + "," + ITEM_AMOUNT + ","
                    + ITEM_PRICE + ") VALUES (" + order_id + ", " + item_id + ","  + item_amount + ", 0)";
            statement.execute(sql);
        }
        connection.close();
        updateOrderInfo(order_id);
    }

    private void updateOrderInfo(int order_id) throws SQLException {
        getConnection();
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM " + TABLE_ORDER_ITEM + " WHERE " + ID + " = " + order_id;
        ResultSet rs = statement.executeQuery(sql);
        int new_total_amount = 0;
        float new_total_price = 0;
        while (rs.next()){
            new_total_amount += rs.getInt(ITEM_AMOUNT);
            new_total_price += rs.getFloat(ITEM_PRICE);
        }
        sql = "UPDATE " + TABLE_ORDER_INFO + " SET " + TOTAL_AMOUNT + " = " + new_total_amount + ", " + TOTAL_PRICE
                + " = " + new_total_price + " WHERE " + ID + "=" + order_id;
        statement.execute(sql);
        connection.close();
    }

    public int addItemToOrder(@Nullable Integer order_id, int item_id, int item_amount, String username) throws SQLException {
        if (order_id == null) {
            return createNewOrder(item_id,item_amount,username);
        } else {
            addOneMoreItem(order_id, item_id, item_amount);
            return order_id;
        }
    }

    public void decreaseItemAmount(int order_id, int item_id, int item_amount) throws SQLException {
        getConnection();
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM " + TABLE_ORDER_ITEM + " WHERE " + ID + " = " + order_id + " && " +ITEM_ID + " = "
                + item_id;
        ResultSet rs = statement.executeQuery(sql);
        if (rs != null) {
            while (rs.next()){
                float new_price = rs.getFloat(ITEM_PRICE)/rs.getInt(ITEM_AMOUNT);
                int new_amount = rs.getInt(ITEM_AMOUNT) - item_amount;
                new_price *= new_amount;
                sql = "UPDATE " + TABLE_ORDER_ITEM + " SET " + ITEM_AMOUNT + " = " + new_amount + ", " + ITEM_PRICE
                        + " = " + new_price + " WHERE " + ID + "=" + order_id + " AND " + ITEM_ID + "=" + item_id + ";";
                statement.execute(sql);
            }
        }
        connection.close();
        updateOrderInfo(order_id);
    }
}

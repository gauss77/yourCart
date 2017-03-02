/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yourcart.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.yourcart.beans.Cart;

/**
 *
 * @author OsamaPC
 */
public class CartModel extends DbConnection {

    ResultSet rs = null;

    Connection con;

    public ArrayList<Cart> getUserCart(int usrId) {

        try {

            con = openConnection();
            ArrayList<Cart> arr = new ArrayList<>();
            PreparedStatement pst = null;
            pst = con.prepareStatement("select * From cart where user_id=?");
            pst.setInt(1, usrId);
            rs = pst.executeQuery();
            while (rs.next()) {
                Cart cart = new Cart();
                cart.setCartId(rs.getInt("id"));
                cart.setUserId(rs.getInt(2));
                cart.setProductId(rs.getInt(3));
                cart.setQuantity(rs.getInt(4));
                arr.add(cart);
            }
            closeConnection();
            return arr;
        } catch (SQLException ex) {
            closeConnection();
            ex.printStackTrace();
            return null;
        } finally {
            closeConnection();
        }
    }

    public boolean addCart(Cart cart) {

        try {
            int search = search(cart.getProductId(), cart.getUserId());
            if (search != 0) {
                return editQantity(search + cart.getQuantity(), cart.getUserId(), cart.getProductId());
            }
            con = openConnection();
            PreparedStatement pst = null;
            System.out.println("my con" + con);
            pst = con.prepareStatement("insert into cart (id,user_id,product_id,quantity)Values (?,?,?,?)");
            pst.setInt(1, cart.getCartId());
            pst.setInt(2, cart.getUserId());
            pst.setInt(3, cart.getProductId());
            pst.setInt(4, cart.getQuantity());
            int executeUpdate = pst.executeUpdate();
            closeConnection();
            if (executeUpdate > 0) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean deleteCart(int cartID) {
        con = openConnection();
        PreparedStatement pst = null;
        try {
            //System.out.println("my con" + con);
            pst = con.prepareStatement("delete From cart where id=?");
            pst.setInt(1, cartID);
            int executeUpdate = pst.executeUpdate();
            closeConnection();
            if (executeUpdate > 0) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;

    }

    public boolean deleteUserCart(int userID) {
        con = openConnection();
        PreparedStatement pst = null;
        try {
            //System.out.println("my con" + con);
            pst = con.prepareStatement("delete From cart where user_id=?");
            pst.setInt(1, userID);
            int executeUpdate = pst.executeUpdate();
            closeConnection();
            if (executeUpdate > 0) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private int search(int pID, int usrID) {
        boolean b = false;
        int qu = 0;
        try {

            con = openConnection();
            PreparedStatement pst = con.prepareStatement("SELECT * from cart where (id=?) and (user_id=?)");
            pst.setInt(1, pID);
            pst.setInt(2, usrID);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                b = true;
                qu = rs.getInt("quantity");
            } else {
                b = false;
            }
            closeConnection();
        } catch (SQLException ex) {
            closeConnection();
            ex.printStackTrace();
        }
        if (b) {
            return qu;
        }
        return 0;
    }

    private boolean editQantity(int quantity, int usrID, int productID) {
        try {

            con = openConnection();
            PreparedStatement pst = con.prepareStatement("update cart set quantity=? where user_id=? and id=? ");
            pst.setInt(1, quantity);
            pst.setInt(2, usrID);
            pst.setInt(3, productID);

            int x = pst.executeUpdate();
            closeConnection();
            if (x > 0) {
                return true;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            closeConnection();
        }
        return false;
    }

    public int getNubmberOfCartsForUser(int usrID) {

        int count = 0;
        ArrayList<Cart> userCart = getUserCart(usrID);
        for (int i = 0; i < userCart.size(); i++) {
            count += userCart.get(i).getQuantity();
            System.out.println("size ==" + userCart.size());
        }
        System.out.println("Quantity ==  " + count);
        return count;
    }
}
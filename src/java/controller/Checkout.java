/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.User_DTO;
import entity.Address;
import entity.Cart;
import entity.City;
import entity.Order_item;
import entity.Order_status;
import entity.Product;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import model.PayHere;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "Checkout", urlPatterns = {"/Checkout"})
public class Checkout extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject requestJsonObject = gson.fromJson(req.getReader(), JsonObject.class);
      

        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("success", false);

        HttpSession httpsession = req.getSession();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        Boolean isCurrentAddress = requestJsonObject.get("isCurrentAddress").getAsBoolean();
        String fname = requestJsonObject.get("fname").getAsString();
        String lname = requestJsonObject.get("lname").getAsString();
        String line1 = requestJsonObject.get("line1").getAsString();
        String line2 = requestJsonObject.get("line2").getAsString();
        String postalCode = requestJsonObject.get("postalCode").getAsString();
        String mobile = requestJsonObject.get("mobile").getAsString();
        String cityId = requestJsonObject.get("city").getAsString();
        
        if (httpsession.getAttribute("user") != null) {
            //user signed in

            //get user from db
            User_DTO user_DTO = (User_DTO) httpsession.getAttribute("user");
            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));
            User user = (User) criteria1.uniqueResult();

            if (isCurrentAddress) {
                //get current address
                Criteria criteria2 = session.createCriteria(Address.class);
                criteria2.add(Restrictions.eq("user", user));
                criteria2.addOrder(Order.desc("id"));
                criteria2.setMaxResults(1);

                if (criteria2.list().isEmpty()) {
                    //current address not found. create a new address
                    responseJsonObject.addProperty("msg", "Current address not found. please create a new address");

                } else {
                    //get current address found
                    Address address = (Address) criteria2.list().get(0);

                    saveOrder(session, transaction, user, address, responseJsonObject);

                }

            } else {
                //create new address

                if (fname.isEmpty()) {
                    responseJsonObject.addProperty("msg", "Please fill first name");
                } else if (lname.isEmpty()) {
                    responseJsonObject.addProperty("msg", "Please fill last name");
                } else if (cityId.isEmpty()) {
                    responseJsonObject.addProperty("msg", "Please select city");
                } else {

                    Criteria criteria3 = session.createCriteria(City.class);
                    criteria3.add(Restrictions.eq("id", Integer.parseInt(cityId)));

                    if (criteria3.list().isEmpty()) {
                        responseJsonObject.addProperty("msg", "Invalid city");
                    } else {
                        //City found
                        City city = (City) criteria3.list().get(0);

                        if (line1.isEmpty()) {
                            responseJsonObject.addProperty("msg", "Please sfill lline1");
                        } else if (line2.isEmpty()) {
                            responseJsonObject.addProperty("msg", "Please sfill lline2");
                        } else if (postalCode.isEmpty()) {
                            responseJsonObject.addProperty("msg", "Please sfill postal code");
                        } else if (postalCode.length() > 5) {
                            responseJsonObject.addProperty("msg", "Invalid postal code");
                        } else if (mobile.isEmpty()) {
                            responseJsonObject.addProperty("msg", "Please sfill mobile number");
                        } else if (!Validation.isMobileNumberValid(mobile)) {
                            responseJsonObject.addProperty("msg", "inavalid mobile");
                        } else {
                            //create new address

                            Address address = new Address();
                            address.setCity(city);
                            address.setFirst_name(fname);
                            address.setLine1(line1);
                            address.setLine2(line2);
                            address.setLast_name(lname);
                            address.setMobile(mobile);
                            address.setPostal_code(postalCode);
                            address.setUser(user);

                            session.save(address);

                            //***compleate the checkout process
                            saveOrder(session, transaction, user, address, responseJsonObject);
                        }
                    }

                }

            }

        } else {
            //user not
            responseJsonObject.addProperty("msg", "User not signed in");
        }

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseJsonObject));

    }

    private void saveOrder(Session session, Transaction transaction, User user, Address address, JsonObject responseJsonObject) {
        try {

            //***Compleate the chkout process
            //create Order in DB
            entity.Orders order = new entity.Orders();
            order.setAddress(address);
            order.setDate_time(new Date());
            order.setUser(user);

            int order_id  = (int) session.save(order);
            session.save(order);

            //Get Crat Item
            Criteria criteria4 = session.createCriteria(Cart.class);
            criteria4.add(Restrictions.eq("user", user));
            List<Cart> cartList = criteria4.list();

            //get order status from Db
            Order_status order_status = (Order_status) session.get(Order_status.class, 5);

            double amount = 0;
            String item = "";
            
            //create Order Item in db
            for (Cart cart : cartList) {
                
                //Calculate amoint
                amount += (cart.getQty() * cart.getProduct().getPrice()) + cart.getProduct().getShipping();
                
                
                //calculate 
                item += cart.getProduct().getTitle()+" x"+cart.getQty()+" ";

                Product product = cart.getProduct();

                Order_item order_item = new Order_item();
                order_item.setOrder(order);
                order_item.setOrder_status(order_status);
                order_item.setProduct(product);
                order_item.setQty(cart.getQty());
                session.save(order_item);

                product.setQty(product.getQty() - cart.getQty());
                session.update(product);

                session.delete(cart);

            }

            transaction.commit();
            
            String merchant_id = "1228182";
            String formatedAmount = new DecimalFormat("0.00").format(amount);
            String currency = "LKR";
            String merchanSecret = "ODg5MzI2MDk4NDkwNTkwMTc1MjU0NzgzMjgxMjgyMjczMDkyOQ==";
            String merchantSecratMd5Hash = PayHere.generateMD5(merchanSecret);
            

            //paymet gaytway
            JsonObject payhere = new JsonObject();
            payhere.addProperty("sandbox", true);
            payhere.addProperty("merchant_id", merchant_id);
            
            payhere.addProperty("return_url", "");
            payhere.addProperty("cancel_url", "");
            payhere.addProperty("notify_url", "https://https://4f49-175-157-29-149.ngrok-free.app/Evera/VerifyPayments");
            
            payhere.addProperty("first_name", user.getFisrt_name());
            payhere.addProperty("last_name", user.getLast_name());
            payhere.addProperty("email", user.getEmail());
            
            payhere.addProperty("city", "colombo");
            payhere.addProperty("country", "Sri lanka");
            payhere.addProperty("phone", "0766844257");
            payhere.addProperty("address", "hirana,hirana");
            
            
            payhere.addProperty("order_id", String.valueOf(order_id));
           
            payhere.addProperty("items", item);
            payhere.addProperty("amount", formatedAmount);
            payhere.addProperty("currency", currency);
           
            
           String md5Hash = PayHere.generateMD5(merchant_id + order_id + formatedAmount + currency + merchantSecratMd5Hash);
            // Generate md5 hash

            payhere.addProperty("hash", md5Hash);
            System.out.println(md5Hash);

            responseJsonObject.addProperty("success", true);
            responseJsonObject.addProperty("msg", "check put completed");
            
            Gson gson = new Gson();
            responseJsonObject.add("payhereJson", gson.toJsonTree(payhere));

        } catch (Exception e) {
            transaction.rollback();
        }
        

    }

}

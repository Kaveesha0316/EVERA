/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import dto.Cart_DTO;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Cart;
import entity.Product;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Admin
 */
@WebServlet(name = "AddToCart", urlPatterns = {"/AddToCart"})
public class AddToCart extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Response_DTO response_DTO = new Response_DTO();
        Gson gson = new Gson();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {

            String id = request.getParameter("id");
            String qty = request.getParameter("qty");

            if (!Validation.isInteger(id)) {
                response_DTO.setContent("Prdoduct Nit found");
            } else if (!Validation.isInteger(qty)) {
                response_DTO.setContent("Invalid qty");
            } else {

                int productId = Integer.parseInt(id);
                int productQty = Integer.parseInt(qty);

                if (productQty <= 0) {

                } else {

                    Product product = (Product) session.get(Product.class, productId);

                    if (product != null) {
                        //product found

                        if (request.getSession().getAttribute("user") != null) {
                            //DB cart

                            User_DTO user_DTO = (User_DTO) request.getSession().getAttribute("user");
                            
                            

                            //get user
                            Criteria criteria1 = session.createCriteria(User.class);
                            criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));
                            User user = (User) criteria1.uniqueResult();

                            //checl in db cart
                            Criteria criteria2 = session.createCriteria(Cart.class);
                            criteria2.add(Restrictions.eq("user", user));
                            criteria2.add(Restrictions.eq("product", product));

                            if (criteria2.list().isEmpty()) {
                                // item not found in cart

                                if (productQty <= product.getQty()) {

                                    Cart cart = new Cart();
                                    cart.setProduct(product);
                                    cart.setUser(user);
                                    cart.setQty(productQty);

                                    session.save(cart);
                                    transaction.commit();
                                    response_DTO.setContent("product added");
                                    response_DTO.setSuccess(true);
                                } else {
                                    //quentiy not aveilable
                                    response_DTO.setContent("quentiy not aveilable");
                                }
                            } else {
                                //item already found in cart

                                Cart cartItem = (Cart) criteria2.uniqueResult();

                                if (cartItem.getQty() + productQty <= product.getQty()) {

                                    cartItem.setQty(cartItem.getQty() + productQty);
                                    session.update(cartItem);
                                    transaction.commit();
                                    response_DTO.setContent("product qty Upadetd");
                                    response_DTO.setSuccess(true);
                                } else {
                                    ///can't update cart ,iquentiy not aveilable/
                                    response_DTO.setContent("can't update cart ,quentiy not aveilable/");
                                }

                            }
                        } else {
                            //session cart

                            HttpSession httpSession = request.getSession();

                            if (httpSession.getAttribute("sessionCart") != null) {
                                //session cart found
                                ArrayList<Cart_DTO> sessionCart = (ArrayList<Cart_DTO>) httpSession.getAttribute("sessionCart");

                                Cart_DTO foundCart_DTO = null;
                                for (Cart_DTO cart_DTO : sessionCart) {

                                    if (product.getId() == cart_DTO.getProduct().getId()) {
                                        foundCart_DTO = cart_DTO;
                                        break;

                                    }

                                }

                                if (foundCart_DTO != null) {
                                    //product found
                                    if (productQty + foundCart_DTO.getQty() <= product.getQty()) {

                                        foundCart_DTO.setQty(productQty + foundCart_DTO.getQty());

                                        response_DTO.setContent("Product qty Upadted");
                                        response_DTO.setSuccess(true);
                                    } else {
                                        ///can't update cart ,iquentiy not aveilable/
                                        response_DTO.setContent("can't update cart ,quentiy not aveilable/");
                                    }

                                } else {
                                    //not product found
                                    if (productQty <= product.getQty()) {
                                        
                                        Cart_DTO cart_DTO = new Cart_DTO();
                                        cart_DTO.setProduct(product);
                                        cart_DTO.setQty(productQty);
                                        sessionCart.add(cart_DTO);
                                        
                                         response_DTO.setContent("Product  Add");
                                        response_DTO.setSuccess(true);

                                    } else {
                                        response_DTO.setContent("quentiy not aveilable");
                                    }

                                }
                                
                                httpSession.setAttribute("sessionCart", sessionCart);

                            } else {

                                if (productQty <= product.getQty()) {

                                    ArrayList<Cart_DTO> sessionCart = new ArrayList<>();

                                    Cart_DTO cart_DTO = new Cart_DTO();
                                    cart_DTO.setProduct(product);
                                    cart_DTO.setQty(productQty);
                                    sessionCart.add(cart_DTO);

                                    httpSession.setAttribute("sessionCart", sessionCart);

                                    response_DTO.setContent("Product added");
                                     response_DTO.setSuccess(true);
                                } else {
                                    //quentiy not aveilable
                                    response_DTO.setContent("quentiy not aveilable");
                                }
                            }
                        }
                    } else {
                        //not found
                        response_DTO.setContent("Product not found");

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
        System.out.println(response_DTO.getContent());
        session.close();

    }

}

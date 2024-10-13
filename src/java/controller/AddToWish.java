/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import dto.Cart_DTO;
import dto.Response_DTO;
import dto.User_DTO;
import dto.Wish_DTO;
import entity.Cart;
import entity.Product;
import entity.User;
import entity.Wishlist;
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
@WebServlet(name = "AddToWish", urlPatterns = {"/AddToWish"})
public class AddToWish extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Response_DTO response_DTO = new Response_DTO();
        Gson gson = new Gson();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {

            String id = request.getParameter("id");
           

            if (!Validation.isInteger(id)) {
                response_DTO.setContent("Prdoduct Not found");
            }  else {

                int productId = Integer.parseInt(id);
             

               

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
                            Criteria criteria2 = session.createCriteria(Wishlist.class);
                            criteria2.add(Restrictions.eq("user", user));
                            criteria2.add(Restrictions.eq("product", product));

                            if (criteria2.list().isEmpty()) {
                                // item not found in cart


                                    Wishlist wishlist = new Wishlist();
                                    wishlist.setProduct(product);
                                    wishlist.setUser(user);
                                 

                                    session.save(wishlist);
                                    transaction.commit();
                                    response_DTO.setContent("product added");
                                    response_DTO.setSuccess(true);
                                    
                                                            } else{
                                response_DTO.setSuccess(true);
                                response_DTO.setContent("product allready added");
                            }
                        } else {
                            //session cart

                            HttpSession httpSession = request.getSession();

                            if (httpSession.getAttribute("sessionWish") != null) {
                                //session cart found
                                ArrayList<Wish_DTO> sessionWish = (ArrayList<Wish_DTO>) httpSession.getAttribute("sessionWish");

                                Wish_DTO foundWish_DTO = null;
                                for (Wish_DTO wish_DTO : sessionWish) {

                                    if (product.getId() == wish_DTO.getProduct().getId()) {
                                        foundWish_DTO = wish_DTO;
                                        break;

                                    }

                                }

                                if (foundWish_DTO != null) {
                                    //product found
                                  response_DTO.setSuccess(true);
                                        ///can't update cart ,iquentiy not aveilable/
                                        response_DTO.setContent("product allready added");
                                 

                                } else {
                                    //not product found
                                   
                                        
                                        Wish_DTO wish_DTO = new Wish_DTO();
                                        wish_DTO.setProduct(product);
                                    
                                        sessionWish.add(wish_DTO);
                                        
                                         response_DTO.setContent("Product  Add");
                                        response_DTO.setSuccess(true);

                                   

                                }
                                
                                httpSession.setAttribute("sessionWish", sessionWish);

                            } else {

                               

                                    ArrayList<Wish_DTO> sessionWish = new ArrayList<>();

                                    Wish_DTO wish_DTO = new Wish_DTO();
                                    wish_DTO.setProduct(product);
                                    
                                    sessionWish.add(wish_DTO);

                                    httpSession.setAttribute("sessionWish", sessionWish);

                                    response_DTO.setContent("Product added");
                                     response_DTO.setSuccess(true);
                               
                            }
                        }
                    } else {
                        //not found
                        response_DTO.setContent("Product not found");

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

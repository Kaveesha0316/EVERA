/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Product;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 *
 * @author Admin
 */
@WebServlet(name = "checkSignIn", urlPatterns = {"/checkSignIn"})
public class checkSignIn extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        JsonObject jsonObject = new JsonObject();

        Response_DTO response_DTO = new Response_DTO();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        
        Session session = HibernateUtil.getSessionFactory().openSession();
      

        if (request.getSession().getAttribute("user") != null) {
            // already signin

            User_DTO user_dto = (User_DTO) request.getSession().getAttribute("user");
            response_DTO.setContent(user_dto);
            response_DTO.setSuccess(true);
        
        } else {
            //not signin
            response_DTO.setContent("not signin"); 
          
        }
        
        jsonObject.add("response_dto", gson.toJsonTree(response_DTO));
        
                //get last three product
        Criteria criteria1 = session.createCriteria(Product.class);
        criteria1.addOrder(Order.desc("id"));
        criteria1.setMaxResults(3);
        List<Product> productList = criteria1.list();
        
        for (Product product1 : productList) {
            product1.setUser(null);
        }
        
        Gson gson1 = new Gson();
         jsonObject.add("productList", gson1.toJsonTree(productList));
        
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(jsonObject));

    }

}

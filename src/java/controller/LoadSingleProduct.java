/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Response_DTO;

import entity.Product;
import entity.subCategory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Admin
 */
@WebServlet(name = "LoadSingleProduct", urlPatterns = {"/LoadSingleProduct"})
public class LoadSingleProduct extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Gson gson = new Gson();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Response_DTO response_DTO = new Response_DTO();

        try {
            String productId = request.getParameter("id");
           

//            Validation.isEmailValid(productId)
            if (true) {
                Product product = (Product) session.get(Product.class, Integer.parseInt(productId));
                product.getUser().setPassword(null);
                product.getUser().setEmail(null);
                product.getUser().setVerification(null);
               

                Criteria criteria1 = session.createCriteria(subCategory.class);
                criteria1.add(Restrictions.eq("mainCategory", product.getSubCategory().getMainCategory()));
                List<subCategory> subCategoryList = criteria1.list();

                Criteria criteria2 = session.createCriteria(Product.class);
                criteria2.add(Restrictions.in("subCategory", subCategoryList));
                criteria2.add(Restrictions.ne("id", product.getId()));
                criteria2.setMaxResults(4);

                List<Product> productList = criteria2.list();

                for (Product product1 : productList) {
                    product1.getUser().setPassword(null);
                    product1.getUser().setEmail(null);
                    product1.getUser().setVerification(null);

                }

                JsonObject jsonObject = new JsonObject();
                jsonObject.add("product", gson.toJsonTree(product));
                jsonObject.add("productList", gson.toJsonTree(productList));

                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(jsonObject));

            } else {
                response_DTO.setContent("product not fount");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

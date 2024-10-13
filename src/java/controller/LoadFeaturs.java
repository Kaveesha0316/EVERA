/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Response_DTO;

import entity.Sizes;

import entity.User;
import entity.brand;
import entity.mainCategory;
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
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

@WebServlet(name = "LoadFeaturs", urlPatterns = {"/LoadFeaturs"})
public class LoadFeaturs extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Response_DTO response_DTO = new Response_DTO();

        Gson gson = new Gson();

        Session session = HibernateUtil.getSessionFactory().openSession();

        Criteria criteria1 = session.createCriteria(mainCategory.class);
        criteria1.addOrder(Order.asc("name"));
        List<mainCategory> mainCategoryList = criteria1.list();

        Criteria criteria2 = session.createCriteria(subCategory.class);
        criteria2.addOrder(Order.asc("name"));
        List<subCategory> subCategoryList = criteria2.list();

        Criteria criteria3 = session.createCriteria(Sizes.class);
        criteria3.addOrder(Order.asc("name"));
        List<Sizes> SizesList = criteria3.list();
        
        Criteria criteria4 = session.createCriteria(brand.class);
        criteria4.addOrder(Order.asc("id"));
        List<brand> brandList = criteria4.list();
        
        
        
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("mainCategoryList", gson.toJsonTree(mainCategoryList));
        jsonObject.add("subCategoryList", gson.toJsonTree(subCategoryList));
        jsonObject.add("SizesList", gson.toJsonTree(SizesList));
        jsonObject.add("brandList", gson.toJsonTree(brandList));
      
        
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(jsonObject));
       

    }

}


package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import entity.Product;
import entity.Sizes;
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

/**
 *
 * @author Admin
 */
@WebServlet(name = "LoadData", urlPatterns = {"/LoadData"})
public class LoadData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        JsonObject jsonobject = new JsonObject();
        jsonobject.addProperty("success", false);
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        Gson gson = new Gson();
        
        //get category list
        Criteria criteria1 = session.createCriteria(mainCategory.class);
        List<mainCategory> mainCategoryList = criteria1.list();
        jsonobject.add("mainCategoryList",gson.toJsonTree(mainCategoryList));
        
         //get condition list
        Criteria criteria2 = session.createCriteria(subCategory.class);
        List<subCategory> subCategoryList = criteria2.list();
        jsonobject.add("subCategoryList",gson.toJsonTree(subCategoryList));
        
          //get coloer list
        Criteria criteria3 = session.createCriteria(Sizes.class);
        List<Sizes> SizesList = criteria3.list();
        jsonobject.add("SizesList",gson.toJsonTree(SizesList));
        
          //get Storage list
        Criteria criteria4 = session.createCriteria(brand.class);
        List<brand> brandList = criteria4.list();
        jsonobject.add("brandList",gson.toJsonTree(brandList));
        
        //get Product list
        Criteria criteria5 = session.createCriteria(Product.class);
        criteria5.addOrder(Order.desc("id"));
        
        jsonobject.addProperty("allProductCount", criteria5.list().size());
        
        criteria5.setFirstResult(1);
        criteria5.setMaxResults(4);
        
        
        List<Product> productList = criteria5.list();
        
        for (Product product : productList) {
            product.setUser(null);
        }
        jsonobject.add("productList",gson.toJsonTree(productList));
        
          jsonobject.addProperty("success", true);
          response.setContentType("application/json");
        response.getWriter().write(gson.toJson(jsonobject));
        
    }

}

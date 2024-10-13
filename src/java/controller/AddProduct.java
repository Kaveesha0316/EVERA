/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Product;
import entity.Sizes;
import entity.User;
import entity.brand;
import entity.mainCategory;
import entity.productStatus;
import entity.subCategory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.HibernateUtil;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Admin
 */
@MultipartConfig
@WebServlet(name = "AddProduct", urlPatterns = {"/AddProduct"})
public class AddProduct extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String maincategoryId = request.getParameter("maincategoryId");
        String subcategorylId = request.getParameter("subcategorylId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String brandId = request.getParameter("brandId");
        String sizeId = request.getParameter("sizeId");

        String price = request.getParameter("price");
        String qty = request.getParameter("qtyId");
        String shipping = request.getParameter("shippingId");

        Part image1 = request.getPart("image1");
        Part image2 = request.getPart("image2");
        Part image3 = request.getPart("image3");

        Gson gson = new Gson();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Response_DTO response_DTO = new Response_DTO();

        if (title.isEmpty()) {
            response_DTO.setContent("Please fill Title");
        }else if (price.isEmpty()) {
            response_DTO.setContent("Please fill price");
        } else if (!Validation.isDouble(price)) {
            response_DTO.setContent("Invalid price");
        }else if (qty.isEmpty()) {
            response_DTO.setContent("Please fill quantity");
        } else if (!Validation.isInteger(qty)) {
            response_DTO.setContent("Invalid quantity");
        }else if (shipping.isEmpty()) {
            response_DTO.setContent("Please fill shipping");
        }else if (!Validation.isDouble(shipping)) {
            response_DTO.setContent("Invalid shipping");
        } else if (maincategoryId.equals("0")) {
            response_DTO.setContent("Please select main category");
        } else if (subcategorylId.equals("0")) {
            response_DTO.setContent("Please select  sub category");
        }  else if (brandId.equals("0")) {
            response_DTO.setContent("Please select brand");
        } else if (sizeId.equals("0")) {
            response_DTO.setContent("Please select Size");
        }  else if (description.isEmpty()) {
            response_DTO.setContent("Please fill description");
        }   else if (image1.getSubmittedFileName() == null) {
            response_DTO.setContent("Please selsect Image 1");
        } else if (image2.getSubmittedFileName() == null) {
            response_DTO.setContent("Please selsect Image 2");
        } else if (image3.getSubmittedFileName() == null) {
            response_DTO.setContent("Please selsect Image 3");
        } else {

            mainCategory maincategory = (mainCategory) session.load(mainCategory.class, Integer.parseInt(maincategoryId));

            if (maincategory == null) {
                response_DTO.setContent("Please select a valid main category");
            } else {

                subCategory subcategory = (subCategory) session.load(subCategory.class, Integer.parseInt(subcategorylId));

                if (subcategory == null) {
                    response_DTO.setContent("Please select a valid sub category");
                } else {

                    brand brand = (brand) session.load(brand.class, Integer.parseInt(brandId));

                    if (brand == null) {
                        response_DTO.setContent("Please select a valid brand");
                    } else {

                        Sizes size = (Sizes) session.load(Sizes.class, Integer.parseInt(sizeId));

                        if (size == null) {
                            response_DTO.setContent("Please select a valid size");
                        } else {

                            Product product = new Product();
                            product.setBrand(brand);

                            product.setDate_time(new Date());
                            product.setDescription(description);
                            product.setSubCategory(subcategory);
                            product.setPrice(Double.parseDouble(price));
                            product.setShipping(Double.parseDouble(shipping));
                            
                            productStatus product_status = (productStatus) session.load(productStatus.class, 1);

                            product.setProduct_status(product_status);
                            product.setQty(Integer.parseInt(qty));
                            product.setSizes(size);
                            product.setTitle(title);

                            User_DTO user_DTO = (User_DTO) request.getSession().getAttribute("user");
                            Criteria criteria1 = session.createCriteria(User.class);
                            criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));

                            User user = (User) criteria1.uniqueResult();

                            product.setUser(user);

                            int id = (int) session.save(product);

                            session.beginTransaction().commit();
                            


                            String applicationPath = request.getServletContext().getRealPath("");
                            String newApplicationPath = applicationPath.replace("build" + File.separator + "web", "web");
//                            String newApplicationPath = applicationPath.replace("build" + File.separator + "web", "web");

                            File folder = new File(newApplicationPath + "//product_images//" + id);

                            folder.mkdir();

                            File file1 = new File(folder, "image1.png");
                            InputStream inputStream1 = image1.getInputStream();
                            Files.copy(inputStream1, file1.toPath(), StandardCopyOption.REPLACE_EXISTING);

                            File file2 = new File(folder, "image2.png");
                            InputStream inputStream2 = image2.getInputStream();
                            Files.copy(inputStream2, file2.toPath(), StandardCopyOption.REPLACE_EXISTING);

                            File file3 = new File(folder, "image3.png");
                            InputStream inputStream3 = image3.getInputStream();
                            Files.copy(inputStream3, file3.toPath(), StandardCopyOption.REPLACE_EXISTING);

                            response_DTO.setContent("New Prduct added");
                            response_DTO.setSuccess(true);

                        }

                    }

                }

            }

        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
        System.out.println(response_DTO.getContent());
        session.close();
    }

}

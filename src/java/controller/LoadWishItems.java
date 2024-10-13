/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import dto.Cart_DTO;
import dto.User_DTO;
import dto.Wish_DTO;
import entity.Cart;
import entity.Product;
import entity.User;
import entity.Wishlist;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Admin
 */
@WebServlet(name = "LoadWishItems", urlPatterns = {"/LoadWishItems"})
public class LoadWishItems extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        HttpSession httpSession = request.getSession();
        ArrayList<Wish_DTO> Wish_DTO_List = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
// meeeeee kedi thamai mn cart ekat add krpu ekk thava kenek buy kamla eeke qty eka ivarai nm methanadi validat krnna puluvn
            if (httpSession.getAttribute("user") != null) {
                //DB wish
                User_DTO user_DTO = (User_DTO) httpSession.getAttribute("user");

                Criteria criteria1 = session.createCriteria(User.class);
                criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));
                User user = (User) criteria1.uniqueResult();

                Criteria criteria2 = session.createCriteria(Wishlist.class);
                criteria2.add(Restrictions.eq("user", user));

                List<Wishlist> wishList = criteria2.list();

                for (Wishlist wishLists : wishList) {

                    Wish_DTO wishDTO = new Wish_DTO();
                    Product product = wishLists.getProduct();
                    product.setUser(null);
                    wishDTO.setProduct(product);
                  

                    Wish_DTO_List.add(wishDTO);
                }

            } else {
                //Session wish

                if (httpSession.getAttribute("sessionWish") != null) {

                    Wish_DTO_List = (ArrayList<Wish_DTO>) httpSession.getAttribute("sessionWish");

                    for (Wish_DTO Wish_DTO : Wish_DTO_List) {

                        Wish_DTO.getProduct().setUser(null);
                    }

                } else {
                    //car is emoty
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(Wish_DTO_List));

        session.close();
    }
}

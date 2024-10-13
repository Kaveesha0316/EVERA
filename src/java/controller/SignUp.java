/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.Response_DTO;
import dto.User_DTO;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import model.Mail;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Admin
 */
@WebServlet(name = "SignUp", urlPatterns = {"/SignUp"})
public class SignUp extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Response_DTO response_DTO = new Response_DTO();

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        User_DTO user_DTO = gson.fromJson(request.getReader(), User_DTO.class);

        if (user_DTO.getFisrt_name().isEmpty()) {
            response_DTO.setContent("Please enter First Name");
        } else if (user_DTO.getLast_name().isEmpty()) {
            response_DTO.setContent("Please enter Last Name");
        } else if (user_DTO.getEmail().isEmpty()) {
            response_DTO.setContent("Please enter Email");
        } else if (!Validation.isEmailValid(user_DTO.getEmail())) {
            response_DTO.setContent("Please enter valid email");
        } else if (user_DTO.getPassword().isEmpty()) {
            response_DTO.setContent("Please enter Password");
        } else if (!Validation.isPasswordValid(user_DTO.getPassword())) {
            response_DTO.setContent("Password must include ate leat one uppercase letter, number,special charactor and be at least eight chracters long.");
        } else {

            Session session = HibernateUtil.getSessionFactory().openSession();

            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));

            if (!criteria1.list().isEmpty()) {
                response_DTO.setContent("this email all ready exixt");
            } else {

                int code = (int) (Math.random() * 10000000);

                User user = new User();
                user.setEmail(user_DTO.getEmail());
                user.setFisrt_name(user_DTO.getFisrt_name());
                user.setLast_name(user_DTO.getLast_name());
                user.setPassword(user_DTO.getPassword());
                user.setVerification(String.valueOf(code));
                user.setSubscription(0);

                Thread mailThread = new Thread() {
                    @Override
                    public void run() {
                        Mail.sendMail(user_DTO.getEmail(),
                                "Evara Registration Verification Code",
                                "<div style=\"font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;\">"
                                + "    <div style=\"max-width: 600px; margin: 0 auto; background-color: white; padding: 20px; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\">"
                                + "        <h2 style=\"text-align: center; color: #4CAF50;\">Welcome to Evara!</h2>"
                                + "        <p style=\"font-size: 16px; color: #333;\">Dear User,</p>"
                                + "        <p style=\"font-size: 16px; color: #333;\">Thank you for registering with Evara. To complete your registration, please use the verification code below:</p>"
                                + "        <h1 style=\"text-align: center; color: #1E90FF; background-color: #f0f8ff; padding: 15px; border-radius: 8px;\">"
                                + "            " + user.getVerification() + ""
                                + "        </h1>"
                                + "        <p style=\"font-size: 16px; color: #333; text-align: center;\">This code is valid for the next 15 minutes. Please do not share it with anyone.</p>"
                                + "        <hr style=\"border: none; border-top: 1px solid #ddd; margin: 20px 0;\">"
                                + "        <p style=\"font-size: 14px; color: #999; text-align: center;\">If you did not request this code, please ignore this email.</p>"
                                + "        <p style=\"font-size: 14px; color: #999; text-align: center;\">Evara Support Team</p>"
                                + "    </div>"
                                + "</div>"
                        );

                    }

                };

                mailThread.start();

                session.save(user);
                session.beginTransaction().commit();

                request.getSession().setAttribute("email", user_DTO.getEmail());
                response_DTO.setSuccess(true);
                response_DTO.setContent("Registration success");

            }

            session.close();

        }
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
        System.out.println(gson.toJson(response_DTO));

    }

}


package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.util.ArrayList;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

      //Get current userService and login information
      UserService userService = UserServiceFactory.getUserService();
      String loggedIn = userService.isUserLoggedIn() + "";
      String logInLink = userService.createLoginURL("/");
      String logOutLink = userService.createLogoutURL("/");
      String nickname = (userService.isUserLoggedIn() ? userService.getCurrentUser().getNickname() : "");

      //Store login information as list object
      ArrayList<String> logInfo = new ArrayList<String>();
      logInfo.add(loggedIn);
      logInfo.add(logInLink);
      logInfo.add(logOutLink);
      logInfo.add(nickname);

      //Convert login information to json and return
      Gson gson = new Gson();
      response.setContentType("application/json;");
      response.getWriter().println(gson.toJson(logInfo));
  }
}

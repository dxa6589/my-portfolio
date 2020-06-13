// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  
  private ArrayList<String> quotes;

  @Override
  public void init() {
    quotes = new ArrayList<>();/*
    quotes.add("Hello world!");
    quotes.add("That's rough buddy");
    quotes.add("She didn't say you did this, she said Hugh did this");
    quotes.add("The grass is not always greener on the other side, it's greener where you water it");
    quotes.add("The krusty krab pizza is the pizza for you and me");
    quotes.add("What the foop? It's time to go girl");
    quotes.add("It's the quenchiest!");*/
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    /*Query query = new Query("Comment");

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    //quotes List<Task> tasks = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      String value = (String) entity.getProperty("value");
      quotes.add(value);
    }*/

    Gson gson = new Gson();
    String json = gson.toJson(this.quotes);

    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    // Get the input from the form.
    String comment = request.getParameter("comment");
    quotes.add(comment);

    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("value", comment);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    // Redirect back to the HTML page.
    response.sendRedirect("/index.html");
  }
}

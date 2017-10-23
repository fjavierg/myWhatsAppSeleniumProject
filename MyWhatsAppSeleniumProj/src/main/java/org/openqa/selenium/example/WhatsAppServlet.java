package org.openqa.selenium.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/WhatsApp")

public class WhatsAppServlet extends HttpServlet {

    static String PAGE_HEADER = "<html><head /><body>";
    static String PAGE_FOOTER = "</body></html>";

    @Inject
    WhatsApp myWhatsApp;


    
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req,resp);
    }
    
   protected void doGet(HttpServletRequest req, HttpServletResponse resp)
         throws ServletException, IOException {

       String nb = req.getParameter("nb");
       String msg = req.getParameter("msg");
       PrintWriter writer = resp.getWriter();       
      

      myWhatsApp.send(nb, msg);

      writer.println(PAGE_HEADER);
      writer.println("<h1>" + "OK done"
           + "</h1>");
      writer.println(PAGE_FOOTER); 
      writer.close();
      
      

   }


}
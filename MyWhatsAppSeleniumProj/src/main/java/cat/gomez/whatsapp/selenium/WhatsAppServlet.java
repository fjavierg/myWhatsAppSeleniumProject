package cat.gomez.whatsapp.selenium;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

@SuppressWarnings("serial")
@WebServlet("/WhatsApp")

public class WhatsAppServlet extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(WhatsAppServlet.class);
    @Resource(lookup = "java:/myJmsTest/MyConnectionFactory")
    ConnectionFactory connectionFactory;

    @Resource(lookup = "java:/myJmsTest/MyQueue")
    Destination destination;
    
    
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req,resp);
    }
    
   protected void doGet(HttpServletRequest req, HttpServletResponse resp)
         throws ServletException, IOException {

       String nb = req.getParameter("nb");
       String msg = req.getParameter("msg");
       PrintWriter writer = resp.getWriter(); 
       
       //
       // Send message using injected object
       //
      /*if (myWhatsApp.send(nb, msg)) {
          writer.println("OK done");
      } else {
          writer.println("Error");
      }*/
      logger.info("New Servlet request : " + req.toString());
      //
      // Send message to JMS queue to be consumed by WhatsAPP message driven EJB
      //
      try {
          //Authentication info can be omitted if we are using in-vm
          QueueConnection connection = (QueueConnection)connectionFactory.createConnection();
          try {
              QueueSession session =
                connection.createQueueSession(false,Session.AUTO_ACKNOWLEDGE);

              try {
                  MessageProducer producer = session.createProducer(destination);

                  try {
                      MapMessage message = session.createMapMessage();
                      message.setString("nb", nb);
                      message.setString("msg", msg);
                      producer.send(message);
                      writer.println("Message sent! ^__^");
                  } finally {
                      producer.close();
                  }
              } finally {
                  session.close();
              }

          } finally {
              connection.close();
          }

      } catch (Exception ex) {
          ex.printStackTrace(writer);
      }
   }

}
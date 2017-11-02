package cat.gomez.whatsapp.selenium;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.jms.*;

@SuppressWarnings("serial")
@WebServlet("/WhatsApp")

public class WhatsAppServlet extends HttpServlet {

/*    @Inject
    WhatsApp myWhatsApp;*/

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
                      TextMessage message = session.createTextMessage(msg);
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
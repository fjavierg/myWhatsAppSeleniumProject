Servlet to send WhatsApp messages

This application provides a HTTP interface to send whatsapp text messages to any destination using a mobile phone connected vit web whatsapp.


Usage

	Deploy war in servlet container (WildFly 10 or Tomcat)
	
	HTTP API http://localhost:8080/MySelenium20Proj/WhatsApp?nb=<nb in international format>&msg=<text message to be sent>	
	or
	Index.html file. Fill in form and click send button
	
	First Chrome execution requires to scan the QR code in web browser with the whatsapp application in the mobile phone.

Details

	Uses Selenium libraries to emulate web client
	Starts a Chrome instance using ChromeDriver and settings stored in C:/user/javier/tmp to reuse cookies and configurations
	Uses WhatsAPI to send a message https://api.whatsapp.com/send?phone="+phoneNb+"&text="+message
	Clicks on send button and send icon to fulfill the sending process
	
Configuration
	Set config.properties values for Selenium Chrome Driver path and Chrome user-data-dir too store chrome settings

Artifacts

	WhatsAppServlet. Servlet that receives the request and calls WhastApp.send method to send the message
	WhatsApp class Constructor to start Chrome browser and send method
	index.html Basic HTML/javascript form send whatsapp messages using HTTP interface. 
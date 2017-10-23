Servlet to send WhatsApp messages


Usage

	http://localhost:8080/MySelenium20Proj/WhatsApp?nb=34644016790&msg=TesttttNooooo
	
	nb = destination mobile number international format
	msg = message to be sent

Details

	Uses Selenium libraries to emulate web client
	Starts a Chrome instance using ChromeDriver and settings stored in C:/user/javier/tmp to reuse cookies and configurations
	Uses WhatsAPI to send a message https://api.whatsapp.com/send?phone="+phoneNb+"&text="+message
	Clicks on send button and send icon to fulfill the sending process

Artifacts

	WhatsAppServlet. Servlet that receives the request and calls WhastApp.send method to send the message
	WhatsApp class Constructor to start Chrome browser and send method
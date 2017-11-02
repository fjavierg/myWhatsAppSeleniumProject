Servlet to send WhatsApp messages

This application provides a HTTP interface to send whatsapp text messages to any destination using a mobile phone connected via web whatsapp.


Usage

	Deploy war in servlet container (WildFly 10 or Tomcat)
	
	HTTP API http://localhost:8080/MySelenium20Proj/WhatsApp?nb=<nb in international format>&msg=<text message to be sent>	(Only for testing, requires deactivation of authentication filter)
	 or
	open Index.html file. Fill in form and click send button
	
	First Chrome execution requires to scan the QR code in web browser with the whatsapp application in the mobile phone.

Details

	Uses Selenium libraries to emulate web client
	Starts a Chrome instance using ChromeDriver and settings stored in C:/user/javier/tmp to reuse cookies and configurations
	Uses WhatsAPI to send a message https://api.whatsapp.com/send?phone="+phoneNb+"&text="+message
	Clicks on send button and send icon to fulfill the sending process using Selenium
	Http API authentication using several schemas based on HMAC signatures in HTTP Authentication header (Strategy design pattern)
	Flexible Users repository (DAO design pattern)
	
Configuration
	Set config.properties values for Selenium Chrome Driver path and Chrome user-data-dir too store chrome settings
	
Authentication schemas:
	torch  Authorization = torch HmacAlgorithm,TimeStampUnixEpochUTC,data(optional),HMAC-SHA64(TimeStampUnixEpochUTC,YourSecret)
						torch SHA256,1503753381,,2F05A80ECA8536AD1AC420C7D7E0BAEDAA1A5C0AFB48BDED2D3C79A123D8E6A1

	sch  Authorization = mysch HmacAlgorithm,TimeStampUnixEpochUTC,UserId,HMAC-SHA64(RequestURI+TimeStampUnixEpochUTC,UserIdSecret)	

Artifacts

	WhatsAppServlet. Servlet that receives the request and calls WhastApp.send method to send the message
	WhatsApp class Constructor to start Chrome browser and send method
	index.html Basic HTML/javascript form send whatsapp messages using HTTP interface.
	Authentication, AuthenticationContext, SchAuthentication and AuthenticationFilter classes for client authentication  
	User, UserDao and UserDaoImpl classes for users persistance,
	CORSFilter, servlet filter for cross-origin resource sharing
	
To DO
	JUnit tests
	Error management
	Clean up javascript client source
	provide PHP and python demo client and libraries
	Implement Users repository,
	
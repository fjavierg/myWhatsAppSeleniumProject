WhatsApp API based on Selenium
========================
Author: J. G�mez

Date: 2017

Version : 1.0

Technologies: JEE, JMS, Servlet, JCA, Selenium

Summary: JEE app to send WhatsApp messages based on Selenium

Target Product: 

What is it?
-----------
HTTP API to send WhatsApp messages based on Selenium

This JEE application provides a HTTP interface to send whatsapp text messages using a mobile phone connected via web whatsapp.


Usage
-----------
Clone project

Deploy ear in JEE container for JMS version (wildfly 10) or Deploy war in servlet container for a non.JMS version

Javascript demo: Open Index.html file. Fill in form and click send button to generate API request

PHP demo. Execute whatsAppClient.php 

First Chrome execution requires to scan the QR code in web browser with the whatsapp application in the mobile phone.

Details
-----------
Uses Selenium libraries to emulate web client

Starts a Chrome instance using ChromeDriver and settings stored in C:/user/javier/tmp to reuse cookies and configurations

Uses WhatsApp API to send a message https://api.whatsapp.com/send?phone="+phoneNb+"&text="+message

Clicks on send button and send icon to fulfill the sending process using Selenium

Http API authentication using several schemas based on HMAC signatures in HTTP Authentication header (Strategy design pattern)

Flexible Users repository (DAO design pattern) 

JMS queues for asynchronous comm.
	
Configuration
-------------
Set config.properties values for Selenium Chrome Driver path and Chrome user-data-dir to store chrome settings
Populate user-jaxb.xml with users data (id and secret phrase)
	
Authentication schemas:

*	torch  Authorization = torch HmacAlgorithm,TimeStampUnixEpochUTC,data(optional),HMAC-SHA64(TimeStampUnixEpochUTC,YourSecret)
						torch SHA256,1503753381,,2F05A80ECA8536AD1AC420C7D7E0BAEDAA1A5C0AFB48BDED2D3C79A123D8E6A1

*	sch  Authorization = mysch HmacAlgorithm,TimeStampUnixEpochUTC,UserId,HMAC-SHA64(RequestURI+TimeStampUnixEpochUTC,UserIdSecret)	

Artifacts
-----------

* WhatsAppServlet. Servlet that receives the request and calls WhastApp.send method to send the message to JMS queue

* WhatsAppMDBean Message driven bean to consume JMS messages

* WhatsApp class Singleton Bean to start Chrome browser, send method using Selenium and destroy browser

* index.html Basic HTML/javascript form to send whatsapp messages using HTTP interface. Implements two authentication schemas

* Authentication, AuthenticationContext, SchAuthentication and AuthenticationFilter filter classes for client authentication  

* User, UserDao and UserDaoImpl classes for users persistance,

* CORSFilter, servlet filter for cross-origin resource sharing
	
To DO
-----------
	JUnit tests
	Error management
	provide python demo client and libraries
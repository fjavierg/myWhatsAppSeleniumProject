/**
 * WhatsApp API javascript client
 * 
 * Implements Send function to generate a signed AJAX request to send a whatsapp text message.
 * HTTP request is digned according to sch authentication schema i.e. "Authorization" header is added
 * with user identifier and URI + timesatamp signed with HMAC SHA256 algorithm usign client secret phrase
 * 
 * @author X. Gómez
 * @version 1.0
 *
 * Requires: 
 */


var USER_ID = "12345678";
var USER_SECRET = "changeit"
var SERVER = "http://localhost:8080";
var URI = "/MySel20Proj/WhatsApp";

function sendWhatsAppMsg(destinationNb, message) {
	
    var XHR = new XMLHttpRequest();
    
    // Define what happens in case of success
    XHR.addEventListener("load", function(event) {alert(event.target.responseText);});
    // Define what happens in case of error
    XHR.addEventListener("error", function(event) {alert('Oups! Something goes wrong.');});
    // Set up our request
    var url = SERVER + URI + "?nb=" + destinationNb + "&msg=" + message;
    var seconds = Math.round((new Date).getTime()/1000);
    
    XHR.open("POST", url);
    //Set autentication auth-schema HmacAlgorithm,TimeStampUnixEpochUTC,Userid,HMAC-SHA256(URI+TimeStamp)
    //Authentication: sch SHA256,1503753381,12345678,2F05A80ECA8536AD1AC420C7D7E0BAEDAA1A5C0AFB48BDED2D3C79A123D8E6A1
    var hmac = CryptoJS.HmacSHA256(URI + seconds.toString(), USER_SECRET).toString();
    XHR.setRequestHeader("Authorization", "sch SHA256," + seconds + "," + USER_ID + "," + hmac);
    // The data sent is what the user provided in the form
    XHR.send();
  }
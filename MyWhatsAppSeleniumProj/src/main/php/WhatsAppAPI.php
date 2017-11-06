<?php

class WhatsAppAPI
{
    //
    // This class sends WhatsApp messages using WhatsApp API.
    //
    //

        const SERVER = 'http://localhost:8080';
        const URI = '/MyWhatsAppSeleniumProj/WhatsApp';
        
        //
        // Searchs for items in wallapop.
        // Input: keyword string, nbItems, orderBy, orderType, category ids, min_price, max_price, longitude and latitude
        //        orderBy [creationDate|distance|salePrice]
        //        orderType [des|asc]
        // Returns data object (see bellow)
        //
        //
        public function send($destinationNb, $message, $userId, $userSecret){
                    
            $url = self::SERVER . self::URI . "?nb=" . $destinationNb . "&msg=" . $message;
            $seconds = time();
            $hmac = hash_hmac('sha256',self::URI . strval($seconds),$userSecret);
                       
            $ch = curl_init();
            curl_setopt($ch, CURLOPT_HTTPHEADER, array("Authorization: sch SHA256,$seconds,$userId,$hmac"));
            curl_setopt($ch, CURLOPT_URL, $url);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
            $data = curl_exec($ch);
            
            if (curl_errno($ch) == 0) {
                return $data;
            } else{
                return "Error";
            }
            
            curl_close($ch);
        
        }
}


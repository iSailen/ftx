import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Arrays;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class GetOrderBook {

    static final String URL_FTX = "https://ftx.com/api/markets/BTC-PERP/orderbook?depth=10";

    public static void main(String[] args) throws NoSuchAlgorithmException {

         // HttpHeaders
        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
        // Request to return JSON format
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("FTX-KEY", "FbfMrTN6iYN6gP38hJKf2P4EjCPqqEwKkSI15i_U");

        String secret = "VBL44OGWyfMtDD_Zfhbn0HghDdb6JqUKBJ97Punc";
        long now = Instant.now().toEpochMilli();
        String req = "GET/api/orders";
        String message = String.valueOf(now)+String.valueOf(req);

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        try {
            sha256_HMAC.init(secret_key);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        String signature = Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));
        System.out.println(message);
        System.out.println(String.valueOf(signature).toLowerCase());
        headers.set("FTX-SIGN", String.valueOf(signature).toLowerCase());

        headers.set("FTX-TS", String.valueOf(now));

        // HttpEntity<String>: To get result as String.
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        // RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Send request with GET method, and Headers.
        ResponseEntity<String> response = restTemplate.exchange(URL_FTX, //
                HttpMethod.GET, entity, String.class);

        String result = response.getBody();

        System.out.println(result);



    }

}







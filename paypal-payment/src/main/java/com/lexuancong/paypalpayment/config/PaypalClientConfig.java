package com.lexuancong.paypalpayment.config;



import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "paypal.client")
@Getter
@Setter
public class PaypalClientConfig {

    private String clientId;
    private String clientSecret;
    @Value("${paypal.mode}")
    private String mode;

    // bean đại diện cho client khi thao tác với sdk paypal nhu gọi api
    @Bean
    public PayPalHttpClient payPalHttpClient() {
         if(this.mode.equals("sanbox")){
             return new PayPalHttpClient( new PayPalEnvironment.Sandbox(this.clientId, this.clientSecret));
         }
         return new PayPalHttpClient(new PayPalEnvironment.Live(this.clientId, this.clientSecret));
    }




}


//@Configuration
//@ConfigurationProperties(prefix = "paypal")
//public class PaypalConfig {
//    private String mode;
//    private Client client;
//
//    public static class Client {
//        private String clientId;
//        private String secret;
//        // getters/setters
//    }
//
//    // getters/setters
//}

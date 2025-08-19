package com.lexuancong.paypalpayment.config;


import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.paypal.sdk.Environment;
import com.paypal.sdk.PaypalServerSdkClient;
import com.paypal.sdk.authentication.ClientCredentialsAuthModel;
import com.paypal.sdk.models.OAuthToken;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.event.Level;
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

    // bean đại diện cho client khi thao tác với sdk paypal nhu gọi api cho kiểu cũ
    @Bean
    public PayPalHttpClient payPalHttpClient() {
        if (this.mode.equals("sanbox")) {
            return new PayPalHttpClient(new PayPalEnvironment.Sandbox(this.clientId, this.clientSecret));
        }
        return new PayPalHttpClient(new PayPalEnvironment.Live(this.clientId, this.clientSecret));
    }


    // sdk kiểu mới của sdk => mới phát triển

    @Bean
    public PaypalServerSdkClient paypalClient() {
        return new PaypalServerSdkClient.Builder()
                .loggingConfig(builder -> builder
                        .level(Level.DEBUG)
                        .requestConfig(logConfigBuilder -> logConfigBuilder.body(true))
                        .responseConfig(logConfigBuilder -> logConfigBuilder.headers(true)))
                .httpClientConfig(configBuilder -> configBuilder
                        .timeout(0))
                .environment(Environment.SANDBOX)
                .clientCredentialsAuth(new ClientCredentialsAuthModel.Builder(this.clientId, this.clientSecret)
//                                .oAuthTokenProvider((lastOAuthToken, credentialsManager) -> {
//                                    // Add the callback handler to provide a new OAuth token
//                                    // It will be triggered whenever the lastOAuthToken is undefined or expired
//                                    OAuthToken oAuthToken = loadTokenFromDatabase();
//                                    if (oAuthToken != null && !credentialsManager.isTokenExpired(oAuthToken)) {
//                                        return oAuthToken;
//                                    }
//                                    return credentialsManager.fetchToken();
//                                })
                                .build()
                )
                .build();
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

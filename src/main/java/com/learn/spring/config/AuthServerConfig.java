package com.learn.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter{

	@Value("${user.oauth.signingKey}")
    private String signingKey;
	
	@Value("${user.oauth.clientId}")
    private String clientID;
	
    @Value("${user.oauth.clientSecret}")
    private String clientSecret;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private BCryptPasswordEncoder bcryptPasswordEncoder;
    
	 @Autowired
	 @Qualifier("userDetailsService")
	 UserDetailsService userDetailsService;
    
    @Bean 
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
    	JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    	converter.setSigningKey(signingKey);
    	return converter;
    }
    
    @Bean
    public TokenStore jwtTokenStore() {
    	return new JwtTokenStore(jwtAccessTokenConverter());
    }
    
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
    	endpoints.tokenStore(jwtTokenStore())
    	.authenticationManager(authenticationManager).userDetailsService(userDetailsService)
    	.accessTokenConverter(jwtAccessTokenConverter());
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    	clients.inMemory().withClient(clientID)
    	.secret(bcryptPasswordEncoder.encode(clientSecret))
    	.authorizedGrantTypes("authorization_code","refresh_token","password")
    	.scopes("read","write")
    	.accessTokenValiditySeconds(300)//5mins
    	.refreshTokenValiditySeconds(86400);//1day
    }
    
}

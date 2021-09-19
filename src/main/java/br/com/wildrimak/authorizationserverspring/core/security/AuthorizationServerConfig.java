package br.com.wildrimak.authorizationserverspring.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

	clients
		.inMemory()
        		.withClient("wildrimak-web")
        		.secret(passwordEncoder.encode("wdk123"))
        		.authorizedGrantTypes("password")
        		.scopes("write", "read")
        		.accessTokenValiditySeconds(300)
        	.and()
                	.withClient("wildrimak-mobile")
        		.secret(passwordEncoder.encode("wdk123"))
        		.authorizedGrantTypes("password")
        		.scopes("write", "read")
        ;
    
    }
    
    @Override // Somente o fluxo password flow precisa disso
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager);
    }
    
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

//	security.checkTokenAccess("isAuthenticated()");
	security.checkTokenAccess("permitAll()");
    }

}

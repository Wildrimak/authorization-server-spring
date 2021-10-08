package br.com.wildrimak.authorizationserverspring.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

	clients
		.inMemory()
        		.withClient("wildrimak-web")
        		.secret(passwordEncoder.encode("wdk123"))
        		.authorizedGrantTypes("password", "refresh_token")
        		.scopes("write", "read")
        		.accessTokenValiditySeconds(20)
        		.refreshTokenValiditySeconds(60)
        	.and()
                	.withClient("wildrimak-mobile")
        		.secret(passwordEncoder.encode("wdk123"))
        		.authorizedGrantTypes("password")
        		.scopes("write", "read")
        	.and() // Um client para o resource server não precisar conhecer/depender dos meus clients atuais
                	.withClient("resource-server-check-token")
        		.secret(passwordEncoder.encode("rsct-123"))
        ;
    
    }
    
    @Override // Somente o fluxo password flow precisa disso
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager)
        .userDetailsService(userDetailsService)
        .reuseRefreshTokens(false); // com isso o client só pode acionar o refresh token uma única vez
    }
    
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

//	security.checkTokenAccess("isAuthenticated()");
	security.checkTokenAccess("permitAll()");
    }

}

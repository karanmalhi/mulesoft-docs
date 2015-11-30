package org.mule.modules.cookbook.config;

import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.oauth.*;
import org.mule.api.annotations.param.Default;

import com.cookbook.tutorial.client.MuleCookBookClient;

@OAuth2(configElementName = "oauth2", friendlyName = "OAuth 2.0", authorizationUrl = "http://devkit-cookbook.cloudhub.io/rest/oauth/authorize", accessTokenUrl = "http://devkit-cookbook.cloudhub.io/rest/oauth/accessToken")
public class OAuthConfig {

    private MuleCookBookClient client;

    @OAuthAccessToken
    private String accessToken;

    @Configurable
    @OAuthConsumerKey
    private String consumerKey;

    @Configurable
    @OAuthConsumerSecret
    private String consumerSecret;

    /**
     * URL used to connect to the service
     */
    @Configurable
    @Default("http://devkit-cookbook.cloudhub.io/soap")
    private String address;

    @OAuthPostAuthorization
    public void postAuthorize() {
        setClient(new MuleCookBookClient(getAddress()));
        getClient().setToken(getAccessToken());
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getConsumerKey() {
        return this.consumerKey;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public String getConsumerSecret() {
        return this.consumerSecret;
    }

    public MuleCookBookClient getClient() {
        return client;
    }

    public void setClient(MuleCookBookClient client) {
        this.client = client;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}

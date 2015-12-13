/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.cookbook.config;

import org.mule.api.ConnectionException;
import org.mule.api.ConnectionExceptionCode;
import org.mule.api.annotations.Connect;
import org.mule.api.annotations.ConnectionIdentifier;
import org.mule.api.annotations.Disconnect;
import org.mule.api.annotations.TestConnectivity;
import org.mule.api.annotations.ValidateConnection;
import org.mule.api.annotations.components.ConnectionManagement;
import org.mule.api.annotations.display.Password;
import org.mule.api.annotations.param.ConnectionKey;

import com.cookbook.tutorial.client.MuleCookBookClient;
import com.cookbook.tutorial.service.InvalidCredentialsException;

/**
 * Configuration type Config
 *
 * @author MuleSoft, Inc.
 */
@ConnectionManagement(friendlyName = "Configuration")
public class ConnectionManagementConfig extends ConnectorConfig {

    /**
     * Connect
     *
     * @param username
     *            A username
     * @param password
     *            A password
     * @throws ConnectionException
     */
    @Connect
    @TestConnectivity
    public void connect(@ConnectionKey String username, @Password String password) throws ConnectionException {
        setClient(new MuleCookBookClient(getAddress()));
        try {
            getClient().login(username, password);
        } catch (InvalidCredentialsException e) {
            throw new ConnectionException(ConnectionExceptionCode.INCORRECT_CREDENTIALS, e.getMessage(), "Invalid credentials");
        }
    }

    /**
     * Disconnect
     */
    @Disconnect
    public void disconnect() {
        setClient(null);
    }

    /**
     * Are we connected
     */
    @ValidateConnection
    public boolean isConnected() {
        return getClient() != null;
    }

    /**
     * Id used only when debuging.
     */
    @ConnectionIdentifier
    public String connectionId() {
        return "001";
    }

}

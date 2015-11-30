/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.cookbook.config;

import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.param.Default;

import com.cookbook.tutorial.client.MuleCookBookClient;

/**
 * Configuration type Config
 *
 * @author MuleSoft, Inc.
 */
public abstract class ConnectorConfig {

    /**
     * Description for address
     */
    @Configurable
    @Default("http://devkit-cookbook.cloudhub.io/soap")
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private MuleCookBookClient client;

    public MuleCookBookClient getClient() {
        return client;
    }

    public void setClient(MuleCookBookClient client) {
        this.client = client;
    }

}
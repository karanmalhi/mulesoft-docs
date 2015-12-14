/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.cookbook;

import java.util.List;

import org.mule.api.annotations.Config;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.ReconnectOn;
import org.mule.api.annotations.lifecycle.OnException;
import org.mule.api.annotations.param.Default;
import org.mule.modules.cookbook.config.ConnectorConfig;
import org.mule.modules.cookbook.handler.CookbookHandler;

import com.cookbook.tutorial.service.Ingredient;
import com.cookbook.tutorial.service.InvalidEntityException;
import com.cookbook.tutorial.service.Recipe;
import com.cookbook.tutorial.service.SessionExpiredException;

/**
 * Anypoint Connector
 *
 * @author MuleSoft, Inc.
 */
@Connector(name = "cookbook", friendlyName = "Cookbook")
public class CookbookConnector {

    @Config
    ConnectorConfig config;

    /**
     * Returns the list of recently added recipes
     *
     * {@sample.xml ../../../doc/cook-book-connector.xml.sample cook-book:getRecentlyAdded}
     * 
     * @return A list of the recently added recipes
     */
    @Processor
    public List<Recipe> getRecentlyAdded() {
        return config.getClient().getRecentlyAdded();
    }

    /**
     * Description for createIngredient
     *
     * {@sample.xml ../../../doc/cook-book-connector.xml.sample cook-book:createIngredient}
     *
     * @param Ingredient
     *            Ingredient to be created
     * @return return Ingredient with Id from the system.
     * 
     * @throws InvalidTokenException
     * @throws SessionExpiredException
     * @throws InvalidEntityException
     */
    @Processor
    @OnException(handler = CookbookHandler.class)
    @ReconnectOn(exceptions = { SessionExpiredException.class })
    public Ingredient createIngredient(@Default("#[payload]") Ingredient Ingredient) throws InvalidEntityException, SessionExpiredException {
        return (Ingredient) config.getClient().create(Ingredient);
    }

    public ConnectorConfig getConfig() {
        return config;
    }

    public void setConfig(ConnectorConfig config) {
        this.config = config;
    }

}
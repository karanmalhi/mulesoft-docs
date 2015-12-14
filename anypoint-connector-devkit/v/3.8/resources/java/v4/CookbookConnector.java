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
import org.mule.api.annotations.param.RefOnly;
import org.mule.modules.cookbook.handler.CookbookHandler;
import org.mule.modules.cookbook.config.ConnectorConfig;

import com.cookbook.tutorial.service.CookBookEntity;
import com.cookbook.tutorial.service.InvalidEntityException;
import com.cookbook.tutorial.service.InvalidTokenException;
import com.cookbook.tutorial.service.NoSuchEntityException;
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
	private
    ConnectorConfig config;

    /**
     * Get a list of the most recently added Ingredients
     *
     * {@sample.xml ../../../doc/cook-book-connector.xml.sample cook-book:getRecentlyAdded}
     *
     * @return List of recently added Ingredients.
     *
     */
    @Processor
    public List<Recipe> getRecentlyAdded() {
        return config.getClient().getRecentlyAdded();
    }

	/**
	 * Description for create
	 *
	 * {@sample.xml ../../../doc/cook-book-connector.xml.sample cook-book:create}
	 *
	 * @param entity Ingredient to be created
	 * @return return Ingredient with Id from the system.
	 *
	 * @throws InvalidTokenException
	 * @throws SessionExpiredException
	 * @throws InvalidEntityException
	 */
	@Processor
	@OnException (handler=CookbookHandler.class)
	@ReconnectOn(exceptions = { SessionExpiredException.class })
	public CookBookEntity create(@Default("#[payload]") @RefOnly CookBookEntity entity) throws InvalidEntityException, SessionExpiredException {
		return config.getClient().create(entity);
	}

	/**
	 * Description for update
	 *
	 * {@sample.xml ../../../doc/cook-book-connector.xml.sample cook-book:update}
	 *
	 * @param entity Ingredient to be updated
	 * @return return Ingredient with Id from the system.
	 *
	 * @throws SessionExpiredException
	 * @throws InvalidEntityException
	 * @throws NoSuchEntityException
	 */
	@Processor
	@OnException (handler=CookbookHandler.class)
	@ReconnectOn(exceptions = { SessionExpiredException.class })
	public CookBookEntity update(@Default("#[payload]") @RefOnly CookBookEntity entity) throws InvalidEntityException, SessionExpiredException, NoSuchEntityException {
		return config.getClient().update(entity);
	}

	/**
	 * Description for get
	 *
	 * {@sample.xml ../../../doc/cook-book-connector.xml.sample cook-book:get}
	 *
	 * @param id Id of the entity to retrieve
	 * @return return Ingredient with Id from the system.
	 *
	 * @throws SessionExpiredException
	 * @throws InvalidEntityException
	 * @throws NoSuchEntityException
	 */
	@Processor
	@OnException (handler=CookbookHandler.class)
	@ReconnectOn(exceptions = { SessionExpiredException.class })
	public CookBookEntity get(@Default("1") Integer id) throws InvalidEntityException, SessionExpiredException, NoSuchEntityException {
		return config.getClient().get(id);
	}

	/**
	 * Description for get
	 *
	 * {@sample.xml ../../../doc/cook-book-connector.xml.sample cook-book:get}
	 *
	 * @param id Id of the entity to retrieve
	 * @return return Ingredient with Id from the system.
	 *
	 * @throws SessionExpiredException
	 * @throws NoSuchEntityException
	 */
	@Processor
	@OnException (handler=CookbookHandler.class)
	@ReconnectOn(exceptions = { SessionExpiredException.class })
	public void delete(@Default("1") Integer id) throws NoSuchEntityException, SessionExpiredException {
		config.getClient().delete(id);
	}

	public ConnectorConfig getConfig() {
		return config;
	}

	public void setConfig(ConnectorConfig config) {
		this.config = config;
	}

}

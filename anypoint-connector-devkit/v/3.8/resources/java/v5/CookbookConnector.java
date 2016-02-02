/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.cookbook;

import java.util.List;

import org.mule.api.annotations.Config;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.MetaDataScope;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.ReconnectOn;
import org.mule.api.annotations.lifecycle.OnException;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.MetaDataKeyParam;
import org.mule.api.annotations.param.MetaDataKeyParamAffectsType;
import org.mule.api.annotations.param.RefOnly;
import org.mule.modules.cookbook.config.ConnectorConfig;
import org.mule.modules.cookbook.datasense.DataSenseResolver;
import org.mule.modules.cookbook.handler.CookbookHandler;

import com.cookbook.tutorial.service.CookBookEntity;
import com.cookbook.tutorial.service.InvalidEntityException;
import com.cookbook.tutorial.service.NoSuchEntityException;
import com.cookbook.tutorial.service.Recipe;
import com.cookbook.tutorial.service.SessionExpiredException;

/**
 * Anypoint Connector
 *
 * @author MuleSoft, Inc.
 */
@Connector(name = "cookbook", friendlyName = "Cookbook")
@MetaDataScope(DataSenseResolver.class)
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
     * Description for create
     *
     * {@sample.xml ../../../doc/cook-book-connector.xml.sample cook-book:create}
     *
     * @param type
     *            Object Type
     * @param entity
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
    public CookBookEntity create(@MetaDataKeyParam(affects = MetaDataKeyParamAffectsType.BOTH) String type, @Default("#[payload]") @RefOnly CookBookEntity entity)
            throws InvalidEntityException, SessionExpiredException {
        return config.getClient().create(entity);
    }

    /**
     * Description for update
     *
     * {@sample.xml ../../../doc/cook-book-connector.xml.sample cook-book:update}
     *
     * @param type
     *            Object Type
     * @param entity
     *            Ingredient to be updated
     * @return return Ingredient with Id from the system.
     *
     * @throws SessionExpiredException
     * @throws InvalidEntityException
     * @throws NoSuchEntityException
     */
    @Processor
    @OnException(handler = CookbookHandler.class)
    @ReconnectOn(exceptions = { SessionExpiredException.class })
    public CookBookEntity update(@MetaDataKeyParam(affects = MetaDataKeyParamAffectsType.BOTH) String type, @Default("#[payload]") @RefOnly CookBookEntity entity)
            throws InvalidEntityException, SessionExpiredException, NoSuchEntityException {
        return config.getClient().update(entity);
    }

    /**
     * Description for get
     *
     * {@sample.xml ../../../doc/cook-book-connector.xml.sample cook-book:get}
     *
     * @param type
     *            Object Type
     * @param id
     *            Id of the entity to retrieve
     * @return return Ingredient with Id from the system.
     *
     * @throws SessionExpiredException
     * @throws InvalidEntityException
     * @throws NoSuchEntityException
     */
    @Processor
    @OnException(handler = CookbookHandler.class)
    @ReconnectOn(exceptions = { SessionExpiredException.class })
    public CookBookEntity get(@MetaDataKeyParam(affects = MetaDataKeyParamAffectsType.OUTPUT) String type, @Default("1") Integer id) throws InvalidEntityException,
            SessionExpiredException, NoSuchEntityException {
        return config.getClient().get(id);
    }

    /**
     * Description for get
     *
     * {@sample.xml ../../../doc/cook-book-connector.xml.sample cook-book:get}
     *
     * @param id
     *            Id of the entity to retrieve
     * @return return Ingredient with Id from the system.
     *
     * @throws SessionExpiredException
     * @throws NoSuchEntityException
     */
    @Processor
    @OnException(handler = CookbookHandler.class)
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

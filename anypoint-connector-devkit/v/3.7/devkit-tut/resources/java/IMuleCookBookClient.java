package com.cookbook.tutorial.client;

import com.cookbook.tutorial.service.*;

import java.util.List;

/**
 * Created by Mulesoft.
 */
public interface IMuleCookBookClient {

    public void setToken(String token);

    public void login(String username,String password) throws InvalidCredentialsException;

    public List<Recipe> getRecentlyAdded();

    public void getRecentlyAdded(ICookbookCallback callback) throws Exception;

    public CookBookEntity get(int id) throws NoSuchEntityException, SessionExpiredException;

    public List<CookBookEntity> searchWithQuery(String query, Integer page, Integer pageSize) throws InvalidRequestException,
            SessionExpiredException;

    public CookBookEntity update(CookBookEntity entity) throws NoSuchEntityException,
            InvalidEntityException, SessionExpiredException;

    public List<CookBookEntity> addList(List<CookBookEntity> entities) throws InvalidEntityException,
            SessionExpiredException;

    public List<CookBookEntity> getList(List<Integer> entityIds) throws NoSuchEntityException,
            SessionExpiredException;

    public void delete(int id) throws NoSuchEntityException, SessionExpiredException;

    public List<CookBookEntity> updateList(List<CookBookEntity> entities) throws NoSuchEntityException,
            InvalidEntityException, SessionExpiredException;

    public void deleteList(List<Integer> entityIds) throws NoSuchEntityException,
            SessionExpiredException;

    public CookBookEntity create(CookBookEntity entity) throws InvalidEntityException,
            SessionExpiredException;

    public List<CookBookEntity> getEntities() throws SessionExpiredException;

    public Description describeEntity(CookBookEntity cookBookEntity) throws InvalidTokenException, InvalidEntityException, NoSuchEntityException, SessionExpiredException;
}

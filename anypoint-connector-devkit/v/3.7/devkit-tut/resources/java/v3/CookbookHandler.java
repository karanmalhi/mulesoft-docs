package org.mule.modules.cookbook.handler;

import org.mule.api.annotations.Handle;
import org.mule.api.annotations.components.Handler;

import com.cookbook.tutorial.service.InvalidEntityException;

@Handler
public class CookbookHandler {

    @Handle
    public void handleException(Exception ex) throws Exception {
        if (ex instanceof InvalidEntityException) {
            throw new RuntimeException("You cannot provide an Id when creating an Ingredient");
        } else {
            // Just let it go
            throw ex;
        }
    }

}

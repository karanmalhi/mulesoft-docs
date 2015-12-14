package org.mule.modules.cookbook.datasense;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.mule.api.annotations.MetaDataKeyRetriever;
import org.mule.api.annotations.MetaDataRetriever;
import org.mule.api.annotations.components.MetaDataCategory;
import org.mule.common.metadata.DefaultMetaData;
import org.mule.common.metadata.DefaultMetaDataKey;
import org.mule.common.metadata.MetaData;
import org.mule.common.metadata.MetaDataKey;
import org.mule.common.metadata.MetaDataModel;
import org.mule.common.metadata.builder.DefaultMetaDataBuilder;
import org.mule.common.metadata.builder.PojoMetaDataBuilder;

import com.cookbook.tutorial.service.Ingredient;
import com.cookbook.tutorial.service.Recipe;

@MetaDataCategory
public class DataSenseResolver {

    /**
     * Retrieves the list of keys
     */
    @MetaDataKeyRetriever
    public List<MetaDataKey> getMetaDataKeys() throws Exception {
        List<MetaDataKey> keys = new ArrayList<MetaDataKey>();

        // Generate the keys
        keys.add(new DefaultMetaDataKey("id1", "Ingredient"));
        keys.add(new DefaultMetaDataKey("id2", "Recipe"));

        return keys;
    }

    /**
     * Get MetaData given the Key the user selects
     * 
     * @param key
     *            The key selected from the list of valid keys
     * @return The MetaData model of that corresponds to the key
     * @throws Exception
     *             If anything fails
     */
    @MetaDataRetriever
    public MetaData getMetaData(MetaDataKey key) throws Exception {
        DefaultMetaDataBuilder builder = new DefaultMetaDataBuilder();
        // Since our model is static and we can simply create the pojo model.
        PojoMetaDataBuilder<?> pojoObject = null;
        if ("id1".equals(key.getId())) {
            pojoObject = builder.createPojo(Ingredient.class);
        } else if ("id2".equals(key.getId())) {
            pojoObject = builder.createPojo(Recipe.class);
        } else {
            throw new RuntimeException("Invalid key:" + key.getId());
        }
        MetaDataModel model = pojoObject.build();
        MetaData metaData = new DefaultMetaData(model);

        return metaData;
    }


    @Inject
    private CookbookConnector connector;

    
    public CookbookConnector getConnector() {
        return connector;
    }

    public void setConnector(CookbookConnector connector) {
        this.connector = connector;
    }
}

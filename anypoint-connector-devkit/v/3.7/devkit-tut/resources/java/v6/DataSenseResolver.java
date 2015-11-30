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
import org.mule.common.metadata.builder.DynamicObjectBuilder;
import org.mule.common.metadata.datatype.DataType;
import org.mule.modules.cookbook.CookbookConnector;

import com.cookbook.tutorial.service.CookBookEntity;
import com.cookbook.tutorial.service.Description;
import com.cookbook.tutorial.service.Ingredient;
import com.cookbook.tutorial.service.InvalidEntityException;
import com.cookbook.tutorial.service.InvalidTokenException;
import com.cookbook.tutorial.service.NoSuchEntityException;
import com.cookbook.tutorial.service.SessionExpiredException;
import com.cookbook.tutorial.service.UnitType;

@MetaDataCategory
public class DataSenseResolver {

    @Inject
    private CookbookConnector connector;

    /**
     * Retrieves the list of keys
     */
    @MetaDataKeyRetriever
    public List<MetaDataKey> getMetaDataKeys() throws Exception {
        List<MetaDataKey> keys = new ArrayList<MetaDataKey>();
        List<CookBookEntity> entities = getConnector().getConfig()
                .getClient().getEntities();
        // Generate the keys
        for (CookBookEntity entity : entities) {
            keys.add(new DefaultMetaDataKey(entity.getClass().getName() + "#"
                    + entity.getId(), entity.getName()));
        }

        return keys;
    }

    /**
     * Get MetaData given the Key the user selects
     *
     * @param key The key selected from the list of valid keys
     * @return The MetaData model of that corresponds to the key
     * @throws Exception If anything fails
     */
    @MetaDataRetriever
    public MetaData getMetaData(MetaDataKey key) throws Exception {
        DefaultMetaDataBuilder builder = new DefaultMetaDataBuilder();
        // Since our model is static and we can simply create the pojo model.
        String[] keyParts = key.getId().split("#");
        if (keyParts.length != 2) {
            throw new RuntimeException(
                    "Invalid key. Format should be 'entityType#id'");
        }
        Integer id = Integer.valueOf(keyParts[1]);
        CookBookEntity entity = (CookBookEntity) Class.forName(keyParts[0])
                .newInstance();
        entity.setId(id);
        Description description = getConnector().getConfig().getClient()
                .describeEntity(entity);

        DynamicObjectBuilder<?> dynamicObject = builder.createDynamicObject(key
                .getId());

        for (Description fields : description.getInnerFields()) {
            addFields(fields, dynamicObject);
        }

        MetaDataModel model = builder.build();
        MetaData metaData = new DefaultMetaData(model);

        return metaData;
    }

    private void addFields(Description description,
            DynamicObjectBuilder<?> dynamicObject) {
        switch (description.getDataType()) {
        case DATE:
            dynamicObject.addSimpleField(description.getName(),
                    DataType.DATE_TIME);
            break;
        case DOUBLE:
            dynamicObject
                    .addSimpleField(description.getName(), DataType.DOUBLE);
            break;
        case INTEGER:
            dynamicObject.addSimpleField(description.getName(),
                    DataType.INTEGER);
            break;
        case LIST:
            if (description.getInnerType().equals("String")) {
                dynamicObject.addList(description.getName()).ofSimpleField(
                        DataType.STRING);
            } else if (description.getInnerType().equals("Ingredient")) {
                DynamicObjectBuilder<?> innerObject = dynamicObject.addList(
                        description.getName()).ofDynamicObject("ingredients");
                try {
                    Description ingredientDescription = getConnector()
                            .getConfig().getClient()
                            .describeEntity(new Ingredient());
                    for (Description desc : ingredientDescription.getInnerFields()) {
                        addFields(desc, innerObject);
                    }
                } catch (InvalidTokenException e) {
                    e.printStackTrace();
                } catch (InvalidEntityException e) {
                    e.printStackTrace();
                } catch (NoSuchEntityException e) {
                    e.printStackTrace();
                } catch (SessionExpiredException e) {
                    e.printStackTrace();
                }
            }
            break;
        case OBJECT:
            DynamicObjectBuilder<?> innerObject = dynamicObject
                    .addDynamicObjectField(description.getName());
            for (Description field : description.getInnerFields()) {
                addFields(field, innerObject);
            }
            break;
        case STRING:
            dynamicObject
                    .addSimpleField(description.getName(), DataType.STRING);
            break;
        case UNIT_TYPE:
            dynamicObject.addEnumField(description.getName(),
                    UnitType.class.getName());
            break;
        default:
            break;
        }

    }

    public CookbookConnector getConnector() {
        return connector;
    }

    public void setConnector(CookbookConnector connector) {
        this.connector = connector;
    }

}

package org.mule.modules.cookbook.pagination;

import java.util.List;
import java.util.Map;

import org.mule.api.MuleException;
import org.mule.modules.cookbook.CookbookConnector;
import org.mule.streaming.ProviderAwarePagingDelegate;

import com.cookbook.tutorial.service.CookBookEntity;
import com.cookbook.tutorial.service.SessionExpiredException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CookbookPagingDelegate extends ProviderAwarePagingDelegate<Map<String, Object>, CookbookConnector> {

    private Integer currentPage = 0;
    private ObjectMapper m = new ObjectMapper();

    private final Integer pageSize;
    private final String query;

    public CookbookPagingDelegate(String query, Integer pageSize) {
        super();
        this.query = query;
        this.pageSize = pageSize;
    }

    @Override
    public void close() throws MuleException {

    }

    /**
     * Returns the next page of items. If the return value is <code>null</code> or an
     * empty list, then it means no more items are available
     *
     * @param connector The provider to be used to do the query. You can assume this provider is already properly initialized
     * @return a populated list of elements. Returning <code>null</code> or an empty list, means no more items are available.
     * @throws Exception
     */
    @Override
    public List<Map<String, Object>> getPage(CookbookConnector connector)
            throws Exception {

        try {
            List<CookBookEntity> list = connector.getConfig()
                    .getClient()
                    .searchWithQuery(query, currentPage++, pageSize);
            List<Map<String, Object>> result = m.convertValue(list,
                    new TypeReference<List<Map<String, Object>>>() {

                    });
            return result;
        } catch (SessionExpiredException ex) {
            //Revert the increment since we want to retry to get the same page if the reconnection is configured.
            currentPage--;
            throw ex;
        }
    }

    /**
     * Returns the total amount of items in the non-paged result set.
     * <p/>
     * In some scenarios, it might not be possible/convenient to actually retrieve this value. -1 is
     * returned in such a case.
     *
     * @param connector The provider to be used to do the query. You can assume this provider is already properly initialized
     */
    @Override
    public int getTotalResults(CookbookConnector provider) throws Exception {
        return -1;
    }

}

package handson;



import com.commercetools.api.client.ApiRoot;
import com.commercetools.api.models.customer.Customer;
import com.commercetools.api.models.subscription.*;
import com.commercetools.api.models.type.ResourceTypeId;
import handson.impl.ClientService;
import io.vrap.rmf.base.client.ApiHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import static handson.impl.ClientService.createApiClient;
import static handson.impl.ClientService.getProjectKey;

/**
 * Create a subscription for customer change requests.
 *
 */
public class B2B_SUBSCRIPTION {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        // TODO:
        //  Check your prefix
    	//*** Importnat Step needs to follow up ****
        //The topic must exist beforehand. The topic has to give the pubsub.topics.publish permission to the service account subscriptions@commercetools-platform.iam.gserviceaccount.com.
        String apiClientPrefix ="ma-rc-dev-admin.";

        final String projectKey = getProjectKey(apiClientPrefix);
        final ApiRoot client = createApiClient(apiClientPrefix);
        Logger logger = LoggerFactory.getLogger(B2B_SUBSCRIPTION.class.getName());

        try (ApiHttpClient apiHttpClient = ClientService.apiHttpClient) {
            logger.info("Created subscription: " +
                    client
                            .withProjectKey(projectKey)
                            .subscriptions()
                            .post(
                                    SubscriptionDraftBuilder.of()
                                            .key("b2b-subscription-add-monthly-spent")
                                            .destination(
                                                    GoogleCloudPubSubDestinationBuilder.of()
                                                    .projectId("rc-b2b")
                                                    .topic("ct-b2b-topic")
                                                    .build()
                                            )
                                            .messages(
                                                    Arrays.asList(
                                                    		
                                                    		MessageSubscriptionBuilder.of()
                                                                    .resourceTypeId(
                                                                            ResourceTypeId.ORDER.toString().toLowerCase()                      // really toString??
                                                                    )
                                                                    .types(Arrays.asList("OrderCreated", "OrderStateChanged"))
                                                                    
                                                                    .build()
                                                    )
                                                   
                                            )
                                            .build()
                            )
                            .execute()
                            .toCompletableFuture().get()
                            .getBody()
            );
        }
        
        
        
               

    }
}

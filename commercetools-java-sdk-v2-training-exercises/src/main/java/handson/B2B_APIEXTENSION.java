package handson;

import com.commercetools.api.client.ApiRoot;
import com.commercetools.api.models.extension.*;
import handson.impl.ClientService;
import io.vrap.rmf.base.client.ApiHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static handson.impl.ClientService.createApiClient;
import static handson.impl.ClientService.getProjectKey;


public class B2B_APIEXTENSION {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        // TODO:
        //  Check your prefix
        //
        String apiClientPrefix = "ctp.";

        final String projectKey = getProjectKey(apiClientPrefix);
        final ApiRoot client = createApiClient(apiClientPrefix);
        Logger logger = LoggerFactory.getLogger(B2B_APIEXTENSION.class.getName());

        try (ApiHttpClient apiHttpClient = ClientService.apiHttpClient) {
            logger.info("Created extension: " +
                    client
                            .withProjectKey(projectKey)
                            .extensions()
                            .post(
                                    ExtensionDraftBuilder.of()
                                            .key("b2b-order-create-extension")
                                            .destination(
                                                    ExtensionHttpDestinationBuilder.of()
                                                    .url("https://us-central1-rc-b2b.cloudfunctions.net/b2bOrderCreate")
                                                    .build()
                                            )
                                            .triggers(
                                                    Arrays.asList(
                                                            ExtensionTriggerBuilder.of()
                                                                    .resourceTypeId(ExtensionResourceTypeId.ORDER)
                                                                    .actions(
                                                                            Arrays.asList(
                                                                                    ExtensionAction.CREATE
                                                                            )
                                                                    )
                                                                    .build()
                                                    )
                                            )
                                            .build()
                            )
                            .execute()
                            .toCompletableFuture().get()
                            .getBody().getId()
            );
        }

    }
}


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


public class Task07c_APIEXTENSION {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        // TODO:
        //  Check your prefix
        //
        String apiClientPrefix = "ma-rc-dev-admin.";

        final String projectKey = getProjectKey(apiClientPrefix);
        final ApiRoot client = createApiClient(apiClientPrefix);
        Logger logger = LoggerFactory.getLogger(Task07c_APIEXTENSION.class.getName());

        try (ApiHttpClient apiHttpClient = ClientService.apiHttpClient) {
            logger.info("Created extension: " +
                    client
                            .withProjectKey(projectKey)
                            .extensions()
                            .post(
                                    ExtensionDraftBuilder.of()
                                            .key("b2b-order-create-extension")
                                            .destination(
                                                    ExtensionAWSLambdaDestinationBuilder.of()
                                                            .arn("arn:aws:lambda:us-east-2:775875797200:function:ma-rc-order-plant-check")
                                                            .accessKey("AKIAIZER4663A26ZHJPA")
                                                            .accessSecret("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")
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


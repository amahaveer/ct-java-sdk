package handson;

import com.commercetools.api.client.ApiRoot;
import com.commercetools.api.models.common.LocalizedString;
import com.commercetools.api.models.common.LocalizedStringBuilder;
import com.commercetools.api.models.customer.CustomerDraft;
import com.commercetools.api.models.extension.ExtensionDraft;
import com.commercetools.api.models.product_type.TextInputHint;
import com.commercetools.api.models.type.*;
import handson.impl.ClientService;
import io.vrap.rmf.base.client.ApiHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static handson.impl.ClientService.createApiClient;
import static handson.impl.ClientService.getProjectKey;


public class B2B_CUSTOMTYPES {


    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        // TODO:
        //  Check your prefix
        //
        String apiClientPrefix = "ctp.";

        final String projectKey = getProjectKey(apiClientPrefix);
        final ApiRoot client = createApiClient(apiClientPrefix);
        Logger logger = LoggerFactory.getLogger(B2B_CUSTOMTYPES.class.getName());

        try (ApiHttpClient apiHttpClient = ClientService.apiHttpClient) {

            Map<String, String> namesForFieldRoles = new HashMap<String, String>() {
                {
                    put("DE", "Roles");
                    put("EN", "Roles");
                }
            };
            Map<String, String> namesForFieldAmountExpent = new HashMap<String, String>() {
                {
                    put("DE", "Total Amount expent in the actual month");
                    put("EN", "Total Amount expent in the actual month");
                }
            };

            // Which fields will be used?
            List<FieldDefinition> definitions = Arrays.asList(
                    FieldDefinitionBuilder.of()
                            .name("roles")
                            .required(false)
                            .label(LocalizedStringBuilder.of()
                                    .values(namesForFieldRoles)
                                    .build()
                            )
                            .type(CustomFieldSetTypeBuilder.of()
                            		.elementType(FieldType())
                            		.build())
                            .build()
                    ,
                    FieldDefinitionBuilder.of()
                            .name("amountExpent"
                            .required(false)
                            .label(LocalizedStringBuilder.of()
                                    .values(namesForFieldAmountExpent)
                                    .build()
                            )
                            .type(CustomFieldStringType.of())
                            .inputHint(TypeTextInputHint.MULTI_LINE)            // shown as single line????
                            .build()
            );

            Map<String, String> namesForType = new HashMap<String, String>() {
                {
                    put("DE", "Employee custom type");
                    put("EN", "Employee custom type");
                }
            };

            logger.info("Custom Type info: " +
            		 client
                     .withProjectKey(projectKey)
                     .types()
                     .post(TypeDraftBuilder.of()
                    		 .resourceTypeIds( Arrays.asList( ResourceTypeId.CUSTOMER))
                    		 .key("employee-type")
                    		 .name(LocalizedStringBuilder.of().values(namesForType).build())
                    		 .fieldDefinitions(definitions).build())
                     .execute().toCompletableFuture().get()
                     .getBody()
            );
        }

    }
}

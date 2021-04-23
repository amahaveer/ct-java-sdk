package handson;

import com.commercetools.api.client.ApiRoot;
import com.commercetools.api.models.common.LocalizedString;
import com.commercetools.api.models.common.LocalizedStringBuilder;
import com.commercetools.api.models.common.ReferenceTypeId;
import com.commercetools.api.models.customer.CustomerDraft;
import com.commercetools.api.models.extension.ExtensionDraft;
import com.commercetools.api.models.product_type.TextInputHint;
import com.commercetools.api.models.type.*;
import com.commercetools.importapi.models.customfields.StringField;

import handson.impl.ClientService;
import io.vrap.rmf.base.client.ApiHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static handson.impl.ClientService.createApiClient;
import static handson.impl.ClientService.getProjectKey;


public class Task07a_CUSTOMTYPES {


    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        // TODO:
        //  Check your prefix
        //
        String apiClientPrefix = "ctp2.";

        final String projectKey = getProjectKey(apiClientPrefix);
        final ApiRoot client = createApiClient(apiClientPrefix);
        Logger logger = LoggerFactory.getLogger(Task07a_CUSTOMTYPES.class.getName());

        try (ApiHttpClient apiHttpClient = ClientService.apiHttpClient) {

            Map<String, String> namesForFieldCheck = new HashMap<String, String>() {
                {
                    put("DE", "Parent Group");
                    put("EN", "Parent Group");
                }
            };
            Map<String, String> namesForFieldComments = new HashMap<String, String>() {
                {
                    put("DE", "Child Group");
                    put("EN", " Child Group");
                }
            };

            // Which fields will be used?
            List<FieldDefinition> definitions = Arrays.asList(
                    FieldDefinitionBuilder.of()
                            .name("ParentGroup")
                            .required(false)
                            .label(LocalizedStringBuilder.of()
                                    .values(namesForFieldCheck)
                                    .build()
                            )
                            //.type(CustomFieldReferenceTypeBuilder.of().referenceTypeId(ReferenceTypeId.CUSTOMER_GROUP).build())
                            .type(CustomFieldSetTypeBuilder.of().elementType(CustomFieldStringTypeBuilder.of().build()).build())
                            .build()
                    ,
                    FieldDefinitionBuilder.of()
                            .name("ChildGroup")
                            .required(false)
                            .label(LocalizedStringBuilder.of()
                                    .values(namesForFieldComments)
                                    .build()
                            )
                            //.type(CustomFieldReferenceTypeBuilder.of().referenceTypeId(ReferenceTypeId.CUSTOMER_GROUP).build())
                            .type(CustomFieldSetTypeBuilder.of().elementType(CustomFieldStringTypeBuilder.of().build()).build())
                            .inputHint(TypeTextInputHint.MULTI_LINE)            // shown as single line????
                            .build()
            );

            Map<String, String> namesForType = new HashMap<String, String>() {
                {
                    put("DE", "Customer Rroup Relation Type");
                    put("EN", "Customer Rroup Relation Type");
                }
            };

            logger.info("Custom Type info: " +
            		 client
                     .withProjectKey(projectKey)
                     .types()
                     .post(TypeDraftBuilder.of()
                    		 .resourceTypeIds( Arrays.asList( ResourceTypeId.CUSTOMER))
                    		 .key("jsw-customer-relation-type")
                    		 .name(LocalizedStringBuilder.of().values(namesForType).build())
                    		 .fieldDefinitions(definitions).build())
                     .execute().toCompletableFuture().get()
                     .getBody()
            );
        }

    }
}

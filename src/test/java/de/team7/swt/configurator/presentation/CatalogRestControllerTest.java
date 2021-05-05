package de.team7.swt.configurator.presentation;

import de.team7.swt.configurator.infrastructure.ProductCatalog;
import de.team7.swt.domain.catalog.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Streamable;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;

import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Vincent Nadoll
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class CatalogRestControllerTest<T extends Product> {

    private static final URI BASE_URI = URI.create("/api/v1/catalog");
    private static final String STATUS = "Status";

    private MockMvc mockMvc;

    // @formatter:off
    @MockBean private ProductCatalog catalog;
    // @formatter:on

    @BeforeEach
    void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(documentationConfiguration(restDocumentation).operationPreprocessors()
                .withRequestDefaults(prettyPrint())
                .withResponseDefaults(prettyPrint())
            ).build();
    }

    @Test
    void index() throws Exception {
        when(catalog.streamManagedProducts()).thenAnswer(i -> categories().map(Arguments::get).map(objects -> objects[0]).get());

        mockMvc.perform(get(BASE_URI))
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document(
                "catalog/index",
                responseHeaders(
                    headerWithName(CONTENT_TYPE).description(APPLICATION_JSON),
                    headerWithName(STATUS).description(OK).optional()
                ),
                responseFields(
                    fieldWithPath("product").description("Collection resource location for the entire catalog of products"),
                    fieldWithPath("beertype").description("Beer type's collection resource location"),
                    fieldWithPath("bottle").description("Bottle's collection resource location"),
                    fieldWithPath("flavour").description("Flavour's collection resource location"),
                    fieldWithPath("ingredient").description("Ingredient's collection resource location"),
                    fieldWithPath("label").description("Label's collection resource location")
                )));
    }

    static Streamable<Arguments> categories() {
        return Streamable.of(
            Arguments.of("product"),
            Arguments.of("beertype"),
            Arguments.of("bottle"),
            Arguments.of("flavour"),
            Arguments.of("ingredient"),
            Arguments.of("label")
        );
    }
}

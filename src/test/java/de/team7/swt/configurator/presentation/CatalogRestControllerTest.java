package de.team7.swt.configurator.presentation;

import de.team7.swt.configurator.model.Bottles;
import de.team7.swt.domain.catalog.Catalog;
import de.team7.swt.domain.catalog.Flavours;
import de.team7.swt.domain.catalog.Ingredients;
import de.team7.swt.domain.catalog.Product;
import de.team7.swt.domain.catalog.Products;
import de.team7.swt.domain.catalog.Types;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Streamable;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

import static de.team7.swt.domain.catalog.Types.TYPE_LAGER_ID;
import static de.team7.swt.domain.catalog.Types.createLager;
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
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
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
    private static Products products;
    private static Types types;
    private static Bottles bottles;
    private static Flavours flavours;
    private static Ingredients ingredients;
    @MockBean private Catalog<Product> catalog;
    // @formatter:on

    @BeforeAll
    static void beforeAll() {
        products = new Products();
        types = new Types();
        bottles = new Bottles();
        flavours = new Flavours();
        ingredients = new Ingredients();
    }

    @BeforeEach
    void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(documentationConfiguration(restDocumentation).operationPreprocessors()
                .withRequestDefaults(prettyPrint())
                .withResponseDefaults(prettyPrint())
            ).build();
    }

    @Test
    void listAll() throws Exception {
        when(catalog.findAll()).thenReturn(products.toList());

        mockMvc.perform(get(BASE_URI))
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document(
                "catalog/list",
                responseHeaders(
                    headerWithName(CONTENT_TYPE).description(APPLICATION_JSON),
                    headerWithName(STATUS).description(OK).optional()
                ),
                forProductCollection("product")
            ));
    }

    @Test
    void index() throws Exception {
        when(catalog.findAllCategories()).thenAnswer(i -> Streamable.of(() -> categories().stream().skip(1).map(Arguments::get).map(objects -> objects[0])));

        URI endpoint = UriComponentsBuilder.fromUri(BASE_URI)
            .path("/index")
            .build().toUri();

        mockMvc.perform(get(endpoint))
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document(
                "catalog/index",
                responseHeaders(
                    headerWithName(CONTENT_TYPE).description(APPLICATION_JSON),
                    headerWithName(STATUS).description(OK).optional()
                ),
                responseFields(
                    fieldWithPath("beertype").description("Beer type's collection resource location"),
                    fieldWithPath("bottle").description("Bottle's collection resource location"),
                    fieldWithPath("flavour").description("Flavour's collection resource location"),
                    fieldWithPath("ingredient").description("Ingredient's collection resource location")
                )));
    }

    @ParameterizedTest
    @MethodSource("categories")
    void listBy(String category, Streamable<T> testDataProvider, ResponseFieldsSnippet responseFieldsSnippet) throws Exception {
        when(catalog.findByCategory(category)).thenAnswer(i -> testDataProvider);

        URI endpoint = UriComponentsBuilder.fromUri(BASE_URI)
            .queryParam("category", category)
            .build().toUri();

        mockMvc.perform(get(endpoint))
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document(
                "catalog/" + category,
                requestParameters(
                    parameterWithName("category").description("The category the product is assigned to")
                ),
                responseHeaders(
                    headerWithName(CONTENT_TYPE).description(APPLICATION_JSON),
                    headerWithName(STATUS).description(OK).optional()
                ),
                responseFieldsSnippet
            ));
    }

    @Test
    void retrieve404() throws Exception {
        when(catalog.findById(TYPE_LAGER_ID)).thenReturn(Optional.empty());

        URI endpoint = UriComponentsBuilder.fromUri(BASE_URI)
            .path("/{id}")
            .buildAndExpand(TYPE_LAGER_ID)
            .toUri();

        mockMvc.perform(get(endpoint))
            .andExpect(status().isNotFound());
    }

    @Test
    void retrieve200() throws Exception {
        when(catalog.findById(TYPE_LAGER_ID)).thenReturn(Optional.of(createLager()));

        URI endpoint = UriComponentsBuilder.fromUri(BASE_URI)
            .path("/{id}")
            .buildAndExpand(TYPE_LAGER_ID)
            .toUri();

        mockMvc.perform(get(endpoint))
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document(
                "catalog/retrieve",
                responseHeaders(
                    headerWithName(CONTENT_TYPE).description(APPLICATION_JSON),
                    headerWithName(STATUS).description(OK).optional()
                ),
                responseFields(forProduct("product"))
            ));
    }

    static Streamable<Arguments> categories() {
        return Streamable.of(
            Arguments.of("product", products, forProductCollection("product")),
            Arguments.of("beertype", types, forProductCollection("beer type")),
            Arguments.of("bottle", bottles, forProductCollection("bottle").and(forBottle())),
            Arguments.of("flavour", flavours, forProductCollection("flavour")),
            Arguments.of("ingredient", ingredients, forProductCollection("ingredient"))
        );
    }

    static ResponseFieldsSnippet forProductCollection(String type) {
        return responseFields(
            fieldWithPath("_embedded[]").description("foo")).andWithPrefix("_embedded[].",
            forProduct(type)
        );
    }

    static FieldDescriptor[] forProduct(String type) {
        return new FieldDescriptor[]{
            fieldWithPath("id").description(String.format("The %s's identifier", type)),
            fieldWithPath("name").description(String.format("The %s's name", type)),
            fieldWithPath("metric").description(String.format("The %s's unit of measurement", type)),
            fieldWithPath("price").description(String.format("The %s's price", type)),
            fieldWithPath("price.amount").description(String.format("The amount of the %s's price", type)),
            fieldWithPath("price.currency").description(String.format("The currency of the %s's price", type)),
            fieldWithPath("price.formatted").description(String.format("The localized formatted %s price", type)),
            fieldWithPath("categories[]").description(String.format("Categories the %s is assigned to", type)),
            fieldWithPath("size").ignored().optional(),
            fieldWithPath("color").ignored().optional()
        };
    }

    static FieldDescriptor[] forBottle() {
        return new FieldDescriptor[]{
            fieldWithPath("_embedded[].size").description("The bottle's size"),
            fieldWithPath("_embedded[].color").description("The bottle's color")
        };
    }
}

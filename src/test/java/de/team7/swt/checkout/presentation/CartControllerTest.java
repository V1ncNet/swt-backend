package de.team7.swt.checkout.presentation;

import de.team7.swt.checkout.model.Cart;
import de.team7.swt.configurator.model.Bottle;
import de.team7.swt.domain.catalog.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.format.WebConversionService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import javax.servlet.http.Cookie;

import static de.team7.swt.configurator.model.Bottles.BOTTLE_05_BROWN_ID;
import static de.team7.swt.configurator.model.Bottles.create05Brown;
import static de.team7.swt.domain.catalog.Flavours.FLAVOUR_CHOCOLATE_ID;
import static de.team7.swt.domain.catalog.Flavours.FLAVOUR_COOKIE_ID;
import static de.team7.swt.domain.catalog.Flavours.createChocolate;
import static de.team7.swt.domain.catalog.Flavours.createCookie;
import static de.team7.swt.domain.catalog.Ingredients.INGREDIENT_CLOVE_ID;
import static de.team7.swt.domain.catalog.Ingredients.createClove;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.COOKIE;
import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Vincent Nadoll
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
    "spring.main.allow-bean-definition-overriding=true"
})
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class CartControllerTest {

    private static final URI BASE_URI = URI.create("/api/v1/cart");
    private static final String STATUS = "Status";
    public static final String JSESSIONID = "JSESSIONID";

    private MockMvc mockMvc;

    // @formatter:off
    @Autowired private Cart cart;
    @MockBean(name = "mvcConversionService") private WebConversionService webConversionService;
    // @formatter:on

    @BeforeEach
    void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(documentationConfiguration(restDocumentation)
                .operationPreprocessors()
                .withRequestDefaults(prettyPrint())
                .withResponseDefaults(prettyPrint()))
            .apply(springSecurity())
            .build();

        when(webConversionService.canConvert(any(TypeDescriptor.class), any(TypeDescriptor.class))).thenReturn(true);
    }

    @Test
    void addingFirstProduct_shouldUpdateCart() throws Exception {
        String productId = FLAVOUR_CHOCOLATE_ID.toString();

        when(webConversionService.convert(eq(productId), any(TypeDescriptor.class), any(TypeDescriptor.class))).thenReturn(createChocolate());

        mockMvc.perform(
            put(BASE_URI)
                .param("product_id", productId))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
            .andDo(document(
                "cart/add",
                requestParameters(
                    parameterWithName("product_id").description("The product ID to add to the cart")
                ),
                requestHeaders(
                    headerWithName(COOKIE).description(JSESSIONID).optional()
                ),
                responseHeaders(
                    headerWithName(CONTENT_TYPE).description(APPLICATION_JSON),
                    headerWithName(STATUS).description(OK).optional(),
                    headerWithName(SET_COOKIE).description(JSESSIONID).optional()
                ),
                responseFields(
                    fieldWithPath("items[]").description("Collection of all items in the cart"),
                    fieldWithPath("items[].product").description("The cart item's encapsulated product")
                )
                    .andWithPrefix("items[].", forCartItem())
                    .andWithPrefix("items[].product.", forProduct())
                    .and(forTotal("cart"))
            ));
    }

    static FieldDescriptor[] forTotal(String parent) {
        return new FieldDescriptor[]{
            fieldWithPath("total").description(String.format("The %s's total cost", parent)),
            fieldWithPath("total.amount").description(String.format("The amount of the %s's price", parent)),
            fieldWithPath("total.currency").description(String.format("The currency of the %s's price", parent)),
            fieldWithPath("total.formatted").ignored(),
        };
    }

    @Test
    void addNonExistingProduct_shouldReturnBadRequest() throws Exception {
        String productId = BOTTLE_05_BROWN_ID.toString();

        when(webConversionService.convert(eq(productId), any(TypeDescriptor.class), any(TypeDescriptor.class))).thenReturn(null);

        mockMvc.perform(
            put(BASE_URI)
                .param("product_id", productId))
            .andExpect(status().isBadRequest());
    }

    @Test
    void initialCart_shouldBeEmpty() throws Exception {
        mockMvc.perform(
            get(BASE_URI))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
            .andDo(document(
                "cart/retrieve",
                responseHeaders(
                    headerWithName(CONTENT_TYPE).description(APPLICATION_JSON),
                    headerWithName(STATUS).description(OK).optional(),
                    headerWithName(SET_COOKIE).description(JSESSIONID).optional()
                ),
                responseFields(fieldWithPath("items[]").description("Collection of all items in the cart"))
                    .and(forTotal("cart"))
            ));
    }

    @Test
    void requestingCart_shouldContainFormerlyAddedItems() throws Exception {
        Product chocolate = createChocolate();
        cart.add(chocolate, chocolate.from(1));

        mockMvc.perform(
            get(BASE_URI))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
            .andDo(document(
                "cart/retrieve",
                responseHeaders(
                    headerWithName(CONTENT_TYPE).description(APPLICATION_JSON),
                    headerWithName(STATUS).description(OK).optional(),
                    headerWithName(SET_COOKIE).description(JSESSIONID).optional()
                ),
                responseFields(
                    fieldWithPath("items[]").description("Collection of all items in the cart"),
                    fieldWithPath("items[].product").description("The cart item's encapsulated product")
                )
                    .andWithPrefix("items[].", forCartItem())
                    .andWithPrefix("items[].product.", forProduct())
                    .and(forTotal("cart"))
            ));
    }

    @Test
    void checkout_shouldCreateCompletionReport() throws Exception {
        Bottle bottle = create05Brown();
        cart.add(bottle, bottle.from(1));

        int amount = 10;

        when(webConversionService.convert(eq(String.valueOf(amount)), any(TypeDescriptor.class), any(TypeDescriptor.class))).thenReturn(amount);

        URI endpoint = UriComponentsBuilder.fromUri(BASE_URI)
            .path("/checkout")
            .queryParam("amount", amount)
            .build().toUri();

        mockMvc.perform(post(endpoint))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
            .andDo(document(
                "cart/checkout",
                requestParameters(
                    parameterWithName("amount").description("How much of each cart item is put into the order")
                ),
                requestHeaders(
                    headerWithName(COOKIE).description(JSESSIONID).optional()
                ),
                responseHeaders(
                    headerWithName(CONTENT_TYPE).description(APPLICATION_JSON),
                    headerWithName(STATUS).description(OK).optional(),
                    headerWithName(SET_COOKIE).description(JSESSIONID).optional()
                ),
                responseFields(
                    fieldWithPath("status").description("The report's overall completion status"),
                    fieldWithPath("order").description("The report's order"),
                    fieldWithPath("completions[]").description("The report's completion item collection")
                )
                    .andWithPrefix("order.", forOrder())
                    .andWithPrefix("completions[].", forCompletions())
            ));
    }

    @AfterEach
    void tearDown() {
        cart.clear();
    }

    static FieldDescriptor[] forCompletions() {
        return new FieldDescriptor[]{
            fieldWithPath("id").description("The completion's ID"),
            fieldWithPath("product_id").description("The completion's product ID"),
            fieldWithPath("product_name").description("The completion's product name"),
            fieldWithPath("status").description("The completion's status"),
            fieldWithPath("message").description("The completion's (error) message"),
            fieldWithPath("price").description("The item's price"),
            fieldWithPath("price.amount").description("The amount of the item's price"),
            fieldWithPath("price.currency").description("The currency of the item's price"),
            fieldWithPath("price.formatted").ignored(),
            fieldWithPath("quantity").description("The item's quantity"),
            fieldWithPath("quantity.amount").description("The item's quantity amount"),
            fieldWithPath("quantity.metric").description("The item's quantity measurement unit"),
        };
    }

    static FieldDescriptor[] forOrder() {
        return new FieldDescriptor[]{
            fieldWithPath("id").description("The order's ID"),
            fieldWithPath("total").description("The order's total cost"),
            fieldWithPath("total.amount").description("The amount of the order's price"),
            fieldWithPath("total.currency").description("The currency of the order's price"),
            fieldWithPath("total.formatted").ignored(),
        };
    }

    static FieldDescriptor[] forCartItem() {
        return new FieldDescriptor[]{
            fieldWithPath("id").description("The item's ID"),
            fieldWithPath("quantity").description("The item's quantity"),
            fieldWithPath("quantity.amount").description("The item's quantity amount"),
            fieldWithPath("quantity.metric").description("The item's quantity measurement unit"),
            fieldWithPath("price").description("The item's price"),
            fieldWithPath("price.amount").description("The amount of the item's price"),
            fieldWithPath("price.currency").description("The currency of the item's price"),
            fieldWithPath("price.formatted").ignored(),
        };
    }

    static FieldDescriptor[] forProduct() {
        return new FieldDescriptor[]{
            fieldWithPath("id").description("The product's identifier"),
            fieldWithPath("name").description("The product's name"),
            fieldWithPath("metric").description("The product's unit of measurement"),
            fieldWithPath("price").description("The product's price"),
            fieldWithPath("price.amount").description("The amount of the product's price"),
            fieldWithPath("price.currency").description("The currency of the product's price"),
            fieldWithPath("price.formatted").description("The localized formatted product price"),
            fieldWithPath("categories[]").description("Categories the product is assigned to"),
            fieldWithPath("size").ignored().optional(),
            fieldWithPath("color").ignored().optional()
        };
    }

    @Test
    @Disabled("Mock MVC is not capable as of creating session cookies. It's part of the Spring Security module.")
    void addProductWithUniqueCategoryConstraintTwice_shouldReturnBadRequest() throws Exception {
        String chocolateId = FLAVOUR_CHOCOLATE_ID.toString();
        String cookieId = FLAVOUR_COOKIE_ID.toString();

        when(webConversionService.convert(eq(chocolateId), any(TypeDescriptor.class), any(TypeDescriptor.class))).thenReturn(createChocolate());
        when(webConversionService.convert(eq(cookieId), any(TypeDescriptor.class), any(TypeDescriptor.class))).thenReturn(createCookie());

        MvcResult firstResponse = mockMvc.perform(
            put(BASE_URI)
                .param("product_id", chocolateId))
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
        Cookie jsessionid = firstResponse.getResponse().getCookie(JSESSIONID);

        mockMvc.perform(
            put(BASE_URI)
                .param("product_id", cookieId)
                .cookie(jsessionid))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Disabled("Mock MVC is not capable as of creating session cookies. It's part of the Spring Security module.")
    void addProductWithUniqueProductConstraintTwice_shouldReturnBadRequest() throws Exception {
        String productId = INGREDIENT_CLOVE_ID.toString();

        when(webConversionService.convert(eq(productId), any(TypeDescriptor.class), any(TypeDescriptor.class))).thenReturn(createClove());

        MvcResult firstResponse = mockMvc.perform(
            put(BASE_URI)
                .param("product_id", productId))
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
        Cookie jsessionid = firstResponse.getResponse().getCookie(JSESSIONID);

        mockMvc.perform(
            put(BASE_URI)
                .param("product_id", productId)
                .cookie(jsessionid))
            .andExpect(status().isBadRequest());
    }

//    @TestConfiguration(proxyBeanMethods = false)
//    static class CartConfiguration {
//        @Bean
//        public Cart cart() {
//            return new Cart();
//        }
//    }
}

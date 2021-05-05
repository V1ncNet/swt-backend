package de.team7.swt.configurator.presentation;

import de.team7.swt.configurator.infrastructure.ProductCatalog;
import de.team7.swt.domain.catalog.Product;
import de.team7.swt.domain.web.CollectionModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * REST request handler for answering requests related to the catalog domain.
 *
 * @author Vincent Nadoll
 */
@RestController
@RequestMapping("/api/v1/catalog")
@RequiredArgsConstructor
public class CatalogRestController {

    private final ProductCatalog catalog;

    /**
     * Retrieves an index of all products and a link to where the actual product catalog is found.
     *
     * @return 200 - index of all products and their catalog link
     */
    @RequestMapping
    public ResponseEntity<Map<String, URI>> index() {
        Map<String, URI> name = catalog.streamManagedProducts()
            .collect(Collectors.toMap(Function.identity(), this::createLinkTo));
        return ResponseEntity.ok(name);
    }

    private URI createLinkTo(String entityName) {
        return ServletUriComponentsBuilder.fromCurrentRequestUri()
            .queryParam("category", entityName)
            .build().toUri();
    }

    /**
     * Retrieves all products of the given category.
     *
     * @param category must not be {@literal null}
     * @return an embedded product list
     */
    @RequestMapping(params = "category")
    public ResponseEntity<CollectionModel<Product>> listBy(@RequestParam String category) {
        List<Product> products = catalog.streamAllByEntityName(category).collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(products));
    }
}

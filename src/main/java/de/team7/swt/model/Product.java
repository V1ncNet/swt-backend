package de.team7.swt.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.money.MonetaryAmount;
import java.util.UUID;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class Product {

    @Id
    private UUID productId;

    @NonNull
    private String name;

    //@NonNull
    //private MonetaryAmount price;

    @NonNull
    private String categories;

    @NonNull
    private String metric;
}



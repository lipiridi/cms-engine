package com.ecommerce.engine.entity;

import static com.ecommerce.engine.util.NullUtils.nullable;

import com.ecommerce.engine.config.exception.ApplicationException;
import com.ecommerce.engine.config.exception.ErrorCode;
import com.ecommerce.engine.dto.admin.request.ProductRequestDto;
import com.ecommerce.engine.enums.LengthClass;
import com.ecommerce.engine.enums.WeightClass;
import com.ecommerce.engine.util.TranslationUtils;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@Table(name = Product.TABLE_NAME)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {

    public static final String TABLE_NAME = "product";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    Category category;

    @ManyToOne
    @JoinColumn
    Image image;

    @Column(unique = true, length = 64)
    String sku;

    @Column(length = 14)
    String ean;

    @Column(length = 64)
    String barcode;

    @ManyToOne
    @JoinColumn
    Brand brand;

    /*@Column(length = 3)
    Currency currency;

    @Column(precision = 15, scale = 2)
    BigDecimal price;

    @Column(precision = 15, scale = 3)
    BigDecimal quantity;*/

    @Enumerated(EnumType.STRING)
    LengthClass lengthClass;

    @Column(precision = 15, scale = 8)
    BigDecimal length;

    @Column(precision = 15, scale = 8)
    BigDecimal width;

    @Column(precision = 15, scale = 8)
    BigDecimal height;

    @Enumerated(EnumType.STRING)
    WeightClass weightClass;

    @Column(precision = 15, scale = 8)
    BigDecimal weight;

    @CreationTimestamp
    Instant created;

    @UpdateTimestamp
    Instant updated;

    boolean enabled;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product", orphanRemoval = true, cascade = CascadeType.ALL)
    Set<ProductDescription> descriptions = new HashSet<>();

    @ToString.Exclude
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product", orphanRemoval = true, cascade = CascadeType.ALL)
    Set<ProductAdditionalCategory> additionalCategories = new HashSet<>();

    @ToString.Exclude
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product", orphanRemoval = true, cascade = CascadeType.ALL)
    Set<ProductAdditionalImage> additionalImages = new HashSet<>();

    @ToString.Exclude
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product", orphanRemoval = true, cascade = CascadeType.ALL)
    Set<ProductAttribute> attributes = new HashSet<>();

    public Product(ProductRequestDto requestDto) {
        if (requestDto.categoryId() != null) {
            category = new Category(requestDto.categoryId());
        }
        if (requestDto.imageId() != null) {
            image = new Image(requestDto.imageId());
        }
        if (requestDto.brandId() != null) {
            brand = new Brand(requestDto.brandId());
        }
        sku = requestDto.sku();
        ean = requestDto.ean();
        barcode = requestDto.barcode();
        lengthClass = requestDto.lengthClass();
        length = requestDto.length();
        width = requestDto.width();
        height = requestDto.height();
        weightClass = requestDto.weightClass();
        weight = requestDto.weight();
        enabled = requestDto.enabled();

        descriptions = requestDto.descriptions().stream()
                .map(ProductDescription::new)
                .peek(desc -> desc.setProduct(this))
                .collect(Collectors.toSet());

        additionalImages = requestDto.additionalImages().stream()
                .map(ProductAdditionalImage::new)
                .peek(img -> img.setProduct(this))
                .collect(Collectors.toSet());

        additionalCategories = requestDto.additionalCategories().stream()
                .map(ProductAdditionalCategory::new)
                .peek(category -> category.setProduct(this))
                .collect(Collectors.toSet());
    }

    public String getLocaleTitle() {
        return HasLocale.getStoreDefaultLocaleName(descriptions);
    }

    public Long getCategoryId() {
        return nullable(category, Category::getId);
    }

    public String getCategoryLocaleTitle() {
        return nullable(category, Category::getLocaleTitle);
    }

    public Long getBrandId() {
        return nullable(brand, Brand::getId);
    }

    public String getBrandName() {
        return nullable(brand, Brand::getName);
    }

    public UUID getImageId() {
        return nullable(image, Image::getId);
    }

    public String getImageSrc() {
        return nullable(image, Image::getSrc);
    }

    @PrePersist
    @PreUpdate
    public void validateAttributesMatchingCategoryRequirements() {
        Set<Category> categories = getAdditionalCategories().stream()
                .map(ProductAdditionalCategory::getCategory)
                .collect(Collectors.toSet());
        categories.add(getCategory());

        Set<Long> mandatoryAttributes = categories.stream()
                .map(Category::getAttributes)
                .flatMap(Collection::stream)
                .filter(CategoryAttribute::isMandatory)
                .map(CategoryAttribute::getAttribute)
                .map(Attribute::getId)
                .collect(Collectors.toSet());

        Set<Long> productAttributes = getAttributes().stream()
                .map(ProductAttribute::getAttribute)
                .map(Attribute::getId)
                .collect(Collectors.toSet());

        if (productAttributes.containsAll(mandatoryAttributes)) {
            return;
        }

        Set<Long> missingIds = new HashSet<>(mandatoryAttributes);
        missingIds.removeAll(productAttributes);

        throw new ApplicationException(
                ErrorCode.INVALID_CONFIGURATION,
                TranslationUtils.getMessage("exception.productAttributesMismatch", missingIds));
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy
                ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Product product = (Product) o;
        return getId() != null && Objects.equals(getId(), product.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this)
                        .getHibernateLazyInitializer()
                        .getPersistentClass()
                        .hashCode()
                : getClass().hashCode();
    }
}

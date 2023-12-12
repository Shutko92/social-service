package ru.skillbox.diplom.group42.social.service.utils;

import org.springframework.data.jpa.domain.Specification;
import ru.skillbox.diplom.group42.social.service.dto.base.BaseSearchDto;
import ru.skillbox.diplom.group42.social.service.entity.base.BaseEntity_;

import javax.persistence.metamodel.SingularAttribute;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.function.Supplier;

public class SpecificationUtil {
    private static final Specification EMPTY_SPECIFICATION = ((root, query, builder) -> null);

    public static Specification getBaseSpecification(BaseSearchDto searchDto) {
        return equal(BaseEntity_.id, searchDto.getId(), true)
                .and(equal(BaseEntity_.isDeleted, searchDto.getIsDeleted(), true));
    }

    public static <T, V> Specification<T> equal(SingularAttribute<T, V> field, V value, boolean isSkipNullValues) {
        return nullValueCheck(value, isSkipNullValues, () -> {
            return ((root, query, builder) -> {
                query.distinct(true);
                return builder.equal(root.get(field), value);
            });
        });
    }

    public static <T> Specification<T> like(SingularAttribute<T, String> field, String value, boolean isSkipNullValues) {
        return nullValueCheck(value, isSkipNullValues, () -> {
            return ((root, query, builder) -> {
                query.distinct(true);
                return builder.like(root.get(field), "%" + value + "%");
            });
        });
    }

    public static <T, V> Specification<T> in(SingularAttribute<T, V> field, Collection<V> collection, boolean isSkipNullValues) {
        return nullValueCheck(collection, isSkipNullValues, () -> {
            return ((root, query, builder) -> {
                query.distinct(true);
                return root.get(field).in(collection);
            });
        });
    }

    public static <T, V> Specification<T> notIn(SingularAttribute<T, V> field, Collection<V> value, boolean isSkipNullValues) {
        return nullValueCheck(value, isSkipNullValues, () -> {
            return (root, query, builder) -> {
                query.distinct(true);
                return root.get(field).in(value).not();
            };
        });
    }

    public static <T> Specification<T> between(SingularAttribute<T, ZonedDateTime> field, ZonedDateTime timeFrom, ZonedDateTime timeTo, boolean isSkipNullValues) {
        return SpecificationUtil.nullValueCheck(timeFrom, timeTo, isSkipNullValues, () -> {
            if (timeFrom == null && timeTo == null) {
                return ((root, query, criteriaBuilder) -> criteriaBuilder.in(root.get(field)));
            }
            if (timeFrom == null) {
                return (root, query, criteriaBuilder)
                        -> criteriaBuilder.lessThanOrEqualTo(root.get(field), timeTo);
            }
            if (timeTo == null) {
                return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(field), timeFrom));
            }
            return (root, query, criteriaBuilder)
                    -> criteriaBuilder.between(root.get(field), timeFrom, timeTo);
        });
    }

    private static <T, V> Specification<T> nullValueCheck(
            V value, V value2,
            boolean isSkipNullValues,
            Supplier<Specification<T>> supplier
    ) {
        return (value == null && value2 == null && isSkipNullValues) ? EMPTY_SPECIFICATION : supplier.get();
    }

    private static <T, V> Specification<T> nullValueCheck(
            V value,
            boolean isSkipNullValues,
            Supplier<Specification<T>> supplier
    ) {
        return (value == null && isSkipNullValues) ? EMPTY_SPECIFICATION : supplier.get();
    }

    public static <T> Specification<T> likeToLower(
            SingularAttribute<? super T, String> attribute,
            String value,
            boolean isSkipNullValues
    ) {
        return nullValueCheck(value, isSkipNullValues, () ->
                (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get(attribute)), "%" + value.toLowerCase() + "%")
        );
    }

}


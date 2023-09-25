package ru.skillbox.diplom.group42.social.service.utils;

import org.springframework.data.jpa.domain.Specification;
import ru.skillbox.diplom.group42.social.service.dto.base.BaseSearchDto;
import ru.skillbox.diplom.group42.social.service.entity.base.BaseEntity_;

import javax.persistence.metamodel.SingularAttribute;
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

    private <T> Specification<T> like(SingularAttribute<T, String> field, String value, boolean isSkipNullValues) {
        return nullValueCheck(value, isSkipNullValues, () -> {
            return ((root, query, builder) -> {
                query.distinct(true);
                return builder.like(root.get(field), "%" + value + "%");
            });
        });
    }

    private <T, V> Specification<T> in(SingularAttribute<T, V> field, Collection<V> collection, boolean isSkipNullValues) {
        return nullValueCheck(collection, isSkipNullValues, () -> {
            return ((root, query, builder) -> {
                query.distinct(true);
                return root.get(field).in(collection);
            });
        });
    }

    private static <T, V> Specification<T> nullValueCheck(
            V value,
            boolean isSkipNullValues,
            Supplier<Specification<T>> supplier
    ) {
        return (value == null && isSkipNullValues) ? EMPTY_SPECIFICATION : supplier.get();
    }
}

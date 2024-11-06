package ru.mastkey.fj_2024.lesson5.util;

import jakarta.persistence.criteria.*;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

// not perfect yet
@UtilityClass
public class FilteringUtils {
    public <T> Specification<T> getFilter(String filtering) {
        if (!StringUtils.hasText(filtering)) {
            return Specification.where(null);
        }

        Map<String, FilterCriteria> filterMap = parseFilters(filtering);

        Specification<T> spec = Specification.<T>where(null);

        for (Map.Entry<String, FilterCriteria> entry : filterMap.entrySet()) {
            spec = spec.and(buildSpecification(entry.getKey(), entry.getValue()));
        }

        return spec;
    }

    private <T> Specification<T> buildSpecification(String fieldPath, FilterCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            Path<?> fieldPathObj = getFieldPath(root, fieldPath);

            if (fieldPathObj == null) {
                return null;
            }

            return switch (criteria.operation()) {
                case "eq" -> buildEq(fieldPathObj, criteriaBuilder, criteria.value);
                case "like" -> buildLike(fieldPathObj, criteriaBuilder, criteria.value);
                case "lt" -> buildLt(fieldPathObj, criteriaBuilder, criteria.value);
                case "le" -> buildLe(fieldPathObj, criteriaBuilder, criteria.value);
                case "gt" -> buildGt(fieldPathObj, criteriaBuilder, criteria.value);
                case "ge" -> buildGe(fieldPathObj, criteriaBuilder, criteria.value);
                default -> null;
            };
        };
    }

    private Predicate buildEq(Path<?> fieldPathObj, CriteriaBuilder criteriaBuilder, Object value) {
        return criteriaBuilder.equal(fieldPathObj, value);
    }

    private Predicate buildLike(Path<?> fieldPathObj, CriteriaBuilder criteriaBuilder, Object value) {
        return criteriaBuilder.like(fieldPathObj.as(String.class), "%" + value.toString() + "%");
    }

    private Predicate buildLt(Path<?> fieldPathObj, CriteriaBuilder criteriaBuilder, Object value) {
        if (fieldPathObj.getJavaType().equals(LocalDate.class)) {
            LocalDate date = LocalDate.parse(value.toString());
            return criteriaBuilder.lessThan(fieldPathObj.as(LocalDate.class), date);
        }
        return criteriaBuilder.lessThan(fieldPathObj.as(Long.class), Long.valueOf(value.toString()));
    }

    private Predicate buildLe(Path<?> fieldPathObj, CriteriaBuilder criteriaBuilder, Object value) {
        if (fieldPathObj.getJavaType().equals(LocalDate.class)) {
            LocalDate date = LocalDate.parse(value.toString());
            return criteriaBuilder.lessThanOrEqualTo(fieldPathObj.as(LocalDate.class), date);
        }
        return criteriaBuilder.lessThanOrEqualTo(fieldPathObj.as(Long.class), Long.valueOf(value.toString()));
    }

    private Predicate buildGt(Path<?> fieldPathObj, CriteriaBuilder criteriaBuilder, Object value) {
        if (fieldPathObj.getJavaType().equals(LocalDate.class)) {
            LocalDate date = LocalDate.parse(value.toString());
            return criteriaBuilder.greaterThan(fieldPathObj.as(LocalDate.class), date);
        }
        return criteriaBuilder.greaterThan(fieldPathObj.as(Long.class), Long.valueOf(value.toString()));
    }

    private Predicate buildGe(Path<?> fieldPathObj, CriteriaBuilder criteriaBuilder, Object value) {
        if (fieldPathObj.getJavaType().equals(LocalDate.class)) {
            LocalDate date = LocalDate.parse(value.toString());
            return criteriaBuilder.greaterThanOrEqualTo(fieldPathObj.as(LocalDate.class), date);
        }
        return criteriaBuilder.greaterThanOrEqualTo(fieldPathObj.as(Long.class), Long.valueOf(value.toString()));
    }

    private Map<String, FilterCriteria> parseFilters(String filtering) {
        Map<String, FilterCriteria> filters = new HashMap<>();
        String[] filterParts = filtering.split(",");

        for (String part : filterParts) {
            String[] keyValueOp = part.split(" ");
            if (keyValueOp.length == 3) {
                filters.put(keyValueOp[0], new FilterCriteria(keyValueOp[1], keyValueOp[2]));
            }
        }

        return filters;
    }

    private <T> Path<?> getFieldPath(Root<T> root, String fieldPath) {
        String[] fieldParts = fieldPath.split("\\.");

        Path<?> path = root;
        for (int i = 0; i < fieldParts.length; i++) {
            String part = fieldParts[i];

            if (i == fieldParts.length - 1) {
                path = path.get(part);
            } else {
                path = root.getJoins().stream()
                        .filter(j -> j.getAttribute().getName().equals(part))
                        .findFirst()
                        .orElseGet(() -> (Join<T, ?>) root.fetch(part, JoinType.LEFT));
            }
        }
        return path;
    }

    private record FilterCriteria(String operation, String value) {
    }
}

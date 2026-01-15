package com.sebastian.inventory_management.util;

import java.util.Optional;
import java.util.function.Function;

public final class ValidationHelper {

    private ValidationHelper() {
    }

    public static <E, ID> void validateUniqueName(
            Optional<E> existing,
            ID excludeId,
            Function<E, ID> idExtractor,
            String entityName,
            String name) {
        existing.ifPresent(entity -> {
            if (excludeId == null || !idExtractor.apply(entity).equals(excludeId)) {
                throw new IllegalArgumentException(
                        entityName + " with name '" + name + "' already exists.");
            }
        });
    }

    public static <E, ID> void validateUniqueField(
            Optional<E> existing,
            ID excludeId,
            Function<E, ID> idExtractor,
            String entityName,
            String fieldName,
            String fieldValue) {
        existing.ifPresent(entity -> {
            if (excludeId == null || !idExtractor.apply(entity).equals(excludeId)) {
                throw new IllegalArgumentException(
                        entityName + " with " + fieldName + " '" + fieldValue + "' already exists.");
            }
        });
    }
}

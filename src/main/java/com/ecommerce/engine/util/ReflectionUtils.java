package com.ecommerce.engine.util;

import com.ecommerce.engine.VaadinFormsRegistrar;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.repository.ListCrudRepository;

import java.lang.reflect.Field;
import java.util.Optional;

@UtilityClass
public class ReflectionUtils {

    public static boolean isToOneColumn(Field field) {
        return field.isAnnotationPresent(OneToOne.class) || field.isAnnotationPresent(ManyToOne.class);
    }

    @SuppressWarnings("rawtypes")
    public static ListCrudRepository<?, ?> findListCrudRepositoryByGenericType(Class<?> entityType) {
        for (ListCrudRepository<?, ?> bean : VaadinFormsRegistrar.getAllRepositories()) {
            Class<?>[] resolveTypeArguments = GenericTypeResolver.resolveTypeArguments(bean.getClass(), ListCrudRepository.class);
            Class<?> entityGenericType = resolveTypeArguments != null ? resolveTypeArguments[0] : null;

            if (entityGenericType != null && entityGenericType.equals(entityType)) {
                return bean;
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> findEntityTypeByRepository(ListCrudRepository<T, ?> bean) {
        Class<?>[] resolveTypeArguments = GenericTypeResolver.resolveTypeArguments(bean.getClass(), ListCrudRepository.class);

        return resolveTypeArguments != null ? (Class<T>) resolveTypeArguments[0] : null;
    }

    @SuppressWarnings("unchecked")
    public static <ID> Class<ID> findIdTypeByAddForm(ListCrudRepository<?, ID> bean) {
        Class<?>[] resolveTypeArguments = GenericTypeResolver.resolveTypeArguments(bean.getClass(), ListCrudRepository.class);

        return resolveTypeArguments != null ? (Class<ID>) resolveTypeArguments[1] : null;
    }

    @SneakyThrows
    public static <ID> ID getEntityId(Object entity, Class<ID> idClass) {
        Class<?> clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);
                return idClass.cast(field.get(entity));
            }
        }

        throw new IllegalArgumentException("No field annotated with @Id found.");
    }

    @SuppressWarnings("unchecked")
    public static <ID> ID castStringToIdType(String stringId, Class<ID> idClass) {
        if (idClass.equals(String.class)) {
            return (ID) stringId;
        }
        if (idClass.equals(Long.class)) {
            return (ID) (Long) Long.parseLong(stringId);
        }
        if (idClass.equals(Integer.class)) {
            return (ID) (Integer) Integer.parseInt(stringId);
        }

        return null;
    }

    public static boolean isSupportedId(Class<?> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();

        Field idField = null;
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                idField = field;
                break;
            }
        }

        if (idField == null) {
            return false;
        }

        Class<?> idClass = idField.getDeclaringClass();

        return idClass.equals(String.class) || idClass.equals(Long.class) || idClass.equals(Integer.class);
    }

    public static String getTableName(Class<?> entityClass) {
        Table tableAnnotation = entityClass.getAnnotation(Table.class);

        return Optional.ofNullable(tableAnnotation)
                .map(Table::name)
                .filter(name -> !name.isEmpty())
                .orElseGet(() -> TextUtils.camelToSnakeCase(entityClass.getSimpleName()));
    }

}

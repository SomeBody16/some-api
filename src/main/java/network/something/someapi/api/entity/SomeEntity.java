package network.something.someapi.api.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SomeEntity {
    String modId();

    String entityId();

    @Retention(value = RUNTIME)
    @Target(value = METHOD)
    @interface Type {
    }

    @Retention(value = RUNTIME)
    @Target(value = METHOD)
    @interface Attributes {
    }
}

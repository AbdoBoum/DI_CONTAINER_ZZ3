package com.company.Anotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({ METHOD, CONSTRUCTOR, FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Inject {
}

package com.company.Injector;

import com.company.Anotations.Singleton;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

public interface ContainerInterface {

    <T> T getBeanInstance(Class<T> clazz);

    <T> void bind(Class<T> interfaceClass, Class<? extends T> implementationClass);

    static Constructor<?>[] getAllConstructors(Class<?> clazz) {
        return clazz.getDeclaredConstructors();
    }

    static boolean isAbstractClass(Class clazz) {
        return Modifier.isAbstract(clazz.getModifiers());
    }

    default boolean isSingleton(Class clazz) {return clazz.isAnnotationPresent(Singleton.class);}

}
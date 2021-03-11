package com.company.Injector;

import com.company.Anotations.Inject;
import com.company.Anotations.Service;
import org.apache.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class Container implements ContainerInterface {

    private static final String ERROR_MSG = "Test fail because of binding error. ";

    private Map<Class, Class> interfaceMappings;

    private Set<Class> historicRequests;

    private Set<Class> instantiableClasses;

    private Set<Class> singletonClasses;

    private Map<Class, Object> singletonInstance;

    Reflections reflections;

    Logger logger = Logger.getLogger(Container.class);

    /** instantiate a container
     * @val interfaceMappings: store interfaces and their implementation
     * @val historicRequests: store classes that were requested to be instantiated - used to detect cycle in dependencies
     * @val instantiated: store classes that were instantiated before
     * @val singletonClasses: store classes annotated with @Singleton
     * @val singletonInstance: store a unique instance for each class annotated with @Singleton
     */
    public Container() {
        interfaceMappings = new HashMap<>();
        historicRequests = new HashSet<>();
        instantiableClasses = new HashSet<>();
        singletonInstance = new HashMap<>();
        singletonClasses = new HashSet<>();
        reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forJavaClassPath())
        );
        scanClasses();
    }

    public <T> void scanClasses() {
        Set<Class<?>> annotated =
                reflections.getTypesAnnotatedWith(Service.class);
        for (Class<?> clazz: annotated) {
            Class<?>[] interfaces = clazz.getInterfaces();
            if (interfaces.length != 0)
                for(Class<?> interfaceClass: interfaces) bind((Class<T>)interfaceClass, (Class<T>)clazz);

        }
    }

    @Override
    public <T> T getBeanInstance(Class<T> interfaceClass) {
        try {
            Class<T> clazz = interfaceClass;

            if (interfaceClass.isInterface()) {
                logger.debug(interfaceClass.getName() + " is an interface");
                if (interfaceMappings.containsKey(interfaceClass)) {
                    logger.debug(interfaceMappings + " contains the interface: " + interfaceClass.getName());
                    clazz = interfaceMappings.get(interfaceClass);
                } else {
                    throw new ContainerException(ERROR_MSG
                            + "No implementation found for this interface.");
                }
            }

            /**
             * If the class is already requested but can't be instantiated, we have a cyclic graph dependency
             */
            if (historicRequests.contains(clazz)) {
                if (!instantiableClasses.contains(clazz)) {
                    throw new ContainerException(ERROR_MSG + "A cyclic dependency was detected.");
                }
            } else {
                historicRequests.add(clazz);
            }
            /**
             * if it's a singleton class we return directly the implementation
             */
            if (singletonInstance.containsKey(clazz)) {
                return (T)singletonInstance.get(clazz);
            }

            return (T)createNewInstance(clazz);
        } catch (ContainerException e) {
            throw new ContainerException(ERROR_MSG + e.getMessage());
        }
    }

    private <T> Object createNewInstance(Class<T> clazz) {
        Constructor<T> constructor = findConstructor(clazz);

        if(constructor != null) {
            Parameter[] parameters = constructor.getParameters();

            List<Object> arguments = Arrays.stream(parameters)
                    .map(param -> getBeanInstance(param.getType()))
                    .collect(Collectors.toList());

            try {
                T implementationClass = constructor.newInstance(arguments.toArray());

                if (!instantiableClasses.contains(clazz)) instantiableClasses.add(clazz);
                if (isSingleton(clazz)) singletonInstance.put(clazz, implementationClass);
                return implementationClass;
            } catch (Exception e) {
                throw new ContainerException(
                        ERROR_MSG + "An Exception was thrown during the instantiation.", e);
            }
        }
            try {
                T implementationClass = clazz.newInstance();

                if (!instantiableClasses.contains(clazz)) instantiableClasses.add(clazz);
                if (isSingleton(clazz)) singletonInstance.put(clazz, implementationClass);
                return implementationClass;

            } catch (Exception e) {
                throw new ContainerException(
                        ERROR_MSG + "An Exception was thrown during the instantiation.", e);
            }
    }

    /**
     * find the constructor to use to instantiate the class
     * @param clazz: the class where we search for the constructor
     * @return
     */
    private <T> Constructor<T> findConstructor(Class<T> clazz) {
        Constructor<?>[] declaredConstructors = ContainerInterface.getAllConstructors(clazz);
        /**
         * If we find one public constructor we return it
         */
        if (declaredConstructors.length == 1) {
            return (Constructor<T>) declaredConstructors[0];
        }

        /**
         * If we find more than one public constructor we search for the one annotated with @Inject
         */
        if (declaredConstructors.length > 1) {
            List<Constructor<?>> constructorsWithInject = Arrays.stream(declaredConstructors)
                    .filter(c -> c.isAnnotationPresent(Inject.class))
                    .collect(Collectors.toList());

            if (constructorsWithInject.isEmpty()) {
                throw new ContainerException(ERROR_MSG +
                        "Found more than one public constructor. Specify which one to choose by annotating it with @Inject");
            }

            if (constructorsWithInject.size() != 1) {
                throw new ContainerException(ERROR_MSG +
                        "Found more than one public constructor annotated with @Inject.");
            }

            return (Constructor<T>) constructorsWithInject.get(0);
        }
        return null;
    }

    /**
     * Bind an interface to its implementation
     * @param interfaceClass: The interface we want to bind
     * @param implementationClass: The implementation of the interface
     */
    @Override
    public <T> void bind(Class<T> interfaceClass, Class<? extends T> implementationClass) {
        if (!interfaceClass.isInterface()) throw new IllegalStateException("[" + interfaceClass.getName() + "] is not an interface.");
        if (implementationClass.isInterface() || ContainerInterface.isAbstractClass(implementationClass)) throw new ContainerException("[" + implementationClass.getName() + "] should not be an interface or abstract class.");
        interfaceMappings.put(interfaceClass, implementationClass);
    }

}

//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package de.micromata.genome.util.runtime;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.util.bean.PrivateBeanUtils;

/**
 * The Class ClassUtils.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class ClassUtils extends org.apache.commons.lang.ClassUtils
{

  /**
   * Find first method with given name.
   *
   * @param clazz the clazz
   * @param method the method
   * @return null if not found
   */
  public static Method findFirstMethod(Class<?> clazz, String method)
  {
    for (Method m : clazz.getDeclaredMethods()) {
      if (m.getName().equals(method) == true) {
        return m;
      }
    }
    if (clazz.getSuperclass() != null) {
      return findFirstMethod(clazz.getSuperclass(), method);
    }
    return null;
  }

  /**
   * Returns all 'visible' methods for the given class. Visible methods are:
   *
   * - own methods (clazz.getDeclaredMethods()) - all public and protected methods from it's inheritance hierarchy
   *
   * @param clazz the Class
   * @return set of visible methods for that class
   */
  public static Set<Method> getAllVisibleMethods(final Class<?> clazz)
  {
    Set<Method> allMethods = new HashSet<>();
    allMethods.addAll(Arrays.asList(clazz.getMethods()));
    allMethods.addAll(Arrays.asList(clazz.getDeclaredMethods()));

    for (Object obj : ClassUtils.getAllSuperclasses(clazz)) {
      Class aClass = (Class) obj;
      for (Method method : aClass.getDeclaredMethods()) {
        if (Modifier.isProtected(method.getModifiers())) {
          allMethods.add(method);
        }
      }
    }
    return allMethods;
  }

  /**
   * Reads the generic type of the given class at the given index.
   *
   * Note: this doesn't work, if class MyClass implements Super<MyConcreteType>
   *
   * @param <T> the generic type
   * @param clazz the class with generic types
   * @param index the index of the generic type
   * @return the generic type or null
   */
  public static Class<?> getGenericTypeArgument(Class<?> clazz, int index)
  {
    Type genericSuperclass = clazz.getGenericSuperclass();
    return findGenericTypeArgument(genericSuperclass, index);
  }

  public static Class<?> findGenericTypeArgument(Type genericSuperclass, int index)
  {
    // CHECKSTYLE.OFF SIMULIERTE_POLYMORPHIE Necesssary for technical reasons (low level handling).
    while (genericSuperclass != null && !(ParameterizedType.class.isAssignableFrom(genericSuperclass.getClass()))) {
      genericSuperclass = ((Class) genericSuperclass).getGenericSuperclass();
    }
    // CHECKSTYLE.ON
    if (genericSuperclass instanceof ParameterizedType) {
      return (Class<?>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[index];
    }
    return null;
  }

  /**
   * Find generic type from method.
   *
   * @param clazz the clazz
   * @param method the method
   * @param pos the pos
   * @return the class
   */
  public static Class<?> findGenericTypeFromMethod(Class<?> clazz, Method method, int pos)
  {
    java.lang.reflect.Type gent = method.getGenericReturnType();
    Class<?> gentype = ClassUtils.findGenericTypeArgument(gent, pos);
    return gentype;
  }

  /**
   * Find generic type from field.
   *
   * @param clazz the clazz
   * @param field the field
   * @param pos the pos
   * @return the class
   */
  public static Class<?> findGenericTypeFromField(Class<?> clazz, Field field, int pos)
  {
    java.lang.reflect.Type gent = field.getGenericType();
    Class<?> gentype = ClassUtils.findGenericTypeArgument(gent, pos);
    return gentype;
  }

  /**
   * Find generic type from property.
   *
   * @param clazz the clazz
   * @param prop the prop
   * @return the class
   */
  public static Class<?> findGenericTypeFromProperty(Class<?> clazz, String prop, int pos)
  {
    Method method = ClassUtils.findMethod(clazz, PrivateBeanUtils.getGetterMethodNameFromFieldName(prop));
    if (method != null) {
      return findGenericTypeFromMethod(clazz, method, pos);
    }
    Field field = PrivateBeanUtils.findField(clazz, prop);
    if (field != null) {
      return findGenericTypeFromField(clazz, field, pos);
    }
    return null;
  }

  /**
   * Looks for the concreate Class of a generic of given classRequested.
   *
   * This implementation looks first in super classes and then in super interfaces.
   *
   * @param <T> the generic type
   * @param clazz concrete class
   * @param classRequested class or interface implementing it.
   * @return null if not found.
   */
  public static <T> Class<T> getGenericTypeArgument(Class<?> clazz, Class<T> classRequested) // NOSONAR "Methods should not be too complex" trivial
  {
    Type genericSuperclass = clazz.getGenericSuperclass();
    if (genericSuperclass != null) {
      Class<T> ret = getGenericTypeArgumentFromGenericSuperType(genericSuperclass, classRequested);
      if (ret != null) {
        return ret;
      }
    }

    Type[] genericInterfaces = clazz.getGenericInterfaces();
    if (genericInterfaces == null) {
      return null;
    }
    for (Type genericInterface : genericInterfaces) {
      Class<T> ret = getGenericTypeArgumentFromGenericSuperType(genericInterface, classRequested);
      if (ret != null) {
        return ret;
      }
    }
    Class<?> superClazz = clazz.getSuperclass();
    if (superClazz != null) {
      return getGenericTypeArgument(superClazz, classRequested);
    }
    return null;
  }

  /**
   * Looks if generic supertype is paramized with given baseRequested.
   *
   * @param <T> the generic type
   * @param genericSuperclass the generic superclass
   * @param baseRequested the base requested
   * @return the generic type argument from generic super type. null if not found.
   */
  //CHECKSTYLE.OFF FinalParameter Precondition cast
  public static <T> Class<T> getGenericTypeArgumentFromGenericSuperType(Type genericSuperclass, Class<T> baseRequested)
  {
    Type loopVar = genericSuperclass;
    while (loopVar != null && !(ParameterizedType.class.isAssignableFrom(loopVar.getClass()))) {
      loopVar = ((Class<?>) loopVar).getGenericSuperclass();
    }
    if (loopVar != null && ParameterizedType.class.isAssignableFrom(loopVar.getClass())) {
      Type[] typeArgs = ((ParameterizedType) loopVar).getActualTypeArguments();
      for (Type typeArg : typeArgs) {
        if (typeArg instanceof ParameterizedType) {
          typeArg = ((ParameterizedType) typeArg).getRawType();
        }
        if ((typeArg instanceof Class) == false) {
          continue;
        }
        if (baseRequested.isAssignableFrom((Class<?>) typeArg) == false) {
          continue;
        }
        return (Class<T>) typeArg;
      }
    }
    return null;
  }

  /**
   * Collects all super classes, implementing given class.
   *
   * @param <T> the generic type
   * @param clazz the clazz
   * @param implementing the implementing
   * @return the all super implementing
   */
  public static <T> Set<Class<? extends T>> getAllSuperImplementing(Class<? extends T> clazz, Class<T> implementing)
  {
    Set<Class<? extends T>> ret = new HashSet<>();
    collectAllSuperImplementing(clazz, implementing, ret);
    return ret;
  }

  /**
   * Collect all super implementing.
   *
   * @param <T> the generic type
   * @param clazz the clazz
   * @param implementing the implementing
   * @param list the list
   */
  public static <T> void collectAllSuperImplementing(Class<?> clazz, Class<T> implementing, //
      Set<Class<? extends T>> list)
  {
    if (clazz == null) {
      return;
    }
    if (implementing.isAssignableFrom(clazz) == false) {
      return;
    }
    list.add((Class<? extends T>) clazz);
    collectAllSuperImplementing(clazz.getSuperclass(), implementing, list);
    for (Class<?> ifaces : clazz.getInterfaces()) {
      collectAllSuperImplementing(ifaces, implementing, list);
    }
  }

  /**
   * Converts name of an enum to enum.
   *
   * If empty or cannot be convertet, returns default.
   *
   * @param <T> the generic type
   * @param defaultValue the default value
   * @param name the name
   * @return the enum value
   */
  public static <T extends Enum<?>> T getEnumValue(T defaultValue, String name)
  {
    if (StringUtils.isEmpty(name) == true) {
      return defaultValue;
    }
    try {
      return (T) PrivateBeanUtils.invokeStaticMethod(defaultValue.getDeclaringClass(), "valueOf", name);
      //CHECKSTYLE.OFF IllegalCatchCheck Buttet proof reflection
    } catch (Exception ex) { // NOSONAR "Illegal Catch" framework
      return defaultValue;
    }
    //CHECKSTYLE.ON
  }

  /**
   * Gets all fields from a class. Keys are the field names as strings.
   *
   * @param type the type
   * @return the all fields
   */
  //CHECKSTYLE.OFF FinalParameter Precondition cast.
  public static Map<String, Field> getAllFields(final Class<?> type)
  {

    Map<String, Field> fieldMap = new HashMap<>();
    for (Field field : type.getDeclaredFields()) {
      fieldMap.put(field.getName(), field);
    }
    Class<?> nextType = type.getSuperclass();

    while (nextType != Object.class) {
      for (Field field : nextType.getDeclaredFields()) {
        fieldMap.put(field.getName(), field);
      }
      nextType = nextType.getSuperclass();
    }
    //CHECKSTYLE.ON
    return fieldMap;
  }

  /**
   * Set all String field to empty on current class.
   *
   * @param object the object
   * @param exceptClass the except class
   * @throws IllegalArgumentException the illegal argument exception
   * @throws IllegalAccessException the illegal access exception
   */
  // CHECKSTYLE.OFF com.puppycrawl.tools.checkstyle.checks.metrics.CyclomaticComplexityCheck Trivial code.
  public static void fillDefaultEmptyFieldsIfEmpty(Object object, Class<?>... exceptClass) // NOSONAR "Methods should not be too complex" trivial
      throws IllegalArgumentException, IllegalAccessException
  {
    Class<?> currentClazz = object.getClass();
    List exClazzes = Arrays.asList(exceptClass);
    while (currentClazz.getSuperclass() != null) {
      if (exClazzes.contains(currentClazz) == false) {
        Field[] fields = currentClazz.getDeclaredFields();
        for (Field field : fields) {
          if (String.class.equals(field.getType())) {
            field.setAccessible(true);
            if (field.get(object) == null) {
              field.set(object, "");
            }
          }
          if (Integer.class.equals(field.getType())) {
            field.setAccessible(true);
            if (field.get(object) == null) {
              field.set(object, 0);
            }
          }
          if (BigDecimal.class.equals(field.getType())) {
            field.setAccessible(true);
            if (field.get(object) == null) {
              field.set(object, BigDecimal.ZERO);
            }
          }
          if (Date.class.equals(field.getType())) {
            field.setAccessible(true);
            if (field.get(object) == null) {
              field.set(object, new Date());
            }
          }
        }
      }
      currentClazz = currentClazz.getSuperclass();
    }
  }
  // CHECKSTYLE.ON

  /**
   * Find class annotations.
   *
   * @param <T> the generic type
   * @param clazz the clazz
   * @param annotClass the annot class
   * @return the list
   */
  public static <T extends Annotation> List<T> findClassAnnotations(Class<?> clazz, Class<T> annotClass)
  {
    List<T> annots = new ArrayList<>();
    Set<Class<?>> visitedClasses = new HashSet<>();
    collectClassAnnotations(clazz, annotClass, annots, visitedClasses);
    return annots;
  }

  /**
   * Collect class annotations.
   *
   * @param <T> the generic type
   * @param clazz the clazz
   * @param annotClass the annot class
   * @param annots the annots
   * @param visitedClasses the visited classes
   */
  public static <T extends Annotation> void collectClassAnnotations(Class<?> clazz, Class<T> annotClass, List<T> annots,
      Set<Class<?>> visitedClasses)
  {
    if (visitedClasses.contains(clazz) == true) {
      return;
    }
    visitedClasses.add(clazz);
    T[] ana = clazz.getAnnotationsByType(annotClass);
    if (ana != null) {
      for (T an : ana) {
        annots.add(an);
      }
    }
    Class<?> superclz = clazz.getSuperclass();
    if (superclz == null || superclz == Object.class) {
      return;
    }
    collectClassAnnotations(superclz, annotClass, annots, visitedClasses);
    Class<?>[] ifaces = clazz.getInterfaces();
    if (ifaces == null || ifaces.length == 0) {
      return;
    }
    for (Class<?> iclazz : ifaces) {
      collectClassAnnotations(iclazz, annotClass, annots, visitedClasses);
    }
  }

  /**
   * Finds a method.
   * 
   * @param clazz
   * @param name
   * @param argumentTypes
   * @return null if not found.
   */
  public static Method findMethod(Class<?> clazz, String name, Class<?>... argumentTypes)
  {
    try {
      return clazz.getMethod(name, argumentTypes);
    } catch (SecurityException | NoSuchMethodException e) {
      return null;
    }
  }
}

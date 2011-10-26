package ee.era.hangman.di;

import static java.lang.Class.forName;
import static java.lang.System.getProperty;

public class DependencyInjection {
  @SuppressWarnings("unchecked")
  public static <T> T inject(Class<T> beanClass) {
    String beanClassName = getProperty(beanClass.getName());
    try {
      if (beanClassName == null) {
        return beanClass.newInstance();
      }
      else {
        return (T) forName(beanClassName).newInstance();
      }
    } catch (Exception e) {
      throw new IllegalArgumentException("Cannot initialize bean " + beanClassName, e);
    }
  }

  public static <T> void wire(Class<T> interfaceClass, Class<? extends T> beanClass) {
    System.setProperty(interfaceClass.getName(), beanClass.getName());
  }
}

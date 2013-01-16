/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package multiface.crypto.cr2;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Diego64
 */
public class SerializationCR2 {

    /**
     * Serializzazione di una collezione di Oggetti JPA
     * @param coll Oggetto da serializzare
     * @param packageJpa Array di parametri da escludere
     * @return Lista di Mappe di oggetti JPA 
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws IOException 
     */
    public static List<Map<String, Object>> serializeJpaCollectionObject(Collection coll, String packageJpa)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, IOException {
        return SerializationCR2.serializeJpaCollectionObject(coll, new String[]{}, packageJpa);
    }

    /**
     * Serializzazione di una collezione di Oggetti JPA
     * @param coll Oggetto da serializzare
     * @param exclude_params Array di parametri da escludere
     * @param packageJpa Package delle JPA
     * @return Lista di Mappe di oggetti JPA 
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws IOException 
     */
    public static List<Map<String, Object>> serializeJpaCollectionObject(Collection coll, String[] exclude_params, String packageJpa)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, IOException {
        return serializeJpaCollectionObject(coll, exclude_params, packageJpa, Integer.MAX_VALUE);
    }

    /**
     * Serializzazione di una collezione di Oggetti JPA
     * @param coll Oggetto da serializzare
     * @param exclude_params Array di parametri da escludere
     * @param packageJpa Package delle JPA
     * @param maxlevel Livello massimo di profondità di ricorsione
     * @return Lista di Mappe di oggetti JPA 
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws IOException 
     */
    public static List<Map<String, Object>> serializeJpaCollectionObject(Collection coll, String[] exclude_params, String packageJpa, int maxlevel)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, IOException {
        List<Map<String, Object>> lmap = new ArrayList<Map<String, Object>>(coll.size());

        for (Object o : coll) {
            lmap.add(serializeJPAObject(o, exclude_params, packageJpa, new LinkedList<Class>(), maxlevel, 0));
        }
        return lmap;
    }

    /**
     * Serializzazione dell'oggetto singolo JPA
     * @param object Oggetto da serializzare
     * @param exclude_params Array di parametri da escludere
     * @param packageJpa Package delle JPA
     * @param classList Lista delle classi gia fatte dalla ricorsione
     * @param maxlevel Livello massimo di profondità di ricorsione
     * @param level Livello attuale di profondità di ricorsione
     * @return Mappa rappresentativa dell'oggetto JPA
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws IOException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException 
     */
    public static Map<String, Object> serializeJPAObject(Object object, String[] exclude_params, String packageJpa, List<Class> classList, int maxlevel, int level)
            throws IllegalAccessException, ClassNotFoundException, IOException, IllegalArgumentException, InvocationTargetException {

        List l = new LinkedList();
        Set s = new HashSet();
        Collection c = new LinkedList();

        Map<String, Object> map = new HashMap<String, Object>();

        Method[] declaredMethods = object.getClass().getDeclaredMethods();

        for (int i = 0; i < declaredMethods.length; i++) {
            // Contollo solo i metodi che iniziano per "get"
            if (declaredMethods[i].getName().charAt(0) == 'g'
                    && declaredMethods[i].getName().charAt(1) == 'e'
                    && declaredMethods[i].getName().charAt(2) == 't') {
                // Controllo se l'istanza non sia una lista
                if (!declaredMethods[i].getReturnType().isInstance(l)
                        && !declaredMethods[i].getReturnType().isInstance(s)
                        && !declaredMethods[i].getReturnType().isInstance(c)) {

                    // Nome del parametero
                    String nameParameter = declaredMethods[i].getName().replaceAll("get", "").toLowerCase();

                    //Controllo se il paramtro non sia stato escluso
                    if (!SerializationCR2.isInTo(nameParameter, exclude_params)) {
                        Object obj = declaredMethods[i].invoke(object, new Object[]{});
                        if (obj != null) {
                            if (packageJpa == null) {
                                parsingObjectJPA(obj, map, nameParameter);
                            } else {
                                // Se l'oggetto è presente nel package delle JPA 
                                if (SerializationCR2.classInstanceOfInToPackage(packageJpa, obj)) {
                                    //Se l'oggetto non è presente nella lista delle classi gia ricorsivamente "chiamate"
                                    if (!SerializationCR2.classIntToList(classList, obj)) {
                                        // se il livello attutale non sia superiore al livello specificato come massimo
                                        if (maxlevel > level) {
                                            // Aggiungo la classe nella lista per evitare ricorsioni infinite
                                            classList.add(obj.getClass());
                                            // Lancio la ricorsione sull'oggetto
                                            map.put(nameParameter, serializeJPAObject(obj, exclude_params, packageJpa, classList, maxlevel, level + 1));
                                        } else {
                                            //MEtto nella mappa il toString dell'oggetto
                                            map.put(nameParameter, obj.toString());
                                        }
                                    }
                                } else {
                                    parsingObjectJPA(obj, map, nameParameter);
                                }
                            }
                        } else {
                            // obj Null;
                            map.put(nameParameter, obj);
                        }
                    }
                } else {
                    //System.out.println(declaredMethods[i].getReturnType());
                }
            }
        }
        return map;
    }

    //#################### METODI PRIVATI  AUSILIARI #################################
    /**
     *
     * @param key
     * @param exclude_params
     * @return
     */
    private static boolean isInTo(String key, String[] exclude_params) {
        for (int i = 0; i < exclude_params.length; i++) {
            if (key.equalsIgnoreCase(exclude_params[i])) {
                return true;
            }
        }
        return false;
    }

    private static boolean classInstanceOfInToPackage(String packageName, Object obj) throws ClassNotFoundException, IOException {
        List<Class> classi = getClasses(packageName);
        for (Class c : classi) {
            //System.out.println("   if(" + c.getName() + " == " + obj.getClass().getName() + ")");
            if (c.isInstance(obj)) {
                return true;
            }
        }
        return false;

    }

    private static boolean classIntToList(List<Class> list, Object obj) {
        for (Class c : list) {
            if (c.isInstance(obj)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Scans all classes accessible from the context class loader which belong
     * to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private static List<Class> getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        ArrayList<Class> c2 = new ArrayList<Class>();
        for (Class c : classes) {
            // Questi if servono per eliminare cle classi RunTime JPA che sono quelle con la fine _ ( underscore) e 
            // le sotto classi, quindi con un numero diverso di . (dot) +1 del package richiesto
            if (!c.getName().contains("_") && (packageName.split("\\.").length + 1) == c.getName().split("\\.").length) {
                c2.add(c);
            }
        }

        return c2;
    }

    /**
     * Recursive method used to find all classes in a given directory and
     * subdirs.
     *
     * @param directory The base directory
     * @param packageName The package name for classes found inside the base
     * directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    private static void parsingObjectJPA(Object obj, Map<String, Object> map, String nameParameter) {
        if (obj instanceof java.util.Date) {
            Date d = (Date) obj;
            map.put(nameParameter, d.getTime());
        } else if (obj.getClass().isInstance(new byte[]{})) {
            String firma = CryptoCR2.encodeHEX((byte[]) obj);
            map.put(nameParameter, firma);
        } else if (obj.getClass().isPrimitive()) {
            map.put(nameParameter, obj);
        } else {
            map.put(nameParameter, obj.toString());
        }
    }
}

import net.mindview.atunit.ClassNameFinder;
import net.mindview.atunit.Test;
import net.mindview.atunit.TestObjectCleanup;
import net.mindview.atunit.TestObjectCreate;
import net.mindview.util.BinaryFile;
import net.mindview.util.ProcessFiles;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static net.mindview.util.Print.print;
import static net.mindview.util.Print.printnb;

/**
 * @Author ZhangGJ
 * @Date 2019/10/04
 */
public class E11_AtUnit2 implements ProcessFiles.Strategy {
    static Class<?> testClass;
    static List<String> failedTests = new ArrayList<String>();
    static long testsRun = 0;
    static long failures = 0;

    public static void main(String[] args) throws Exception {
        ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true); // Enable asserts
        new ProcessFiles(new E11_AtUnit2(), "class").start(args);
        if (failures == 0)
            print("OK (" + testsRun + " tests)");
        else {
            print("(" + testsRun + " tests)");
            print("\n>>> " + failures + " FAILURE" + (failures > 1 ? "S" : "") + " <<<");
            for (String failed : failedTests)
                print("  " + failed);
        }
    }

    public void process(File cFile) {
        try {
            String cName = ClassNameFinder.thisClass(BinaryFile.read(cFile));
            if (!cName.contains("."))
                return; // Ignore unpackaged classes
            testClass = Class.forName(cName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        TestMethods testMethods = new TestMethods();
        HashMap<Method, String> testNotes = new HashMap<Method, String>();
        Method creator = null;
        Method cleanup = null;
        for (Method m : testClass.getDeclaredMethods()) {
            testMethods.addIfTestMethod(m);
            // Check to see whether @TestNote is present, and used
            // correctly.
            TestNote testNote;
            if ((testNote = m.getAnnotation(TestNote.class)) != null) {
                if (m.getAnnotation(Test.class) == null)
                    throw new RuntimeException("@TestNote method" + " must be a @Test method, too");
                testNotes.put(m, testNote.value());
            }
            if (creator == null)
                creator = checkForCreatorMethod(m);
            if (cleanup == null)
                cleanup = checkForCleanupMethod(m);
        }
        if (testMethods.size() > 0) {
            if (creator == null)
                try {
                    if (!Modifier.isPublic(testClass.getDeclaredConstructor().getModifiers())) {
                        print("Error: " + testClass + " default constructor must be public");
                        System.exit(1);
                    }
                } catch (NoSuchMethodException e) {
                    // Synthesized default constructor; OK
                }
            print(testClass.getName());
        }
        for (Method m : testMethods) {
            printnb("  . " + m.getName() + " ");
            if (testNotes.containsKey(m))
                printnb(" : " + testNotes.get(m) + " ");
            try {
                Object testObject = createTestObject(creator);
                boolean success = false;
                try {
                    if (m.getReturnType().equals(boolean.class))
                        success = (Boolean) m.invoke(testObject);
                    else {
                        m.invoke(testObject);
                        success = true; // If no assert fails
                    }
                } catch (InvocationTargetException e) {
                    // Actual exception is inside e:
                    print(e.getCause());
                }
                print(success ? "" : "(failed)");
                testsRun++;
                if (!success) {
                    failures++;
                    failedTests.add(testClass.getName() + ": " + m.getName());
                }
                if (cleanup != null)
                    cleanup.invoke(testObject, testObject);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class TestMethods extends ArrayList<Method> {
        void addIfTestMethod(Method m) {
            if (m.getAnnotation(Test.class) == null)
                return;
            if (!(m.getReturnType().equals(boolean.class) || m.getReturnType().equals(void.class)))
                throw new RuntimeException("@Test method" + " must return boolean or void");
            m.setAccessible(true); // In case it's private, etc.
            add(m);
        }
    }

    private static Method checkForCreatorMethod(Method m) {
        if (m.getAnnotation(TestObjectCreate.class) == null)
            return null;
        if (!m.getReturnType().equals(testClass))
            throw new RuntimeException(
                "@TestObjectCreate " + "must return instance of Class to be tested");
        if ((m.getModifiers() & java.lang.reflect.Modifier.STATIC) < 1)
            throw new RuntimeException("@TestObjectCreate " + "must be static.");
        m.setAccessible(true);
        return m;
    }

    private static Method checkForCleanupMethod(Method m) {
        if (m.getAnnotation(TestObjectCleanup.class) == null)
            return null;
        if (!m.getReturnType().equals(void.class))
            throw new RuntimeException("@TestObjectCleanup " + "must return void");
        if ((m.getModifiers() & java.lang.reflect.Modifier.STATIC) < 1)
            throw new RuntimeException("@TestObjectCleanup " + "must be static.");
        if (m.getParameterTypes().length == 0 || m.getParameterTypes()[0] != testClass)
            throw new RuntimeException(
                "@TestObjectCleanup " + "must take an argument of the tested type.");
        m.setAccessible(true);
        return m;
    }

    private static Object createTestObject(Method creator) {
        if (creator != null) {
            try {
                return creator.invoke(testClass);
            } catch (Exception e) {
                throw new RuntimeException("Couldn't run " + "@TestObject (creator) method.");
            }
        }
        // Use the default constructor:
        try {
            return testClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(
                "Couldn't create a " + "test object. Try using a @TestObject method.");
        }
    }
}

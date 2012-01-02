package it.unipr.aotlab.dmat.core.util;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;

import java.io.File;

public class PackageGetClasses {
    public interface OnClassExecutor {
        public void execOnClass(Class<?> klass);
    }

    public interface ClassNameFilter {
        public boolean accept(String className);
    }

    private static void execOnClassesImpl(String pkgName, File pkgDirectory,
            ClassNameFilter filter, OnClassExecutor executor) {
        try {
            for (String file : pkgDirectory.list()) {
                if (!file.endsWith(".class"))
                    continue;

                String className = pkgName + '.'
                        + file.substring(0, file.length() - 6);

                if (filter.accept(className))
                    executor.execOnClass(Class.forName(className));
            }
        } catch (ClassNotFoundException e) {
            throw new DMatInternalError(pkgName + " is not a valid package.");
        }
    }

    public static void execOnClasses(String pkgName, ClassNameFilter filter,
            OnClassExecutor executor) throws DMatInternalError {
        File pkgDirectory = null;
        try {
            pkgDirectory = new File(Thread.currentThread()
                    .getContextClassLoader()
                    .getResource(pkgName.replace('.', '/')).getFile());
        } catch (NullPointerException x) {
            throw new DMatInternalError(pkgName + " is not a valid package.");
        }
        if (!pkgDirectory.exists()) {
            throw new DMatInternalError(pkgName + " is not a valid package.");
        }

        execOnClassesImpl(pkgName, pkgDirectory, filter, executor);
    }
}

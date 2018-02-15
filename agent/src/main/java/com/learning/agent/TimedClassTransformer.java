package com.learning.agent;

import javassist.ByteArrayClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class TimedClassTransformer implements ClassFileTransformer {
	private final static Logger logger = LoggerFactory.getLogger(TimedClassTransformer.class);
	private ClassPool classPool;
	
	public TimedClassTransformer() {
		classPool = new ClassPool();
		classPool.appendSystemPath();
		try {
			classPool.appendPathList(System.getProperty("java.class.path"));

			// make sure that MetricReporter is loaded
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	 
    public byte[] transform(ClassLoader loader, String fullyQualifiedClassName, Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain, byte[] classBytes) throws IllegalClassFormatException {
		String className = fullyQualifiedClassName.replace("/", ".");

		if (!className.startsWith("com.identityforge.idfserver.backend.tops.transport"))
			return classBytes;
		classPool.appendClassPath(new ByteArrayClassPath(className, classBytes));

		try {
			CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classBytes));
			if (ctClass.isFrozen()) {
				logger.info("Skip class {}: is frozen ", className);
				return classBytes;
			}
			
			if (ctClass.isPrimitive() || ctClass.isArray() || ctClass.isAnnotation()
					|| ctClass.isEnum() || ctClass.isInterface()) {
				logger.info("Skip class {}: not a class ", className);
				return classBytes;
			}
			
			boolean isClassModified = false;
			logger.info("Instrumenting class : " + className);
			for(CtMethod method: ctClass.getDeclaredMethods()) {
				// if method is annotated, add the code to measure the time
					if (method.getMethodInfo().getCodeAttribute() == null) {
						logger.info("Skip method " + method.getLongName());
						continue;
					}
					method.addLocalVariable("__metricStartTime", CtClass.longType);
					method.insertBefore("__metricStartTime = System.currentTimeMillis();");
					String metricName = ctClass.getName() + "." + method.getName();
					method.insertAfter("com.learning.agent.MetricReporter.printExecutionTime(\"" + metricName + "\", System.currentTimeMillis() - __metricStartTime);");
					isClassModified = true;
				}
			if (!isClassModified) {
				return classBytes;
			}
			return ctClass.toBytecode();
		} catch (Exception e) {
			logger.info("Skip class {}: ", className, e.getMessage());
			return classBytes;
		}
    }
}
package jp.co.fluxengine.apptest;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.LinkedList;

/**
 * テストメソッドにStringの引数がある場合、
 *　@DslPath で指定されているパスを与えるResolver
 * @DslPath はメソッドだけでなくクラスにも付けられるので、
 * クラスのネストのトップからパスを構築する
 */
public class DslPathResolver implements ParameterResolver {

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		
		return parameterContext.getParameter().getType().equals(String.class);
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		
		return extensionContext.getTestMethod().map(method -> {
			LinkedList<String> dslPathHierarchy = new LinkedList<>();

			DslPath methodDslPath = method.getAnnotation(DslPath.class);
			if (methodDslPath != null && methodDslPath.value() != null) {
				dslPathHierarchy.addFirst(methodDslPath.value());
			}

			for (Class<?> clazz = method.getDeclaringClass(); clazz != null; clazz = clazz.getDeclaringClass()) {
				DslPath clazzDslPath = clazz.getAnnotation(DslPath.class);
				if (clazzDslPath != null && clazzDslPath.value() != null) {
					dslPathHierarchy.addFirst(clazzDslPath.value());
				}
			}

			return String.join("/", dslPathHierarchy);
		}).orElse(null);
	}

}

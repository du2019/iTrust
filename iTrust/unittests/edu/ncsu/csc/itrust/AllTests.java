package edu.ncsu.csc.itrust;

import java.lang.reflect.Modifier;
import junit.framework.Test;
import junitx.util.DirectorySuiteBuilder;
import junitx.util.SimpleTestFilter;

public class AllTests {
	// Turns out that the unchecked warning is from the junit-addons, not us, so we can suppress it
	@SuppressWarnings("unchecked")
	public static Test suite() throws Exception {
		DBBuilder.rebuildAll();
		DirectorySuiteBuilder builder = new DirectorySuiteBuilder();
		builder.setFilter(new SimpleTestFilter() {
			public boolean include(Class clazz) {
				// Ignore the HTTP tests in this suite
				return !clazz.getPackage().getName().contains("http")
						&& !Modifier.isAbstract(clazz.getModifiers());
			}
		});
		return builder.suite("build/classes");
	}

}

package com.jackw.other;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.parabot.core.Context;
import org.parabot.core.Core;
import org.parabot.core.Directories;
import org.parabot.core.asm.hooks.HookFile;
import org.parabot.core.classpath.ClassPath;
import org.parabot.environment.api.utils.WebUtil;
import org.parabot.environment.servers.ServerProvider;
import org.parabot.environment.servers.loader.ServerLoader;

public class Misc {

	static {
		System.setProperty("log4j.configurationFile", new File("log4j2.xml").getAbsolutePath());
	}

	private static final Logger logger = LogManager.getLogger(Misc.class);

	public static URLClassLoader pbClassLoader;

	public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, InstantiationException {


		Directories.validate();
		Core.setDebug(true);
		Core.setVerbose(true);

		final File api =
				Paths.get("C:\\Users\\Jak\\.m2\\repository\\org\\parabot\\runewild-api\\1.0.1//runewild-api-1.0.1.jar")
						.toFile();
		final File clientjar =
				Paths.get("C:\\Users\\Jak\\Documents\\Parabot\\other/runewild_client.jar")
						.toFile();
		final File hooks =
				Paths.get("C:\\Users\\Jak\\Documents\\Parabot\\servers/[hooks]runewild-clientonly.xml")
						.toFile();

		final ClassPath classPath = new ClassPath();
		logger.info(classPath);

		// API
		final boolean a = classPath.addJar(api);
		final int ct2 = classPath.classes.size();
		logger.info("API {} has a total of {} classes", api.getAbsolutePath(), ct2);

		assert a;

		List<Map.Entry<String, ClassNode>> accessors = classPath.classes.entrySet().stream().filter(e -> e.getKey().startsWith("org.rev317.min.accessors.")).collect(Collectors.toList());


		pbClassLoader = new URLClassLoader(new URL[] {api.toURI().toURL()}, null);


		//final boolean b = BuildPathAdd(api.toURI().toURL());
		//assert b;

		final ServerLoader serverLoader = new ServerLoader(classPath);
		final String[] classNames   = serverLoader.getServerClassNames(); // annotated with @ServerProvider
		assert classNames.length == 1;

		final String className = classNames[0]; // Loader.class in 317-min-api
		final Class<?> providerClass = serverLoader.loadClass(className);
		final Constructor<?> struct = providerClass.getConstructor();

		// TODO add 'PBClassPath' to actual BuildPath (system class loader)
		final ServerProvider serverProvider = (ServerProvider) struct.newInstance(); // new
		// Below = ServerExecutor.finalize()
		final Context context = Context.getInstance(serverProvider);//.setProviderInfo(serverProviderInfo);

		//context.load();
		// load() ^^ equivilent:
		//serverProvider.parseJar();
		Context.getInstance().getClassPath().addJar(WebUtil.toURL(clientjar));

		final int clientTotal = context.getClassPath().classes.size();
		logger.info("Client classes total: {}", clientTotal);

		// This is a new ClassPath created in <init of Context
		//context.getClassPath().addJar(api);
		//final int apiTotal = context.getClassPath().classes.size() - clientTotal;
		//logger.info("API classes total now on classpath: {}", apiTotal);

		serverProvider.injectHooks(new HookFile(hooks, HookFile.TYPE_XML));
		//final Applet applet = serverProvider.fetchApplet();

		logger.info("ok");
	}
}

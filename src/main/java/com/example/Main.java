package com.example;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.WebResourceSet;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.JarResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.flywaydb.core.Flyway;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * データベースのマイグレーション、Tomcat の設定／起動を行う。
 * 
 * @author marcie
 *
 */
public class Main {

	private final static Logger logger = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) throws Exception {

		Config config = ConfigFactory.load();
		Config server = config.getConfig("server");
		Config dataSource = config.getConfig("data-source");
		Config app = config.getConfig("app");

		migrateDb(dataSource);

		Tomcat tomcat = new Tomcat();

		Context ctx = prepareTomcat(tomcat, server, app);

		prepareWebResource(ctx);

		prepareJndiResource(ctx, dataSource);

		tomcat.start();

		tomcat.getServer().await();

	}

	/**
	 * Tomcat の設定を行う
	 * 
	 * @param tomcat
	 *            Tomcat インスタンス。内部で変更している
	 * @param server
	 *            サーバ設定
	 * @param app
	 *            アプリケーション設定
	 * @return コンテクスト
	 * @throws ServletException
	 *             デプロイ時エラーが発生した時
	 */
	private static Context prepareTomcat(Tomcat tomcat, Config server, Config app) throws ServletException {
		String appBase = ".";
		tomcat.setPort(server.getInt("port"));
		tomcat.setHostname(server.getString("host"));
		tomcat.getHost().setAppBase(appBase);
		tomcat.enableNaming();
		return tomcat.addWebapp(app.getString("context-path"), appBase);
	}

	/**
	 * JNDI リソースを設定する
	 * 
	 * @param ctx
	 *            コンテクスト。内部で変更している。
	 */
	private static void prepareJndiResource(Context ctx, Config dataSource) {
		ContextResource resource = new ContextResource();
		resource.setName("jdbc/db");
		resource.setAuth("Container");
		resource.setType("javax.sql.DataSource");
		resource.setProperty("driverClassName", dataSource.getString("driver-class-name"));
		resource.setProperty("url", dataSource.getString("url"));
		resource.setProperty("username", dataSource.getString("username"));
		resource.setProperty("password", dataSource.getString("password"));

		ctx.getNamingResources().addResource(resource);
	}

	/**
	 * 実行状況にあわせ、ctx にウェブリソースを設定する。
	 * 
	 * @param ctx
	 *            コンテクスト。内部で変更している。
	 */
	private static void prepareWebResource(Context ctx) {
		WebResourceRoot resources = new StandardRoot(ctx);

		WebResourceSet wrs = null;

		URL main = Main.class.getResource("Main.class");
		if ("jar".equalsIgnoreCase(main.getProtocol())) {
			String path = main.getPath();
			wrs = new JarResourceSet(resources, "/WEB-INF/classes",
					path.substring(path.indexOf(':') + 1, path.indexOf('!')), "/");
		} else if ("file".equalsIgnoreCase(main.getProtocol())) {
			Path path = Paths.get(main.getFile()).getParent().getParent();
			wrs = new DirResourceSet(resources, "/WEB-INF/classes", path.toFile().getAbsolutePath(), "/");
		} else {
			throw new IllegalStateException("Main class is not stored in a jar file or file system.");
		}
		resources.addPreResources(wrs);
		ctx.setResources(resources);

	}

	/**
	 * データベースのマイグレーションをおこなう
	 * 
	 * @param dataSource
	 *            データソース設定
	 */
	private static void migrateDb(Config dataSource) {
		Flyway flyway = new Flyway();
		flyway.setDataSource(dataSource.getString("url"), dataSource.getString("username"),
				dataSource.getString("password"));
		flyway.migrate();
	}
}

package com.sjzu.edu.common.model;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.activerecord.generator.TypeMapping;
import com.jfinal.plugin.druid.DruidPlugin;
import com.sjzu.edu.common.DemoConfig;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: https://jfinal.com/club
 *
 * 在数据库表有任何变动时，运行一下 main 方法，极速响应变化进行代码重构
 */
public class _JFinalDemoGenerator {

	public static DataSource getDataSource(String configFileName) {
		Prop p = PropKit.use(configFileName);
		DruidPlugin druidPlugin = DemoConfig.createDruidPlugin(p);
		druidPlugin.start();
		return druidPlugin.getDataSource();
	}

	public static void main(String[] args) {
		// 假设您希望为默认数据库生成模型
		DataSource dataSource = getDataSource("demo-config-dev.txt"); // 或者其他配置文件名
//		DataSource dataSource = getDataSource("demo-config-lpg.txt"); // 或者其他配置文件名
//		DataSource dataSource = getDataSource("demo-config-jiaqi.txt"); // 或者其他配置文件名

		// 以下设置与原来相同...
		String modelPackageName = "com.sjzu.edu.common.model";
		String baseModelPackageName = modelPackageName + ".base";
		String baseModelOutputDir = System.getProperty("user.dir") + "/src/main/java/" + baseModelPackageName.replace('.', '/');
		System.out.println("输出路径：" + baseModelOutputDir);
		String modelOutputDir = baseModelOutputDir + "/..";

		Generator generator = new Generator(dataSource, baseModelPackageName, baseModelOutputDir, modelPackageName, modelOutputDir);
		// 以下设置与原来相同...
		generator.setGenerateRemarks(true);
		generator.setDialect(new MysqlDialect());
		generator.setGenerateChainSetter(false);
		generator.addBlacklist("adv");
		generator.setGenerateDaoInModel(false);
		generator.setGenerateDataDictionary(false);
		generator.setRemovedTableNamePrefixes("t_");

		TypeMapping tm = new TypeMapping();
		tm.addMapping(LocalDateTime.class, Date.class);
		tm.addMapping(LocalDate.class, Date.class);
		generator.setTypeMapping(tm);

		generator.generate();
	}
}





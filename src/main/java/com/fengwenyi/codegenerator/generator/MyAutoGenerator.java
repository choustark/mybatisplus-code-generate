package com.fengwenyi.codegenerator.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.config.builder.Entity;
import com.baomidou.mybatisplus.generator.config.builder.Mapper;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.fengwenyi.codegenerator.Config;
import com.fengwenyi.codegenerator.bo.CodeGeneratorBo;
import com.fengwenyi.codegenerator.constant.XmlSymbolConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author chou
 * @since 2021-09-26
 */
@Slf4j
public class MyAutoGenerator {

    private final CodeGeneratorBo bo;

    public MyAutoGenerator(CodeGeneratorBo bo) {
        this.bo = bo;
    }

    public void execute() {
        FastAutoGenerator.create(dataSourceBuilder())
                .globalConfig(this::globalConfigBuilder)
                .packageConfig(this::packageConfigBuilder)
                .strategyConfig(this::strategyConfigBuilder)
				.templateEngine(new FreemarkerTemplateEngine())
                .templateConfig(this::templateConfigBuilder)
                .injectionConfig(this::injectionConfigBuilder)
                .execute();
    }

	/**
	 * 注入自定义变量
	 * @param builder
	 */
    private void injectionConfigBuilder(InjectionConfig.Builder builder) {
        builder.beforeOutputFile((tableInfo,map)->{
			log.info("table filed info >>>>> {}",tableInfo.getFields());
            // service 注入名称
            //EmployeeService
            String diService = getDiCfgName(tableInfo.getServiceName());
            map.put("diServiceName",diService);
            // service 注入名称
            String diMapper = getDiCfgName(tableInfo.getMapperName());
            map.put("diMapperName",diMapper);
			// 自定义xml 配置方法变量
			map.put("selectByCondition",true);
			// xml 符号自定
			map.put("lbrace", XmlSymbolConstant.L_BRACE + XmlSymbolConstant.CONDITION_WORD + XmlSymbolConstant.DOT);
			map.put("rbrace",XmlSymbolConstant.R_BRACE);
        });
		Map<String, String> map = new HashMap<>();

		builder.customFile(map);
    }

    private static String getDiCfgName(String serviceName) {
        String fistWord = serviceName.substring(0, 1).toLowerCase();
        String substring = serviceName.substring(1);
        return fistWord + substring;
    }

    private void templateConfigBuilder( TemplateConfig.Builder builder) {
        builder.entity("templates/code/entity.java")
                .service("templates/code/service.java")
                .serviceImpl("templates/code/serviceImpl.java")
                .mapper("templates/code/mapper.java")
                .xml("templates/code/mapper.xml")
                .controller("templates/code/controller.java")
                .build();
    }

    public DataSourceConfig.Builder dataSourceBuilder() {
        return new DataSourceConfig.Builder(bo.getDbUrl(), bo.getUsername(), bo.getPassword());
    }

    public void globalConfigBuilder(GlobalConfig.Builder builder) {

        builder.fileOverride().author(bo.getAuthor());

        String outDir = Config.OUTPUT_DIR;
        if (StringUtils.hasText(bo.getOutDir())) {
            outDir = bo.getOutDir();
        }
        builder.outputDir(outDir);
        DateType dateType = DateType.TIME_PACK;
        if ("java.util".equalsIgnoreCase(bo.getDateTimeType())) {
            dateType = DateType.ONLY_DATE;
        }
        builder.dateType(dateType);

        if (BooleanUtils.isTrue(bo.getSwaggerSupport())) {
            builder.enableSwagger();
        }

    }

    public void packageConfigBuilder(PackageConfig.Builder builder) {
        builder
                .parent(bo.getPackageName())
                // builder.moduleName("");
                .controller(bo.getPackageController())
                .entity(bo.getPackageEntity())
                .mapper(bo.getPackageMapper())
                .xml(bo.getPackageMapperXml())
                .service(bo.getPackageService())
                .serviceImpl(bo.getPackageServiceImpl());
    }

    public void strategyConfigBuilder(StrategyConfig.Builder builder) {
        builder.addInclude(bo.getTableNames())
                .addFieldPrefix(bo.getFieldPrefixes())
                .addTablePrefix(bo.getTablePrefixes())
                .addExclude(bo.getExcludeTableNames())
                .entityBuilder()
                .naming(NamingStrategy.underline_to_camel)
                //.enableChainModel()
                //.enableLombok()
                //.enableActiveRecord()
                .formatFileName(bo.getFileNamePatternEntity())
                .idType(IdType.ASSIGN_ID)
                .logicDeleteColumnName(bo.getFieldLogicDelete())
                .versionColumnName(bo.getFieldVersion())
                .superClass(bo.getSuperClassName())
                .addIgnoreColumns(bo.getIgnoreColumns())
                .mapperBuilder()
                .formatMapperFileName(bo.getFileNamePatternMapper())
                .formatXmlFileName(bo.getFileNamePatternMapperXml())
                .serviceBuilder()
                .formatServiceFileName(bo.getFileNamePatternService())
                .formatServiceImplFileName(bo.getFileNamePatternServiceImpl())
                .controllerBuilder()
                .enableRestStyle()
                .enableHyphenStyle();

        Entity.Builder entityBuilder = builder.entityBuilder();
        if (BooleanUtils.isTrue(bo.getLombokChainModel())) {
            entityBuilder.enableChainModel();
        }
        if (BooleanUtils.isTrue(bo.getLombokModel())) {
            entityBuilder.enableLombok();
        }
        if (BooleanUtils.isTrue(bo.getColumnConstant())) {
            entityBuilder.enableColumnConstant();
        }
        // 字段注解
        if (BooleanUtils.isTrue(bo.getFieldAnnotation())) {
            entityBuilder.enableTableFieldAnnotation();
        }

        Mapper.Builder mapperBuilder = builder.mapperBuilder();
        if (BooleanUtils.isTrue(bo.getBaseResultMap())) {
            mapperBuilder.enableBaseResultMap();
        }
        if (BooleanUtils.isTrue(bo.getBaseColumnList())) {
            mapperBuilder.enableBaseColumnList();
        }

        // 开启mapper注解
        if (BooleanUtils.isTrue(bo.getMapperAnnotation())) {
            mapperBuilder.enableMapperAnnotation();
        }
    }

}

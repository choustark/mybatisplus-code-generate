package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;

/**
 * ${table.comment!} 服务实现类
 * @author ${author}
 * @since ${date}
 */
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

    @Autowired
    private ${table.mapperName} ${diMapperName};

	@Override
	boolean add${entity}(${entity}DTO dto){
		return true;
	}

	@Override
	${entity}DTO getOne(Long id){
		return null;
	}

	@Override
	IPage<${entity}DTO> getPage(${entity}QueryDTO dto,Integer pageNo,Integer pageSize){
		return null;
	}

	@Override
	boolean edit${entity}(${entity}DTO updateDto){
		return true;
	}

	@Override
	boolean delete(Long id){
		return true;
	}

	@Override
	List<${entity}> selectByCondition(${entity} condition);
        return ${diMapperName}.selectByCondition(condition)
	}

}
</#if>

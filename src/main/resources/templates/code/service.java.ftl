package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};

/**
 * ${table.comment!} 服务类
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

	/**
	* 新增
	* @param dto
	*/
	boolean add${entity}(${entity}DTO dto);

	/**
	* 详情查询
	* @param id
	*/
	${entity}DTO getOne(Long id);

	/**
	* 分页查询
	* @param po
	* @param pageNo
	* @param pageSize
	*/
	IPage<${entity}DTO> getPage(${entity}QueryDTO queryDto,Integer pageNo,Integer pageSize);


	/**
	* 编辑
	* @param id
	*/
	boolean edit${entity}(${entity}DTO po);

	/**
	* 删除
	* @param id
	*/
	boolean delete(Long id);

	/**
	* 条件查询
	* @param id
	*/
	List<${entity}> selectByCondition(${entity} condition);

}
</#if>

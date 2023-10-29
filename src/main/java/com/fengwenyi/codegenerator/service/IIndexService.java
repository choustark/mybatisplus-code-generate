package com.fengwenyi.codegenerator.service;

import com.fengwenyi.api.result.ResponseTemplate;
import com.fengwenyi.codegenerator.vo.CodeGeneratorRequestVo;

/**
 * @author chou
 * @since 2021-07-12
 */
public interface IIndexService {

    /**
     * 生成代码
     * @param requestVo
     * @return
     */
    ResponseTemplate<Void> codeGenerator(CodeGeneratorRequestVo requestVo);

}


layui.use(function() {
    let layer = layui.layer
        ,form = layui.form
        ,jQuery = layui.jquery;

    let $ = jQuery;

    let author = '<a href="https://fengwenyi.com?fs=mpcg">Erwin Feng</a>';
    let htmlInitOptionSelect = '<option value="">请选择</option>';
    let htmlInitOptionAdd = '<option value="">添加新的配置</option>';

    let dbAddress = getDbAddress();
    let dbUsername = getDbUsername();
    let dbPassword = getDbPassword();
    let outputDir = getOutputDir();

    jQuery("#author").val(author);
    jQuery("#dbAddress").val(dbAddress);
    jQuery("#dbUsername").val(dbUsername);
    jQuery("#dbPassword").val(dbPassword);
    jQuery("#outputDir").val(outputDir);

    formSetVal()

    let layerIndex;

    showDbConfigSelect();

    function showDbConfigSelect() {
        let dbConfigList = dataDbConfigQueryList()
        $.each(dbConfigList, function(i, item) {
            showDbConfigSelectHtml(item)
        });
    }

    function showDbConfigSelectHtml(item) {
        let k = item.k;
        let name = item.v.name
        let html = '<option value="' + k + '">' + name + '</option>';
        // let html = new Option(name, k);
        $('#dbConfigSelect').append(html);
        $('#boxDbConfigSelect').append(html);
        form.render('select');
    }

    // 监听点击修改昵按钮
    jQuery('#btnEditDatabaseConfig').on('click', function () {
        layerIndex = layer.open({
            type: 1,
            title: '数据库配置',
            closeBtn: 1, //不显示关闭按钮
            anim: 5,
            shade: [0.5],
            area: ['', ''],
            shadeClose: true, //开启遮罩关闭
            content: jQuery('.box-database-config')
        });
    });

    form.on('submit(formDatabaseConfigSave)', function (data) {
        let key = data.field.key;
        if (isEmpty(key)) {
            key = guid();
            data.field.key = key;
            dataDbConfigAdd(key, data.field);
            let o = data.field;
            form.val('boxFormDbConfig', o);
            let obj = dataDbConfigQueryByKey(key);
            if (nonNull(obj)) {
                showDbConfigSelectHtml(obj);
            } // 有问题
        } else {
            dataDbConfigUpdate(key, data.field);
            $("#dbConfigSelect").empty();
            $("#boxDbConfigSelect").empty();
            $("#dbConfigSelect").append(htmlInitOptionSelect);
            $("#boxDbConfigSelect").append(htmlInitOptionAdd);
            showDbConfigSelect();
        }
        return false;
    });

    // 添加
    form.on('submit(boxBtnDbConfigAdd)', function () {
        let count = dataDbConfigCount();
        // console.log(count)
        if (count > 4) {
            alertFail(layer, "数量限制不能再添加")
            return false;
        }
        $("#boxFormDbConfig")[0].reset();
        layui.form.render();
        return false;
    });

    // 删除
    form.on('submit(boxBtnDbConfigDelete)', function (data) {
        $("#boxFormDbConfig")[0].reset();
        layui.form.render();
        dataDbConfigDelete(data.field.key);
        $("#dbConfigSelect").empty();
        $("#boxDbConfigSelect").empty();
        $("#dbConfigSelect").append(htmlInitOptionSelect);
        $("#boxDbConfigSelect").append(htmlInitOptionAdd);
        showDbConfigSelect();
        form.render('select');
        return false;
    });


    //监听提交
    form.on('submit(formCodeGenerator)', function(data){
        handleFormSubmit(data);
        return false;
    });

    // 监听点击保存配置按钮
    form.on('submit(formCodeGeneratorConfigSave)', function(data){
        formCache(data);
        return false;
    });

    // 处理form提交请求
    function handleFormSubmit(data) {
        let url = "/code-generator";
        let param = JSON.stringify(data.field);
        ajaxPost(jQuery, layer, url, param, function (response) {
            if (response.success) {
                handleCache(data.field);
                alertSuccess(layer, response.message);
            } else {
                alertFail(layer, response.message);
            }
        });
    }

    // 缓存
    function handleCache(formData) {
        setDbAddress(formData.host);
        setDbUsername(formData.username)
        setDbPassword(formData.password)
        setAuthor(formData.author)
        setOutputDir(formData.outDir)
    }

    form.on('select(boxFormDbConfigSelect)', function(data){
        let key = data.value;
        if (nonNull(key)) {
            let dbConfigObj = dataDbConfigQueryByKey(key)
            if (nonNull(dbConfigObj)) {
                let formVal = dbConfigObj.v
                form.val('boxFormDbConfig', formVal)
            }
        }
    });

    // 选择数据库
    form.on('select(selectDbConfig)', function(data){
        let key = data.value;
        if (nonNull(key)) {
            let dbConfigObj = dataDbConfigQueryByKey(key)
            if (nonNull(dbConfigObj)) {
                let formVal = dbConfigObj.v
                form.val('formFull', formVal)
            }
        }
    });
    // form 数据缓存
    function formCache(data) {
        setDataConfig(data.field)
        layer.msg('保存成功')
    }

    // form 赋值
    function formSetVal() {
        let data = getDataConfig()
        if (nonNull(data)) {
            form.val('formFull', data)
        }
    }

    jQuery('#formCodeGeneratorConfigDelete').on('click', function () {
        dataConfigDelete()
        layer.msg('清除成功')
    })

});
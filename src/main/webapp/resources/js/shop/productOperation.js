function initPageOfProductOperation() {
    //从URL里获取productId参数的值
    var productId = getQueryString('productId');
    //通过productId获取商品信息的Url
    var infoUrl = 'getProductById?productId='+productId;
    //获取当前店铺设定的商品列表Url
    var categoryUrl = 'getProductCategoryList';
    //更新商品信息的Url
    var productPostUrl = 'modifyProduct';
    //由于商品添加和编辑使用的是同一个页面
    //该标识用来标明本次是添加还是编辑操作
    var isEdit = false;
    if (productId){
        //若有productId则为编辑操作
        getInfo(productId);
        isEdit = true;
    } else {
        getCategory();
        productPostUrl = 'addProduct';
    }

    //获取需要编辑的商品的商品信息，并赋值给表单
    function getInfo(id) {
        $.getJSON(
            infoUrl,function (result) {
                var data = result.data;
                if(data == null){
                    $.toast(result.message);
                    return;
                }
                //从返回的JSON当中获取product对象的信息，并赋值给表单
                var product = data.product;
                $("#product_name").val(product.productName);
                $('#product_desc').val(product.productDesc);
                $('#weight').val(product.weight);
                $('#normal_price').val(product.normalPrice);
                $('#sale_price').val(product.salePrice);
                //获取原本的商品类别以及该店铺的所有商品类别列表
                var optionHtml = '';
                var optionArr = data.productCategoryList;
                var optionSelected = '';
                if (product.productCategory != null && product.productCategory.productCategoryId != null)
                    optionSelected = product.productCategory.productCategoryId;
                //生成前端的Html商品类别列表，并默认选择编辑前的商品类别
                optionArr.map(function (value, index) {
                    var isSelected = optionSelected === value.productCategoryId ? 'selected'
                        :'';
                    optionHtml += '<option data-value="'
                        +value.productCategoryId + '"'+isSelected + '>'
                        +value.productCategoryName +'</option>';
                });
                $('#product_category').html(optionHtml);
            }
        );
    }

    //为商品添加操作提供该店铺下的所有商品类别列表
    function getCategory() {
        $.getJSON(categoryUrl,function (result) {
            if (result.retCode == "0") {
                var productCategoryList = result.data;
                var optionHtml = '';
                productCategoryList.map(function (value,index) {
                    optionHtml += '<option data-value='
                        +value.productCategoryId +'>'
                        +value.productCategoryName +'</option>';
                });
                $('#product_category').html(optionHtml);
            }
        });
    }

    //针对商品详情图控件组，若该控件组的最后一个元素发生变化（即上传了图片）
    //若控件总数不大于6，则生成一个新的文件上传控件
    $('.detail_img_div').on('change','.detail_img:last-child',function () {
        if ($('.detail_img').length < 6){

            $('#detail_img').append('<input type = file class="detail_img">');
        }
    });

    $('#submit').click(function () {
        //获取表单里输入的验证码
        var verifyCodeActual = $('#j_captcha').val();
        if (!verifyCodeActual) {
            $.toast('请输入验证码！');
            return;
        }
        //创建商品Json对象，并从表单里面获取对应的属性值
        var product = {};
        product.productName = $('#product_name').val();
        product.productDesc = $('#product_desc').val();
        product.weight = $('#weight').val();
        product.normalPrice = $('#normal_price').val();
        product.salePrice = $('#sale_price').val();
        //获取选定商品类别值
        product.productCategory = {
            productCategoryId : $('#product_category').find('option').not(
                function () {
                    return !this.selected;
                }
            ).data('value')
        };
        product.productId = productId;

        //获取缩略图文件流
        var thumbnail = $('#small_img')[0].files[0];
        //生成表单对象，用于接收参数并传给后台
        var formData = new FormData();
        formData.append('thumbnail',thumbnail);
        //遍历商品详情图控件，获取里面的文件流
        $('.detail_img').map(function (index,value) {
            //判断该控件是否已选择了文件
            var temp = $('.detail_img')[0];
            console.log('-'+value);
            console.log('+'+index);
            if ($('.detail_img')[index].files.length > 0){
                //将第i个文件流赋值给key为productImgi的表单键值对里
                formData.append('productImg'+ index,
                    $('.detail_img')[index].files[0]);
            }
        });
        //将product json对象转成字符流保存至表单对象key为productStr的键值对中
        formData.append('productStr',JSON.stringify(product));
        formData.append('verifyCodeActual',verifyCodeActual);
        //将数据提交至后台处理相关操作
        $.ajax({
            url:productPostUrl,
            type:'POST',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function (result) {
                if (result.retCode == '0') {
                    $.toast(result.message);
                    $('#captcha_img').click();
                }else {
                    $.toast(result.message);
                    $('#captcha_img').click();
                }
            }
        })
    })
}
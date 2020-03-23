function initPageOfShopOperation() {
    var shopId = getQueryString('shopId');
    var isEdit = shopId?true:false;
    var initUrl = 'getShopInfo';
    var registerShopUrl = 'registerShop';
    var shopInfoUrl = "getShopById?shopId=" + shopId;
    var editShopUrl = 'modifyShop';
    if (!isEdit) {
        getShopInitInfo();
    }else {
        getShopInfo(shopId);
    }

    //得到指定ShopId的商店信息
    function getShopInfo(shopId) {
        $.getJSON(shopInfoUrl,function (result) {
            var data = result.data;
            if (result.retCode == 0) {
                var shop = data.shop;
                $('#shop_name').val(shop.shopName);
                $('#shop_addr').val(shop.shopAddr);
                $('#phone').val(shop.phone);
                $('#shop_desc').val(shop.shopDesc);
                var shopCategory = '<option value="'
                    + shop.shopCategory.shopCategoryId + '" selected>'
                    + shop.shopCategory.shopCategoryName+ '</option>';

                var tempAreaHtml = '';
                data.areaList.map(function (item, index) {
                    tempAreaHtml +='<option value = "' + item.areaId+'">'+
                        item.areaName+'</option>';
                });
                $('#shop_category').html(shopCategory);
                $('#shop_category').attr('disabled','disabled');
                $('#area_category').html(tempAreaHtml);
                $('#area_category [value ="'+shop.area.areaId+'"]').attr('selected','selected');
            }
        });
    }

    function getShopInitInfo() {
        $.getJSON(initUrl,function (result) {
            if (result.retCode==0) {
                var data = result.data;
                var tempHtml='';
                var tempAreaHtml = '';
                data.shopCategoryList.map(function (item, index) {
                    tempHtml += '<option value= "' + item.shopCategoryId+'">'
                    +item.shopCategoryName+'</option>';
                });
                data.areaList.map(function (item, index) {
                    tempAreaHtml +='<option value = "' + item.areaId+'">'+
                        item.areaName+'</option>';
                });
                $('#shop_category').html(tempHtml);
                $('#area_category').html(tempAreaHtml);
            }
        });


    }
    $('#submit').click(function () {
        if (!checkForm()){
            return;
        }
        var shop = {};
        if (isEdit) {
            shop.shopId = shopId;
        }
        shop.shopName=$('#shop_name').val();
        shop.shopAddr=$('#shop_addr').val();
        shop.phone=$('#phone').val();
        shop.shopDesc = $('#shop_desc').val();
        shop.shopCategory={
            shopCategoryId:$('#shop_category').val()
        };
        console.log(shop.shopCategory);
        shop.area={
            areaId:$('#area_category').val()
        };
        var shopImg = $('#shop_img')[0].files[0];
        var formData = new FormData();
        formData.append('shopImg',shopImg);
        formData.append('shopStr',JSON.stringify(shop));
        var verifyCodeActual = $('#j_captcha').val();
        formData.append('verifyCodeActual',verifyCodeActual);
        $.ajax({
            url:(isEdit?editShopUrl:registerShopUrl),
            type:'POST',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function (data) {
                console.log(data);
                if (data.retCode===0) {
                    $.alert(data.message);
                }else {
                    $.alert(data.message);
                }
                $('#captcha_img').click();
            }
        })
    });

    function checkForm() {
        var shop_name = $('#shop_name').val().trim();
        var shop_addr = $('#shop_addr').val().trim();
        var phone = $('#phone').val().trim();
        var shopImg = $('#shop_img')[0].files[0];
        var shop_desc = $('#shop_desc').val().trim();
        var j_captcha = $('#j_captcha').val().trim();
        var isPhone = /^0?1[3|4|5|6|7|8][0-9]\d{8}$/;//手机号码
        //检验内容
        if (shop_name===''||shop_addr===''||phone===''||shop_desc===''){
            $.toast('请完整填写所有内容！');
            return false;
        }
        if (j_captcha==null){
            $.toast('请填写验证码！');
            return false;
        }
        //手机号码正则检测
        if (!isPhone.test(phone)) {
            $.toast('请正确填写手机号！');
            return false;
        }if (!isEdit&&shopImg == null) {
            $.toast('请上传图片！');
            return false;
        }
        return true;
    }
}
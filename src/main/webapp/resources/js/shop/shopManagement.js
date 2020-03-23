function initPageOfShopManagement() {
    var shopId = getQueryString('shopId');
    var shopInfoUrl = 'getShopManagementInfo?shopId='+ shopId;
    $.getJSON(shopInfoUrl,function (result) {
        var data = result.data;
        if (data.redirect) {1
            window.location.href = data.url;
        }else {
            if (data.shopId != undefined && data.shopId != null){
                shopId = data.shopId;
            }
            $('#shopInfo').attr('href','shopOperation?shopId=' + shopId);
        }
    });
}
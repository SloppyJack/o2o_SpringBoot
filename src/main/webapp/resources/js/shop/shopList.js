var initPageOfShopList ={
    getList: function(){
        $.ajax({
            url:'getShopList',
            type:'get',
            dataType:'json',
            success:function (result) {
                if (result.retCode==0){
                    handleList(result.data.shopList);
                    handleUser(result.data.user);
                }
            }
        });
    }
};
function handleUser(data) {
    $('#userName').text('欢迎您！'+data.name);
}

function handleList(data) {
    var html ='';
    data.map(function (item, index) {
        html +=  '<li> <div class="item-content"><div class="item-inner"><div class="item-title color-gray">' +
            item.shopName +
            '</div><div class="item-title color-gray">' +
            shopStatus(item.status) +
            '</div>'+ goShop(item.status,item.shopId) +'</div> </div> </li>';
    });
    $('#shop_content').html(html);
    $('#btn_exit').html('<div class="row"><div class="col-100"><a href="#" class="button button-big button-fill button-danger">退出</a></div></div>');
}

function shopStatus(status) {
    if (status == 100000) {
        return '审核中';
    }else if (status == 100001) {
        return '店铺非法';
    }else if (status == 100004) {
        return '通过认证';
    }
}

function goShop(status, id) {
    if (status == 100004){
        return '<a class="icon icon-edit" href="shopManagement?shopId=' +id+
            '">管理</a>';
    }else {
        return '无权限';
    }
}
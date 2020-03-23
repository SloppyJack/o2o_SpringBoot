var initPageOfProductManagement = function() {
        initTable_ProductList();
        $('#btn_add').click(function(){
            window.location.href= 'productOperation';
        });
};

function initTable_ProductList() {
    var param = {"pageIndex":1,"pageSize":50};
    $.ajax({
        url:'getProductListByShop',
        type:'post',
        data:param,
        dataType:'json',
        success:function (result) {
            var data = result.data.productList;
            var html = '';
            if (result.retCode == '0') {
                data.map(function (item, index) {
                    html += '<li id="' + item.productCategoryId + '">' +
                        '<div class="item-content">' +
                        '<div class="item-inner row">' +
                        '<div class="item-title color-gray col-50" >' + item.productName + '</div>' +
                        '<div class="item-title color-gray col-25">' + item.weight + '</div>' +
                        '<div class="col-25"><a href="javascript:void(0);" onclick="doOperationById_PM(' + item.productId+','+item.status + ') "class="button">操作</a> </div> ' +
                        '</div>' +
                        '</div>' +
                        '</li>';
                });
                $('#product_content').html(html);
                $('#btn_add').attr('type', 'button');
            }
        }
    });
}



function doOperationById_PM(id,status){
    var temp = '下架';
    if (status == 0){
        temp = '上架';
    }
    var buttons1 = [
        {
            text: '请选择',
            label: true
        },
        {
            text: '编辑',
            bold: true,
            color: 'success',
            onClick: function() {
                window.location.href= 'productOperation?productId='+id;
            }
        },
        {
            text: temp,
            onClick: function() {
                var product = {};
                product.productId = id;
                if (status == 0) {
                    //0表示商品状态为下架，1表示上架
                    product.status = 1;
                }else if (status == 1){
                    product.status = 0;
                }
                //上下架相关商品
                $.ajax({
                    url:'modifyProduct',
                    type:'POST',
                    data:{
                        productStr:JSON.stringify(product),
                        statusChange:true
                    },
                    dataType:'json',
                    success:function () {
                        initTable_ProductList();
                    }

                });
            }
        },
        {
            text: '预览',
            onClick: function() {
                $.toast('功能正在开发中');
            }
        }
    ];
    var buttons2 = [
        {
            text: '取消',
            bg: 'danger'
        }
    ];
    var groups = [buttons1, buttons2];
    $.actions(groups);
}
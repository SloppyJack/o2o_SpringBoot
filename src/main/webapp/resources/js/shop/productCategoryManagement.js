function initPageOfProductCategoryManagement() {
        var li_index = 1;
        initTable_ProductCategoryList();
        $('#btn_add').click(function(){
            $('#product_category_content').append('<li id="li_temp_'+li_index+'"><div class="item-content"><div class="item-inner row temp">' +
                '<div class="col-50"><input type="text" id="input_name" placeholder="填写分类名字~"/></div> ' +
                '<div class="col-25"><input type="text" id="input_weight" placeholder="填写优先级~"/></div> ' +
                '<div class="col-25"><a href="javascript:void(0);" class="button  button-warning" onclick=" remove_temp_li('+ li_index++ +') ">正在添加</a> </div> ' +
                '</div> </div> </li>');
        });

        $('#btn_submit').click(function () {
            //由于each方法中return不会直接中断方法，所以设置一个标志
            var flag = false;
            var productCategoryList = [];
            $('.temp').each(function () {
                var tempObj = {};
                tempObj.productCategoryName = $(this).find('div').eq(0).find('input').val();
                tempObj.weight = $(this).find('div').eq(1).find('input').val();
                if (tempObj.productCategoryName == '' || tempObj.weight == '' || isNaN(tempObj.weight)){
                    console.log('test');
                    $.toast('请正确填写所有内容');
                    flag = true;
                    return false;   //相当于break，结束循环
                }
                productCategoryList.push(tempObj);
            });
            if (flag)
                return;
            //开始ajax请求
            $.ajax({
                url:'addProductCategorys',
                type:'POST',
                data:JSON.stringify(productCategoryList),
                contentType:'application/json',
                success:function (result) {
                    if (result.retCode === 0){
                        $.toast('添加成功');
                        initTable_ProductCategoryList();
                    }else {
                        $.toast(result.message);
                    }

                }
            });

        });


}

function remove_temp_li(li_index) {
    $('#li_temp_'+li_index).remove();
}

function initTable_ProductCategoryList() {
    $.getJSON('getProductCategoryList',function (result) {
        var data = result.data;
        var html = '';
        if (result.retCode == '0'){
            data.map(function (item, index) {
                html += '<li id="'+item.productCategoryId +'">' +
                    '<div class="item-content">' +
                        '<div class="item-inner row">' +
                        '<div class="item-title color-gray col-50" >' +item.productCategoryName+'</div>' +
                        '<div class="item-title color-gray col-25">' +item.weight+'</div>' +
                        '<div class="col-25"><a href="javascript:void(0);" onclick="doOperationById_PC('+ item.productCategoryId +') "class="button">操作</a> </div> ' +
                        '</div>' +
                    '</div>' +
                    '</li>';
            });
            $('#product_category_content').html(html);
            $('#btn_add').attr('type','button');
            $('#btn_submit').attr('type','button');
        }
    });
}



function doOperationById_PC(id){
    var buttons1 = [
        {
            text: '请选择',
            label: true
        },
        {
            text: '删除',
            bold: true,
            color: 'danger',
            onClick: function() {
                $.ajax({
                    url:'deleteProductCategory',
                    type:'POST',
                    data:"id="+id,
                    success:function (result) {
                        if (result.retCode == 0){
                            $('#'+id).remove();
                            $.toast('删除成功');
                        }else {
                            $.toast(result.message);
                        }

                    }
                });
            }
        },
        {
            text: '编辑',
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
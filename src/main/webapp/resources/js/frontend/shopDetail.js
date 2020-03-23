function initPageOfFrontShopDetail(){
    var loading = false;
    //分页允许返回的最大条数
    var maxItem = 99;
    //一页返回的最大条数
    var pageSize = 5;
    //从地址栏Url里尝试获取shopId
    var parentId = getQueryString('shopId');
    //从地址栏Url里尝试获取shopId
    var shopId = getQueryString('shopId');
    //根据shopId列出店铺详情信息的Url
    var url = 'listShopDetailPageInfo?shopId='+shopId;
    //获取店铺列表的Url
    var listUrl = 'listProductsByShop';
    //页码
    var pageNum = 1;
    //该查询条件下的总列表长度
    var totalCount = 0;
    //初始化店铺详情信息
    initShopDetail();
    //预先加载5条店铺信息
    addItems(pageSize,pageNum,true);

    function initShopDetail() {
        $.getJSON(url,function (result) {
            if (result.retCode == 0){
                var data = result.data;
                $('#shop_name').html(data.shop.shopName);
                $('#shop_img').attr('src',data.shop.shopImg);
                $('#create_time').html(formatTime(data.shop.createTime,'Y-M-D h:m:s'));
                $('#shop_desc').html(data.shop.shopDesc);
                initPicker(data);
            }
            console.log(result);
        });
    }

    /**
     * 添加查询到的结果集
     * @param pageSize
     * @param pageIndex
     * @param hasChangedCondition 标识是否更改过查询条件
     */
    function addItems(pageSize, pageIndex,hasChangedCondition) {
        loading = true;
        if (hasChangedCondition){
            $('#shop_content').html('');
            //重新绑定滚动事件
            $.attachInfiniteScroll($('.infinite-scroll'));
            $('.infinite-scroll-preloader').show();
        }
        var productCategoryId='';
        var productCategoryName = '';
        if ($('#picker_val').length > 0){
            productCategoryId = $('#picker_val').val();
        }
        if ($('#search_input').length > 0)
            productCategoryName = $('#search_input').val();
        var url = listUrl + '?pageIndex=' + pageIndex + '&pageSize=' + pageSize +
            '&shopId=' + shopId + '&productCategoryId=' +
            productCategoryId + '&productCategoryName=' + productCategoryName;
        //访问后台获取相应查询条件下的店铺列表
        $.getJSON(url,function (result) {
            if (result.retCode === 0){
                //当前查询条件下店铺的总数
                totalCount = result.data.count;
                var htmlTemp = '';
                result.data.productList.map(function (item,index){
                    htmlTemp += '<div class="card">' +
                        '                        <div class="card-header">'+item.productName +'</div>' +
                        '                        <div class="card-content">' +
                        '                            <div class="list-block media-list">' +
                        '                                <ul>' +
                        '                                    <li class="item-content">' +
                        '                                        <div class="item-media">' +
                        '                                            <img src="'+ item.imgAddr +'" width="44">' +
                        '                                        </div>' +
                        '                                        <div class="item-inner">' +
                        '                                            <div class="item-title-row">' +
                        '                                                <div class="item-title">'+ item.productDesc +'</div>' +
                        '                                            </div>' +
                        '                                            <div class="item-subtitle">'+ '原价:'+item.normalPrice+' 促销价:'+item.salePrice +'</div>' +
                        '                                        </div>' +
                        '                                    </li>' +
                        '                                </ul>' +
                        '                            </div>' +
                        '                        </div>' +
                        '                        <div class="card-footer">' +
                        '                            <span>'+ formatTime(item.createTime,'Y-M-D h:m:s') +'</span>' +
                        '                            <span><button onclick="buyProduct('+ item.productId +')" class="button">购买</button></span>' +
                        '                        </div>' +
                        '                    </div>';
                });
                if (hasChangedCondition) {
                    //如有更改，则覆盖
                    $('#product_content').html(htmlTemp);
                }else {
                    //添加至指定的内容区中
                    $('#product_content').append(htmlTemp);
                }
                //获取目前为止已显示的卡片总数
                var existedNum = $('#product_content .card').length;
                if (existedNum >= totalCount || existedNum >= maxItem) {
                    //加载完毕，则注销无限加载事件
                    $.detachInfiniteScroll($('.infinite-scroll'));
                    //隐藏加载提示符
                    $('.infinite-scroll-preloader').hide();
                    if (totalCount === 0){
                        //添加相关提示：未查找到相关内容
                        $('#product_content').append('<p class="text-center">未查找到相关内容</p>');
                    }else {
                        $('#product_content').append('<p class="text-center">到底了~</p>');
                    }
                    return;
                }
                //否则页码加1，继续load出新的店铺
                pageNum += 1;
                //加载结束，可以再次加载了
                loading = false;
                //容器发生改变,如果是js滚动，需要刷新滚动
                $.refreshScroller();
            }
        });
    }

    //监听搜索框
    $('#search_input').on('input',function () {
        pageNum = 1;
        addItems(pageSize,pageNum,true);
    });

    //初始化选择器
    function initPicker(data){
        var productCategoryList = data.productCategoryList;   //商品类别列表
        var arrVal_productCategoryList = [];
        var arrDisplay_productCategoryList = [];
        for (var i=0 ; i<productCategoryList.length ; i++){
            arrVal_productCategoryList.push(productCategoryList[i].productCategoryId);
            arrDisplay_productCategoryList.push(productCategoryList[i].productCategoryName);
        }
        $('#picker_display').picker({
            rotateEffect: false,    //
            toolbarTemplate: '<header class="bar bar-nav">' +
                '<button onclick="resetPicker()" class="button button-link pull-left close-picker">清空</button>'+
                '<button class="button button-link pull-right close-picker">确定</button>'+
                '<h1 class="title">类别与区域</h1>'+
                '</header>',
            cols: [
                {
                    textAlign: 'center',
                    values: arrVal_productCategoryList,
                    displayValues:arrDisplay_productCategoryList
                }
            ],
            formatValue: function (picker, value, displayValue){
                //此方法用于控制如何显示picker的选中值
                $('#picker_val').val(value.join(','));
                return displayValue.join('-');
            },
            onClose:function () {//关闭picker的回调函数
                pageNum = 1;    //重新赋值页码
                //此处延时，为了性能
                setTimeout(function () {
                    addItems(pageSize,pageNum,true);
                },500);
            }
        });
    }
}

/**
 * 购买商品
 * @param productId
 */
function buyProduct(productId) {
    $.toast('购买功能正在开发中~');
}
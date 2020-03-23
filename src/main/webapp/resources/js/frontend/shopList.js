function initPageOfFrontShopList() {
   var loading = false;
   //分页允许返回的最大条数
    var maxItem = 99;
    //一页返回的最大条数
    var pageSize = 5;
    //获取店铺列表的Url
    var listUrl = 'listShops';
    //获取店铺类别及区域列表的Url
    var searchDivUrl = 'listShopPageInfo';
    //页码
    var pageNum = 1;
    //该查询条件下的总列表长度
    var totalCount = 0;
    //从地址栏Url里尝试获取parentId
    var parentId = getQueryString('parentId');
    //渲染出店铺类别列表及区域列表供搜索
    getSearchDivData();
    //预先加载5条店铺信息
    addItems(pageSize,pageNum,true);

    function getSearchDivData() {
        //如果传入了parentId，则取出此一级类别下面的所有二级类别
        var url = searchDivUrl + '?parentId='+parentId;
        $.getJSON(url,function (result) {
                if (result.retCode === 0) {
                    initPicker(result.data);
                }
            }
        )
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
        var areaId='';
        var shopCategoryId='';
        var shopName = '';
        if ($('#picker_val').length > 0){
            shopCategoryId = $('#picker_val').val().split(',')[0];
            areaId = $('#picker_val').val().split(',')[1];
        }
        if ($('#search_input').length > 0)
            shopName = $('#search_input').val();
        var url = listUrl + '?pageIndex=' + pageIndex + '&pageSize=' + pageSize +
            '&parentId=' + parentId + '&areaId=' + areaId + '&shopCategoryId=' +
            shopCategoryId + '&shopName=' + shopName;
        //访问后台获取相应查询条件下的店铺列表
        $.getJSON(url,function (result) {
           if (result.retCode === 0){
               //当前查询条件下店铺的总数
               totalCount = result.data.count;
               var htmlTemp = '';
               result.data.shopList.map(function (item,index){
                   htmlTemp += '<div class="card">' +
                       '                        <div class="card-header">'+ '['+item.area.areaName+' '+item.shopAddr+'] '+item.shopName +'</div>' +
                       '                        <div class="card-content">' +
                       '                            <div class="list-block media-list">' +
                       '                                <ul>' +
                       '                                    <li class="item-content">' +
                       '                                        <div class="item-media">' +
                       '                                            <img src="'+ item.shopImg +'" width="44">' +
                       '                                        </div>' +
                       '                                        <div class="item-inner">' +
                       '                                            <div class="item-title-row">' +
                       '                                                <div class="item-title">'+ item.shopDesc +'</div>' +
                       '                                            </div>' +
                       '                                            <div class="item-subtitle">'+ '地址:'+item.shopAddr+' tel:'+item.phone +'</div>' +
                       '                                        </div>' +
                       '                                    </li>' +
                       '                                </ul>' +
                       '                            </div>' +
                       '                        </div>' +
                       '                        <div class="card-footer">' +
                       '                            <span>'+ formatTime(item.createTime,'Y-M-D h:m:s') +'</span>' +
                       '                            <span><a href="shopDetail?shopId='+item.shopId+'">查看详情</a></span>' +
                       '                        </div>' +
                       '                    </div>';
               });
               if (hasChangedCondition) {
                   //如有更改，则覆盖
                   $('#shop_content').html(htmlTemp);
               }else {
                   //添加至指定的内容区中
                   $('#shop_content').append(htmlTemp);
               }
               //获取目前为止已显示的卡片总数
               var existedNum = $('#shop_content .card').length;
               if (existedNum >= totalCount || existedNum >= maxItem) {
                   //加载完毕，则注销无限加载事件
                   $.detachInfiniteScroll($('.infinite-scroll'));
                   //隐藏加载提示符
                   $('.infinite-scroll-preloader').hide();
                   if (totalCount === 0){
                       //添加相关提示：未查找到相关内容
                       $('#shop_content').append('<p class="text-center">未查找到相关内容</p>');
                   }else {
                       $('#shop_content').append('<p class="text-center">到底了~</p>');
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

    //下滑屏幕自行进行分页搜索
    $(document).on('infinite','.infinite-scroll-bottom',function() {
        if (loading)
            return;
        //设定加载符，若还在后台取数则不能再次访问，避免多次加载
        loading = true;
        //延时加载，便于展示效果
        setTimeout(function () {
            addItems(pageSize,pageNum,false);
        },1000);
    });

    $('#search_input').on('input',function () {
        pageNum = 1;
        addItems(pageSize,pageNum,true);
    });

    //点击店铺的卡片进入该店铺的详情页
    //$('.shop-list').

    function initPicker(data){
        var shopCategoryList = data.shopCategoryList;   //商品类别列表
        var areaList = data.areaList;   //区域列表
        var arrVal_shopCategoryList = [];
        var arrDisplay_shopCategoryList = [];
        var arrVal__areaList = [];
        var arrDisplay_areaList = [];
        for (var i=0 ; i<shopCategoryList.length ; i++){
            arrVal_shopCategoryList.push(shopCategoryList[i].shopCategoryId);
            arrDisplay_shopCategoryList.push(shopCategoryList[i].shopCategoryName + '(' + shopCategoryList[i].shopCategoryDesc + ')');
        }
        for (i=0 ; i<areaList.length ; i++){
            arrVal__areaList.push(areaList[i].areaId);
            arrDisplay_areaList.push(areaList[i].areaName);
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
                    values: arrVal_shopCategoryList,
                    displayValues:arrDisplay_shopCategoryList
                },
                {
                    textAlign: 'center',
                    values: arrVal__areaList,
                    displayValues:arrDisplay_areaList
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

//重置picker的值
function resetPicker() {
    $('#picker_display').val('');
    $('#picker_val').val('');
}

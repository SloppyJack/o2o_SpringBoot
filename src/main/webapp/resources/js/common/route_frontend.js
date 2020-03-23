//window.onload方法会在资源加载完毕后才会调用
window.onload= function () {
    $(document).on("pageInit", function (e, pageId, $page) {
        if(pageId === "pageFrontHomePage"){
            initPageOfFrontHomePage();
        }else if(pageId === "pageFrontShopList"){
            initPageOfFrontShopList();
        }else if(pageId === "pageFrontShopDetail"){
            initPageOfFrontShopDetail();
        }
    });

    //初始化
    $.init();
};
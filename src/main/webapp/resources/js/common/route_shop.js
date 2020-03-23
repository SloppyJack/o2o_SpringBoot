//window.onload方法会在资源加载完毕后才会调用
window.onload= function () {
    $(document).on("pageInit", function (e, pageId, $page) {
        if (pageId === "pageShopList") {
            initPageOfShopList.getList();
        } else if (pageId === "pageShopOperation") {
            initPageOfShopOperation();
        } else if (pageId === "pageShopManagement") {
            initPageOfShopManagement();
        } else if (pageId === "pageProductCategoryManagement") {
            initPageOfProductCategoryManagement();
        } else if (pageId === "pageProductOperation") {
            initPageOfProductOperation();
        } else if (pageId === "pageProductManagement") {
            initPageOfProductManagement();
        }
    });

    //初始化
    $.init();
};

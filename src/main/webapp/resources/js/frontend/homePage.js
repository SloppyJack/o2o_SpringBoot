function initPageOfFrontHomePage(){
    //定义访问后台，获取头条列表及一级类别列表的URL
    var url = 'listMainPageInfo';
    //访问后台，获取头条列表及一级类别列表
    $.getJSON(url,function (result) {
        if (result.retCode == 0){
            var data = result.data;
            //获取后台传递过来的头条列表
            var headNewsList = data.headNewsList;
            var swiperHtml = '';
            if (headNewsList != null) {
                //定义images数组
                var images = [];
                //遍历头条列表，并拼接处轮播图组
                headNewsList.map(function (value, index) {
                    /*swiperHtml +=
                        '<div class = "swiper-slide">' +
                            '<a href="'+ value.newsLink +'"external>' +
                                '<img class="banner-img" style="width:50%;height:50%" src="'+ value.newsImg +'"alt="'+ value.newsName +'">' +
                            '</a>' +
                        '</div>';*/
                    var temp = {
                        tagName: "a",
                        /*attrs: {
                            href: value.newsLink,
                            style: "width:100%; height: 100%"
                        },*/
                        children: [
                            {
                                tagName: "img",
                                attrs: {
                                    src: value.newsImg,
                                    style: "width:100%; height: 100%"
                                }
                            }
                        ]
                    };
                    images.push(temp);
                });
                //此注释表示：awesomeSlider这个轮播图框架支持的类型
                /*images = [
                    {
                        tagName: "div",
                        attrs: {
                            style:
                                "width:100%; height: 100%; background-color: pink; font-size: 32px; color: #fff;"
                        },
                        children: [
                            "It's not just picture",
                            {
                                tagName: "div",
                                attrs: {
                                    style:
                                        "width:100%; height: 100%; background-color: pink; font-size: 14px; color: #fff;"
                                },
                                children: [
                                    "text text text text text text text text text text text text text"
                                ]
                            }
                        ]
                    },
                    {
                        tagName: "a",
                        attrs: {
                            href: "https://metxnbr.github.io/awesome-slider/demo/assets/2.png",
                            style: "width:100%; height: 100%"
                        },
                        children: [
                            {
                                tagName: "img",
                                attrs: {
                                    src: "upload/item/headNews/01.jpg",
                                    style: "width:100%; height: 100%"
                                }
                            }
                        ]
                    },
                    {
                        tagName: "img",
                        attrs: {
                            src: "upload/item/headNews/03.jpg",
                            style: "width:100%; height: 100%"
                        }
                    },
                    {
                        tagName: "img",
                        attrs: {
                            src: "upload/item/headNews/02.jpg",
                            style: "width:100%; height: 100%"
                        }
                    }
                ];*/
                //由于sui的路由有缓存功能，所以此处须判断
                $('#root').html('');
                //将jq对象转换为Dom对象
                var container = $('#root').get(0);
                var awesomeSlider = new AwesomeSlider(images,container);
            }
            //获取后台传过来的大类列表
            var shopCategoryList = data.shopCategoryList;
            var categoryHtml = '';
            //遍历大类列表，拼接出一行的类别
            shopCategoryList.map(function (value, index) {
               categoryHtml += '<div class="card-content col-50 shop-classify" data-categoryid="'+ value.shopCategoryId +'">' +
                                   '<div class="list-block media-list">' +
                                       '<ul>' +
                                       '<li class="item-content">' +
                                           '<div class="item-media">' +
                                           '<img src="'+value.shopCategoryImg+'" width="44">' +
                                           '</div>' +
                                           '<div class="item-inner">' +
                                           '<a class="item-link" href="shopList?parentId='+ value.shopCategoryId +'">'+
                                           '<div class="item-title-row">' +
                                           '<div class="item-title">'+value.shopCategoryName+'</div>' +
                                           '</div>' +
                                           '<div class="item-subtitle">'+value.shopCategoryDesc+'</div>' +
                                           '</div>' +
                                       '</a></li>' +
                                       '</ul>' +
                                   '</div>' +
                               '</div>'
            });
            //将拼接好的类别赋值给前端Html控件进行展示
            $('#parentCategory').html(categoryHtml);

            /*$('.row').on('click','.shop-classify',function (e) {
                var shopCategoryId = e.currentTarget.dataset.categoryid;
                var newUrl = 'shopList?parentId='+shopCategoryId;
                window.location.href = newUrl;
            });*/
        }
    });
}
var root = document.getElementById("root");
function appendContainer(text) {
    var container = document.createElement("div");
    container.className = "container";

    root.appendChild(container);

    if (text) {
        var introduce = document.createElement("div");
        introduce.className = "introduce";
        var textNode = document.createTextNode(text);
        introduce.appendChild(textNode);
        container.appendChild(introduce);
    }
    return container;
}
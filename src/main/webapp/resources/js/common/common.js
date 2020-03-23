function changeVerifyCode(img) {
    img.src = "../Kaptcha?"+Math.floor(Math.random()*100);
}

function getQueryString(name) {
    var reg = new RegExp("(^|&)"+name+"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r!=null)
        return decodeURIComponent(r[2]);
    return '';
}

// 格式化日期，如月、日、时、分、秒保证为2位数
function formatNumber (n) {
    n = n.toString();
    return n[1] ? n : '0' + n;
}
// 参数number为毫秒时间戳，format为需要转换成的日期格式
function formatTime (number, format) {
    var time = new Date(number);
    var newArr = [];
    var formatArr = ['Y', 'M', 'D', 'h', 'm', 's'];
    newArr.push(time.getFullYear());
    newArr.push(formatNumber(time.getMonth() + 1));
    newArr.push(formatNumber(time.getDate()));

    newArr.push(formatNumber(time.getHours()));
    newArr.push(formatNumber(time.getMinutes()));
    newArr.push(formatNumber(time.getSeconds()));

    for (var i in newArr) {
        format = format.replace(formatArr[i], newArr[i]);
    }
    return format;
}
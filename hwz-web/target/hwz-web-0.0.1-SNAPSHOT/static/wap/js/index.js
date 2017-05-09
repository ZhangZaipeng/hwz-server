/**
 * Created by Administrator on 2016/6/30.
 */
(function () {
    document.addEventListener('DOMContentLoaded', function () {
        var html = document.documentElement;
        var windowWidth = html.clientWidth;
        html.style.fontSize = windowWidth / 7.5 + 'px';
        // µÈ¼ÛÓÚhtml.style.fontSize = windowWidth / 750 * 100 + 'px';
    }, false);
})();
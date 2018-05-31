function applyJqueryStuff() {


    setTimeout(function () {
        zWatch.fireDown("onSize", '');
    }, 600)

    $('a.sidebar-toggle').click(function () {
        setTimeout(function () {
            zWatch.fireDown("onSize", '');
        }, 400)
    });

}

function onPageLoaded() {

    $('.tooltip').tooltipster({
        position: 'bottom'
    });
    $('.actiontb-a').tooltipster({
        position: 'bottom'
    });
}


zk.afterMount(function () {
    var skin = $("meta[name=skin]");

    $("body").attr("class", skin[0].content + " sidebar-mini sidebar-collapse fixed");
    applyJqueryStuff();
});

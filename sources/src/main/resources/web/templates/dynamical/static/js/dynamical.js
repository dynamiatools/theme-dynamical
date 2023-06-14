function applyJqueryStuff() {


    setTimeout(function () {
        zWatch.fireDown("onSize", '');
    }, 500)

    $('a.sidebar-toggle').click(function () {
        setTimeout(function () {
            zWatch.fireDown("onSize", '');
        }, 300)
    });

}

function onPageLoaded() {

}


zk.afterMount(function () {
    var skin = $("meta[name=skin]");

    $("body").attr("class", skin[0].content + " sidebar-mini layout-fixed layout-navbar-fixed");
    applyJqueryStuff();
});


function toast(type,title,message,icon){
    $(document).Toasts('create', {
        title: title,
        body: message,
        autohide: true,
        autoremove: true,
        close: true,
        class: type,
        delay: 4000,
        position: 'bottomRight',
        icon: icon
    })
}
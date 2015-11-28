function applyJqueryStuff() {
	// Activate the layout maker
	$.AdminLTE.layout.activate();

	// Enable sidebar tree view controls
	$.AdminLTE.tree('.sidebar');

	$('a.sidebar-toggle').click(function() {
		
		
		zWatch.fireDown("onSize",'');
		
	});

}

function onPageLoaded() {

	$('.tooltip').tooltipster({
		position : 'bottom'
	});
	$('.actiontb-a').tooltipster({
		position : 'bottom'
	});
}

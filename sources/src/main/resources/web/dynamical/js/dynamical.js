function applyJqueryStuff() {
	$.AdminLTE.tree('.sidebar');
}

function changeHash(value) {
	if (window.history && window.history.pushState) {
		history.pushState({}, "Page " + value, getContextPath() + "/page/"
				+ value);
	} else {
		window.location.hash = value;
	}
}

function sendMeHash(uuid) {
	var hash = window.location.hash;
	if (hash) {
		hash = hash.substring(1, hash.length)

		zAu.send(new zk.Event(zk.Widget.$(uuid), 'onHash', hash));
	}
}

function getContextPath() {
	return window.location.pathname.substring(0, window.location.pathname
			.indexOf("/", 2));
}

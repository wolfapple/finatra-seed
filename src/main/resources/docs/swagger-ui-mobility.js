function changeHost() {
    var newspec = ui.spec().toJSON().resolved;

    if (newspec == null || newspec.host == null) {
        setTimeout(function () { changeHost(); }, 100);
        return;
    }

    var scheme = location.protocol.slice(0, -1);
    var host = location.hostname + (location.port ? ':' + location.port : '');

    newspec.scheme = [scheme] || newspec.scheme;
    newspec.host = host || newspec.host;

    ui.getStore().dispatch({type: 'set_scheme', payload: {scheme: newspec.scheme[0]}})
    ui.getStore().dispatch({type: 'spec_update_resolved', payload: newspec})
}

var beforeEvent = window.onload;
window.onload = function() {
  if (beforeEvent) beforeEvent();
  changeHost();
}
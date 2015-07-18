/* global define:false */
define(['utils/Config', 'utils/Websocket'], function (cfg, Websocket) {
  'use strict';

  var wsStatusEl = document.getElementById('websocket-content');

  function Boilerplay() {
    this.ws = new Websocket(cfg.wsUrl, this);
    document.getElementById('require-content').textContent = 'Initialized';
  }

  Boilerplay.prototype.onConnect = function() {
    var self = this;
    function sendPing() {
      self.ws.send('Ping', { timestamp: new Date().getTime() });
      setTimeout(sendPing, 5000);
    }

    wsStatusEl.textContent = 'Connected';
    setTimeout(sendPing, 1000);
  };

  Boilerplay.prototype.onMessage = function(c, v) {
    switch(c) {
      case 'Pong':
        var delta = new Date().getTime() - v.timestamp;
        wsStatusEl.textContent = 'Ping response received in [' + delta + 'ms].';
        break;
      default:
        console.log('Message [' + c + '] received over websocket: ' + JSON.stringify(v, null, 2));
        break;
    }
  };

  return Boilerplay;
});

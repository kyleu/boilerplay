/* global Boilerplay:false */
(function() {
  'use strict';

  var ajaxStatusEl;

  var scalaJsStatusEl;
  var scalaJsService;

  function call(url, successCallback, failureCallback) {
    var request = new XMLHttpRequest();

    request.onreadystatechange = function() {
      if(request.readyState === XMLHttpRequest.DONE) {
        if(request.status === 200) {
          successCallback(url, request.responseText);
        } else {
          failureCallback(url, request.responseText);
        }
      }
    };

    request.onerror = function() {
      failureCallback(url, 'Network error');
    };

    request.open('GET', url, true);
    request.send();
  }

  function ajaxCallback(url, content) {
    var delta = new Date().getTime() - parseInt(content);
    ajaxStatusEl.textContent = 'Ping response received in [' + delta + 'ms].';
  }

  function scalaJsCallback(json) {
    var msg = JSON.parse(json);
    switch(msg.c) {
      case 'Pong':
        var delta = new Date().getTime() - msg.v.timestamp;
        scalaJsStatusEl.textContent = 'Ping response received in [' + delta + 'ms].';
        break;
      default:
        console.log('Message [' + msg.c + '] received in Scala.js: ' + JSON.stringify(msg.v, null, 2));
        break;
    }
  }

  function sendPings() {
    var url = '/ping?timestamp=' + new Date().getTime();
    call(url, ajaxCallback, function() { console.log('Ajax Error!'); });
    scalaJsService.receive('Ping', { timestamp: new Date().getTime() });
    setTimeout(sendPings, 5000);
  }

  function init() {
    ajaxStatusEl = document.getElementById('ajax-content');
    scalaJsStatusEl = document.getElementById('scalajs-content');

    scalaJsService = new Boilerplay();
    scalaJsService.register(scalaJsCallback);
    scalaJsStatusEl.textContent = 'Ready';
    setTimeout(sendPings, 1000);
  }

  document.addEventListener('DOMContentLoaded', init);
})();

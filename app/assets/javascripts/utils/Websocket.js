/* global define:false */
/* global _:false */
define(['utils/Config'], function (cfg) {
  'use strict';

  function Websocket(url, context) {
    this.url = url;
    this.connected = false;
    this.connect(context);
  }

  Websocket.prototype.connect = function(context) {
    var me = this;
    var ws = new WebSocket(this.url);
    ws.onopen = function() {
      me.connected = true;
      context.onConnect(me.url);
    };
    ws.onmessage = function(event) {
      var json = JSON.parse(event.data);
      if(json.c === 'MessageSet') {
        var messages = '';
        _.each(json.v.messages, function(message, messageIndex) {
          if(messageIndex > 0) {
            messages += ', ';
          }
          messages += message.c;
        });
      }
      context.onMessage(json.c, json.v);
    };
    ws.onclose = function() {
      me.connected = false;
      setTimeout(function() {
        console.info('Websocket connection closed. Attempting to reconnect.');
        me.connect(context);
      }, 5000);
    };
    ws.onerror = function(err) {
      me.connected = false;
      console.error('Received error from websocket connection [' + err + '].');
    };
    this.connection = ws;
  };

  Websocket.prototype.close = function() {
    this.connection.close();
  };

  Websocket.prototype.send = function(c, v) {
    var msg = { c: c, v: v };
    var s = null;
    if(cfg.debug) {
      // console.log('Sending message [' + c + '].');
      s = JSON.stringify(msg, undefined, 2);
    } else {
      s = JSON.stringify(msg);
    }
    this.connection.send(s);
  };

  return Websocket;
});

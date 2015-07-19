/* global define:false */
define([], function() {
  'use strict';

  return {
    getDebugInfo: function(scene, game) {
      return {
        'userAgent': window.navigator.userAgent,
        'windowHeight': window.innerHeight,
        'windowWidth': window.innerWidth,
        'screenHeight': screen.height,
        'screenWidth': screen.width,
        'screenOrientation': window.screen.orientation === undefined ? '?' : window.screen.orientation.type,
        'currentTime': new Date().toString()
      };
    }
  };
});

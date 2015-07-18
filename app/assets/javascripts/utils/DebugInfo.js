/* global define:false */
define([], function() {
  'use strict';

  return {
    getDebugInfo: function(scene, game) {
      return {
        'userAgent': window.navigator.userAgent,
        'gameHeight': game === null ? 0 : game.height,
        'gameWidth': game === null ? 0 : game.width,
        'renderer': game === null ? 0 : (game.renderType === 2 ? 'WebGL' : 'Canvas'),
        'device': game === null ? 'None' : game.device,
        'windowHeight': window.innerHeight,
        'windowWidth': window.innerWidth,
        'screenHeight': screen.height,
        'screenWidth': screen.width,
        'screenOrientation': window.screen.orientation === undefined ? '?' : window.screen.orientation.type,
        'gameId': game === null ? '-' : game.id,
        'currentTime': new Date().toString()
      };
    }
  };
});

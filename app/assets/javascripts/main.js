/* global requirejs:false */
requirejs.config({
  baseUrl: '/assets/javascripts',
  paths: {
    lib: '/assets/lib'
  }
});

requirejs(['Boilerplay'], function(Boilerplay) {
  'use strict';
  window.boilerplay = new Boilerplay();
});

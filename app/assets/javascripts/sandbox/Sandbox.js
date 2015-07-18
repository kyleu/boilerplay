/* global define:false */
define([], function () {
  function sandbox(ws) {
    ws.send('DebugInfo', { data: 'Sandbox!' });
    return 'Ok: ' + ws.id;
  }

  return {
    go: function(ws) {
      var startTime = new Date().getTime();
      var result = sandbox(ws);
      var elapsed = new Date().getTime() - startTime;
      console.log('Sandbox executed in [' + elapsed + 'ms] with result [' + result + '].');
      return result;
    }
  };
});

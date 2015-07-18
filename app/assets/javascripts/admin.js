/* global nv:false */
/* global d3:false */

function wireLinks() {
  'use strict';
  $('.trace-link').on('click', function(ev) {
    var url = ev.currentTarget.href;
    $('#trace-content').load(url, null, function() {
      wireLinks();
    });
    return false;
  });
}

function wireSearchButton() {
  'use strict';
  $('#search-button').on('click', function(ev) {
    $('#search-form').submit();
    return false;
  });
}

function getChartData() {
  var c = document.getElementById('trend-chart-data');
  if(c === undefined || c === null) {
    return [];
  } else {
    var json = c.innerHTML;
    return JSON.parse(json);
  }
}

function wireChart() {
  var chartData = getChartData();
  if(chartData.length > 0) {
    nv.addGraph(function() {
      var chart = nv.models.lineChart()
        .margin({ left: 30, right: 30 })
        .useInteractiveGuideline(true)
        .showLegend(true)
        .showYAxis(true)
        .showXAxis(true);

      chart.xAxis.tickFormat(function(d) {
        return d3.time.format('%b %d')(new Date(d));
      });
      chart.yAxis.tickFormat(d3.format(',0f'));

      d3.select('#trend-chart svg').datum(chartData).call(chart);
      nv.utils.windowResize(function() {
        chart.update();
      });
      return chart;
    });
  }
}

$(function() {
  'use strict';
  wireLinks();
  wireSearchButton();
  wireChart();
});

$(function () {
    'use strict';

    var ticksStyle = {
        fontColor: '#495057',
        fontStyle: 'bold'
    };

    var mode = 'index';
    var intersect = true;

    var $salesChart = $('#com.fertifa.app.affiliate-pie');
    var $salesChart = new Chart($salesChart, {
        type: 'pie',
        data: {
            labels: ['register', 'clicked'],
            datasets: [{
                label: '',
                backgroundColor: [
                    'rgba(155, 141, 242, 0.8)',
                    'rgba(100, 41, 142, 0.8)'
                ],
                data: affiliate_data,
                labels: [
                    'Red',
                    'Orange'
                ]
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false
        }
    });
});

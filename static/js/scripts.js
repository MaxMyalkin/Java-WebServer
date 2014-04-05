/**
 * Created by maxim on 16.02.14.
 */

function setClientTime() {
    var currentTime = new Date();
    var hours = currentTime.getHours();
    var minutes = currentTime.getMinutes();
    var seconds = currentTime.getSeconds();
    if (hours < 10)
        hours = '0' + hours;
    if (minutes < 10)
    minutes = '0' + minutes;
    if (seconds < 10)
    seconds = '0' + seconds;
    document.getElementById('ClientTime').innerHTML = hours + ':' + minutes + ':' + seconds;
    }

function refresh() {
    location.reload();
}


function ajaxFunc(){
    $.ajax( {
        url: '/ajaxcheking',
        success: function(msg){
            $('#info').html(msg);
            if (msg != "") {
                clearInterval(interval);
            }

        }
    });
}

window.onload = function() {
    interval = setInterval(ajaxFunc, 1000);
}



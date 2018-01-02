var stompClient = null;

function onReady() {
    fetch('/actuator/health')
        .then(function (response) {
            return response.json();
        })
        .then(function (json) {
            console.log('parsed json', json);
            console.log('parsed json', json.details.messageStatus.status);
            updateMessageStatus(json.details.messageStatus.status);
        })
        .catch(function (ex) {
            console.log('parsing failed', ex)
        });
    connectWebSocket();
}

function updateMessageStatus(status) {
    document.getElementById('vHabStatus').innerHTML = status;
}

function connectWebSocket() {
    var socket = new SockJS('/vhab-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('STOMP: connected to ' + frame);
        stompClient.subscribe('/topic/status', function (status) {
            console.log('STOMP: receive status ' + status.body);
            updateMessageStatus(status.body);
        });
    }, function (error) {
        console.log('STOMP: ' + error);
        setTimeout(connectWebSocket, 10000);
        console.log('STOMP: Reconecting in 10 seconds');
    });
}

if (document.readyState === 'complete' || document.readyState !== 'loading') {
    onReady();
} else {
    document.addEventListener('DOMContentLoaded', onReady());
}
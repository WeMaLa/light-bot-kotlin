var stompClient = null;

function connectWebSocket() {
    var socket = new SockJS('/vhab-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('STOMP: connected to ' + frame);
        stompClient.subscribe('/topic/status', function (greeting) {
            console.log('STOMP: receive status ' + greeting.body);
            document.getElementById('vHabStatus').innerHTML = greeting.body;
        });
    }, function (error) {
        console.log('STOMP: ' + error);
        setTimeout(connectWebSocket, 10000);
        console.log('STOMP: Reconecting in 10 seconds');
    });
}

if (document.readyState === 'complete' || document.readyState !== 'loading') {
    connectWebSocket();
} else {
    document.addEventListener('DOMContentLoaded', connectWebSocket());
}
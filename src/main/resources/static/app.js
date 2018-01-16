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
    connectWebSocket('/topic/status', updateMessageStatus);
    connectWebSocket('/topic/event', eventReceived);
    initServiceOverlays();
}

function updateMessageStatus(status) {
    document.getElementById('vHabStatus').innerHTML = status;
}

function eventReceived(json) {
    var event = JSON.parse(json);

    var div = document.createElement("div");
    div.setAttribute('data-accessory-id', event.accessoryId);
    div.setAttribute('data-characteristic-id', event.characteristicId);
    div.setAttribute('data-value', event.value);
    div.appendChild(document.createTextNode(json));
    document.getElementById('events').appendChild(div);
}

function connectWebSocket(topic, callback) {
    var socket = new SockJS('/vhab-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('STOMP: connected to ' + frame);
        stompClient.subscribe(topic, function (status) {
            console.log('STOMP: receive content ' + status.body + ' on topic ' + topic);
            callback(status.body);
        });
    }, function (error) {
        console.log('STOMP: ' + error);
        setTimeout(connectWebSocket(topic, callback), 10000);
        console.log('STOMP: Reconnecting in 10 seconds');
    });
}

function initServiceOverlays() {
    document.getElementById('services').appendChild(createWindow('kitchen-window', 11000, 11100, 11101, 11102, 11103));
}

function createWindow(id, accessoryId, serviceId, targetPositionCharacteristicId, currentPositionCharacteristicId, nameCharacteristicId) {
    var div = document.createElement("div");
    div.setAttribute("id", id)
    div.setAttribute('data-type', 'window');
    div.setAttribute('data-accessory-id', accessoryId);
    div.setAttribute('data-service-id', serviceId);
    div.setAttribute('data-current-position-characteristic-id', currentPositionCharacteristicId);
    div.setAttribute('data-target-position-characteristic-id', targetPositionCharacteristicId);
    div.setAttribute('data-name-characteristic-id', nameCharacteristicId);
    div.appendChild(document.createTextNode(id));
    return div;
}

if (document.readyState === 'complete' || document.readyState !== 'loading') {
    onReady();
} else {
    document.addEventListener('DOMContentLoaded', onReady());
}
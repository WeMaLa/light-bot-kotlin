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
}

function updateMessageStatus(status) {
    document.getElementById('vHabStatus').innerHTML = status;
}

function eventReceived(json) {
    var event = JSON.parse(json);
    var accessoryIdAttribute = document.createAttribute('data-accessory-id');
    accessoryIdAttribute.value = event.accessoryId;
    var characteristicIdAttribute = document.createAttribute('data-characteristic-id');
    characteristicIdAttribute.value = event.characteristicId;
    var valueAttribute = document.createAttribute('data-value');
    valueAttribute.value = event.value;

    var textnode = document.createTextNode(json);

    var div = document.createElement("div");
    div.setAttributeNode(accessoryIdAttribute);
    div.setAttributeNode(characteristicIdAttribute);
    div.setAttributeNode(valueAttribute);
    div.appendChild(textnode);
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

if (document.readyState === 'complete' || document.readyState !== 'loading') {
    onReady();
} else {
    document.addEventListener('DOMContentLoaded', onReady());
}
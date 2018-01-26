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
    updateWindowState('kitchen-window', 11000, 11100, 11101, 11102, 11103);
    document.getElementById('services').appendChild(createLightBulb('kitchen-light-bulb', 12000, 12100, 12101, 12102));
    updateLightBulbState('kitchen-light-bulb', 12000, 12100, 12101, 12102);
}

function createWindow(htmlId, accessoryId, serviceId, targetPositionCharacteristicId, currentPositionCharacteristicId, nameCharacteristicId) {
    var div = document.createElement("div");
    div.setAttribute("id", htmlId);
    div.setAttribute('data-type', 'window');
    div.setAttribute('data-accessory-id', accessoryId);
    div.setAttribute('data-service-id', serviceId);
    div.setAttribute('data-current-position-characteristic-id', currentPositionCharacteristicId);
    div.setAttribute('data-target-position-characteristic-id', targetPositionCharacteristicId);
    div.setAttribute('data-name-characteristic-id', nameCharacteristicId);
    div.setAttribute('class', 'vhab-window-state');
    div.setAttribute('style', 'display:none');
    div.appendChild(document.createTextNode(htmlId));
    return div;
}

function updateWindowState(htmlId, accessoryId, serviceId, targetPositionCharacteristicId, currentPositionCharacteristicId, nameCharacteristicId) {
    fetchCharacteristic(accessoryId, serviceId, targetPositionCharacteristicId)
        .then(function (json) {
            console.log(json)
        });
    fetchCharacteristic(accessoryId, serviceId, currentPositionCharacteristicId)
        .then(function (json) {
            console.log(json)
        });
    fetchCharacteristic(accessoryId, serviceId, nameCharacteristicId).then(function (json) {
        console.log(json)
    });
}

function createLightBulb(htmlId, accessoryId, serviceId, onCharacteristicId, nameCharacteristicId) {
    var div = document.createElement("div");
    div.setAttribute("id", htmlId);
    div.setAttribute('data-type', 'lightBulb');
    div.setAttribute('data-accessory-id', accessoryId);
    div.setAttribute('data-service-id', serviceId);
    div.setAttribute('data-on-characteristic-id', onCharacteristicId);
    div.setAttribute('data-name-characteristic-id', nameCharacteristicId);
    div.setAttribute('class', 'vhab-window-state');
    div.setAttribute('style', 'display:none');
    div.appendChild(document.createTextNode(htmlId));
    return div;
}

function updateLightBulbState(htmlId, accessoryId, serviceId, onCharacteristicId, nameCharacteristicId) {
    var onCharacteristic = fetchCharacteristic(accessoryId, serviceId, onCharacteristicId);
    console.log(onCharacteristic);
    var nameCharacteristic = fetchCharacteristic(accessoryId, serviceId, nameCharacteristicId);
    console.log(nameCharacteristic);
}

function fetchCharacteristic(accessoryId, serviceId, characteristicId) {
    return fetch('/api/accessories/' + accessoryId + '/services/' + serviceId + '/characteristics/' + characteristicId)
        .then(function (response) {
            return response.json();
        })
        .then(function (json) {
            //console.log('parsed characteristic json', json);
            return json;
        })
        .catch(function (ex) {
            console.log('parsing failed', ex)
        });
}

if (document.readyState === 'complete' || document.readyState !== 'loading') {
    onReady();
} else {
    document.addEventListener('DOMContentLoaded', onReady());
}
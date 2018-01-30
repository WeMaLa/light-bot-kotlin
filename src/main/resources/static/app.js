var stompClientForStatus = null;
var stompClientForEvents = null;

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
    connectWebSocket(stompClientForStatus, '/topic/status', updateMessageStatus);
    connectWebSocket(stompClientForEvents, '/topic/event', eventReceived);
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

    // TODO update state
    // document.querySelector('div[data-accessory-id="11000"]').getAttribute("data-type")
}

function connectWebSocket(stompClient, topic, callback) {
    var socket = new SockJS('/vhab-websocket');
    stompClient = Stomp.over(socket);
    stompClient.debug = null;
    stompClient.connect({}, function (frame) {
        console.log('STOMP (' + topic + '): connected to ' + frame);
        stompClient.subscribe(topic, function (status) {
            //console.log('STOMP: receive content ' + status.body + ' on topic ' + topic);
            callback(status.body);
        });
    }, function (error) {
        console.log('STOMP: ' + error);
        setTimeout(function () {
            connectWebSocket(stompClient, topic, callback)
        }, 10000);
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
    div.appendChild(document.createTextNode(htmlId));
    return div;
}

function updateWindowState(htmlId, accessoryId, serviceId, targetPositionCharacteristicId, currentPositionCharacteristicId, nameCharacteristicId) {
    var accessoryDiv = document.querySelector('div[data-accessory-id="' + accessoryId + '"]');

    if (accessoryDiv == null) {
        console.error("Could not find html accessory element with id '" + accessoryId + "'");
        return;
    }

    fetchCharacteristic(accessoryId, serviceId, targetPositionCharacteristicId).then(function (json) {
        accessoryDiv.innerText = json.value;
        console.log(json);
    });
    fetchCharacteristic(accessoryId, serviceId, currentPositionCharacteristicId).then(function (json) {
        console.log(json);
    });
    fetchCharacteristic(accessoryId, serviceId, nameCharacteristicId).then(function (json) {
        console.log(json);
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
    fetchCharacteristic(accessoryId, serviceId, onCharacteristicId).then(function (json) {
        console.log(json);
    });
    fetchCharacteristic(accessoryId, serviceId, nameCharacteristicId).then(function (json) {
        console.log(json);
    });
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
import {IEvent} from "../event/IEvent";
import {WebSocketEvent} from "./webSocketEvent";
import {EventList} from "../event/EventList";
import SockJS from 'sockjs-client'
import Stomp from '@stomp/stompjs'

export class WebSocket {

    private static _addWebSocketEvents: EventList<WebSocket, WebSocketEvent> = new EventList<WebSocket, WebSocketEvent>();

    private _stompClientForEvents = null;

    public start() {
        this.connectWebSocket(this._stompClientForEvents, '/topic/event', this.handleEvent)
    }

    get onEvent(): IEvent<WebSocket, WebSocketEvent> {
        return WebSocket._addWebSocketEvents.get("onEvent");
    }

    handleEvent(message: string) {
        let event = JSON.parse(message);
        let webSocketEvent = new WebSocketEvent(event.accessoryId, event.characteristicId, event.value);
        WebSocket._addWebSocketEvents.get("onEvent").dispatch(this, webSocketEvent);
    }

    connectWebSocket(stompClient, topic: string, callback: (message: string) => void) {
        let socket = new SockJS('/vhab-websocket');
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
                this.connectWebSocket(stompClient, topic, callback);
            }, 10000);
            console.log('STOMP: Reconnecting in 10 seconds');
        });
    }

}
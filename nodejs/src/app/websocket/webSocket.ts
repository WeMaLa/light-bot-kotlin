import {IEvent} from "../event/IEvent";
import {AccessoryWebSocketEvent, StatusWebSocketEvent, WebSocketEvent} from "./webSocketEvent";
import {EventList} from "../event/EventList";
import SockJS from 'sockjs-client'
import Stomp from '@stomp/stompjs'

export class WebSocket<E extends WebSocketEvent> {

    public _addWebSocketEvents: EventList<WebSocket<E>, E> = new EventList<WebSocket<E>, E>();

    private _stompClientForEvents = null;
    private _topic: string = null;

    constructor(topic: string) {
        this._topic = topic;
    }

    public start() {
        this.connectWebSocket(this._stompClientForEvents, this._topic, this.handleEvent)
    }

    get onEvent(): IEvent<WebSocket<E>, E> {
        return this._addWebSocketEvents.get("onEvent");
    }

    handleEvent(websocket: WebSocket<E>, message: string) {
        try {
            let event = JSON.parse(message);
            let webSocketEvent = <E><any> new AccessoryWebSocketEvent(event.accessoryId, event.characteristicId, event.value);
            //console.log(webSocketEvent);
            websocket._addWebSocketEvents.get("onEvent").dispatch(this, webSocketEvent);
        } catch(e) {
            let webSocketEvent = <E><any> new StatusWebSocketEvent(message);
            //console.log(webSocketEvent);
            websocket._addWebSocketEvents.get("onEvent").dispatch(this, webSocketEvent);
        }
    }

    connectWebSocket(stompClient, topic: string, callback: (websocket: WebSocket<E>, message: string) => void) {
        let socket = new SockJS('/vhab-websocket');
        let that = this;
        stompClient = Stomp.over(socket);
        stompClient.debug = null;
        stompClient.connect({}, function (frame) {
            console.log('STOMP (' + topic + '): connected to ' + frame);
            stompClient.subscribe(topic, function (status) {
                //console.log('STOMP: receive content ' + status.body + ' on topic ' + topic);
                callback(that, status.body);
            });
        }, function (error) {
            console.log('STOMP: ' + error);
            setTimeout(function () {
                that.connectWebSocket(stompClient, topic, callback);
            }, 10000);
            console.log('STOMP: Reconnecting in 10 seconds');
        });
    }

}
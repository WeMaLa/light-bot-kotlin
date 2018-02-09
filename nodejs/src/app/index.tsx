/**
 * ======================================
 * This the root component for your app.
 * ======================================
 */

// import root less file here as an entry point for all other less files. these are imported
// in index.scss (including bootstrap).
// You can also import less files in other tsx files instead of importing them in index.scss.

// import "React" here to prevent the error "TS2686 [...]refers to a UMD global[...]"
import * as React from "react";
import * as ReactDOM from "react-dom";

import "./index.scss";
import {GroundPlot} from "./components/groundPlot";
import {WebSocket} from "./websocket/webSocket";
import {Kitchen} from "./components/room/kitchen";
import {Diner} from "./components/room/diner";
import {Living} from "./components/room/living";
import {Store} from "./components/room/store";
import {RestSmall} from "./components/room/restSmall";
import {RestBig} from "./components/room/restBig";
import {Entrance} from "./components/room/entrance";
import {Bed} from "./components/room/bed";
import {Study} from "./components/room/study";
import {InfoBox} from "./components/infoBox";
import {EventList} from "./components/eventList";
import {AccessoryWebSocketEvent, StatusWebSocketEvent} from "./websocket/webSocketEvent";

const eventWebSocket: WebSocket<AccessoryWebSocketEvent> = new WebSocket<AccessoryWebSocketEvent>("/topic/event");
const statusWebSocket: WebSocket<StatusWebSocketEvent> = new WebSocket<StatusWebSocketEvent>("/topic/status");

ReactDOM.render(
    <div>
        <GroundPlot/>
        <Kitchen webSocket={eventWebSocket}/>
        <Diner webSocket={eventWebSocket}/>
        <Living webSocket={eventWebSocket}/>
        <Store webSocket={eventWebSocket}/>
        <RestSmall webSocket={eventWebSocket}/>
        <RestBig webSocket={eventWebSocket}/>
        <Entrance webSocket={eventWebSocket}/>
        <Bed webSocket={eventWebSocket}/>
        <Study webSocket={eventWebSocket}/>
        <InfoBox vHABStateWebSocket={statusWebSocket} accessoryWebSocket={eventWebSocket}/>
        <EventList/>
    </div>,
    document.getElementById("info")
);

if (document.readyState === 'complete' || document.readyState !== 'loading') {
    eventWebSocket.start();
    statusWebSocket.start();
} else {
    document.addEventListener('DOMContentLoaded', eventWebSocket.start());
    document.addEventListener('DOMContentLoaded', statusWebSocket.start());
}
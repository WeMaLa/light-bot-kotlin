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

const webSocket: WebSocket = new WebSocket();

ReactDOM.render(
    <div className="info">
        <GroundPlot/>
        <Kitchen webSocket={webSocket}/>
        <Diner webSocket={webSocket}/>
    </div>,
    document.getElementById("info")
);

if (document.readyState === 'complete' || document.readyState !== 'loading') {
    webSocket.start();
} else {
    document.addEventListener('DOMContentLoaded', webSocket.start());
}
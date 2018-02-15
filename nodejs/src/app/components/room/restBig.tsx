import * as React from "react";
import {WebSocket} from "../../websocket/webSocket";
import {LightBulb} from "../accessory/lightBulb";
import {AccessoryWebSocketEvent} from "../../websocket/webSocketEvent";
import {GroundPlotInitializer} from "../groundPlot";

export interface RestBigProps {
    webSocket: WebSocket<AccessoryWebSocketEvent>;
    initializer: GroundPlotInitializer;
}

export interface RestBigState {
}

export class RestBig extends React.Component<RestBigProps, RestBigState> {

    render() {
        return <div className="room rest big">
            <LightBulb webSocket={this.props.webSocket}
                       offsetXInPercent={0.23}
                       offsetYInPercent={0.553}
                       accessoryId={61000}
                       serviceId={61100}
                       onCharacteristicId={61101}
                       nameCharacteristicId={61102}
                       initializer={this.props.initializer}/>
        </div>
    }
}
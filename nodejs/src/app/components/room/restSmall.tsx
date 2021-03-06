import * as React from "react";
import {WebSocket} from "../../websocket/webSocket";
import {LightBulb} from "../accessory/lightBulb";
import {AccessoryWebSocketEvent} from "../../websocket/webSocketEvent";
import {GroundPlotInitializer} from "../groundPlot";

export interface RestSmallProps {
    webSocket: WebSocket<AccessoryWebSocketEvent>;
    initializer: GroundPlotInitializer;
}

export interface RestSmallState {
}

export class RestSmall extends React.Component<RestSmallProps, RestSmallState> {

    render() {
        return <div className="room rest small">
            <LightBulb webSocket={this.props.webSocket}
                       offsetXInPercent={0.382}
                       offsetYInPercent={0.471}
                       accessoryId={51000}
                       serviceId={51100}
                       onCharacteristicId={51101}
                       nameCharacteristicId={51102}
                       initializer={this.props.initializer}/>
        </div>
    }
}
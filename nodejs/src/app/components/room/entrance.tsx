import * as React from "react";
import {WebSocket} from "../../websocket/webSocket";
import {LightBulb} from "../accessory/lightBulb";
import {AccessoryWebSocketEvent} from "../../websocket/webSocketEvent";
import {GroundPlotInitializer} from "../groundPlot";

export interface EntranceProps {
    webSocket: WebSocket<AccessoryWebSocketEvent>;
    initializer: GroundPlotInitializer;
}

export interface EntranceState {
}

export class Entrance extends React.Component<EntranceProps, EntranceState> {

    render() {
        return <div className="room entrance">
            <LightBulb webSocket={this.props.webSocket}
                       offsetXInPercent={0.248}
                       offsetYInPercent={0.365}
                       accessoryId={71000}
                       serviceId={71100}
                       onCharacteristicId={71101}
                       nameCharacteristicId={71102}
                       initializer={this.props.initializer}/>
            <LightBulb webSocket={this.props.webSocket}
                       offsetXInPercent={0.535}
                       offsetYInPercent={0.365}
                       accessoryId={72000}
                       serviceId={72100}
                       onCharacteristicId={72101}
                       nameCharacteristicId={72102}
                       initializer={this.props.initializer}/>
        </div>
    }
}
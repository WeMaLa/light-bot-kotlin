import * as React from 'react';
import {Window} from "../accessory/window";
import {WebSocket} from "../../websocket/webSocket";
import {LightBulb} from "../accessory/lightBulb";
import {AccessoryWebSocketEvent} from "../../websocket/webSocketEvent";

export interface KitchenProps {
    webSocket: WebSocket<AccessoryWebSocketEvent>;
}

export interface KitchenState {
}

export class Kitchen extends React.Component<KitchenProps, KitchenState> {

    render() {
        return <div className='room kitchen'>
            <Window webSocket={this.props.webSocket}
                    offsetXInPercent={0.42}
                    offsetYInPercent={0.02}
                    accessoryId={11000}
                    serviceId={11100}
                    currentPositionCharacteristicId={11102}
                    targetPositionCharacteristicId={11101}
                    nameCharacteristicId={11103}/>
            <LightBulb webSocket={this.props.webSocket}
                       offsetXInPercent={0.4}
                       offsetYInPercent={0.18}
                       accessoryId={12000}
                       serviceId={12100}
                       onCharacteristicId={12101}
                       nameCharacteristicId={12102}/>
        </div>
    }
}
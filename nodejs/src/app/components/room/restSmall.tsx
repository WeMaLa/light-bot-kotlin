import * as React from 'react';
import {Window} from "../accessory/window";
import {WebSocket} from "../../websocket/webSocket";
import {LightBulb} from "../accessory/lightBulb";
import {Heater} from "../accessory/heater";
import {AccessoryWebSocketEvent} from "../../websocket/webSocketEvent";

export interface RestSmallProps {
    webSocket: WebSocket<AccessoryWebSocketEvent>;
}

export interface RestSmallState {
}

export class RestSmall extends React.Component<RestSmallProps, RestSmallState> {

    render() {
        return <div className='room rest small'>
            <LightBulb webSocket={this.props.webSocket}
                       offsetXInPercent={0.382}
                       offsetYInPercent={0.471}
                       accessoryId={51000}
                       serviceId={51100}
                       onCharacteristicId={51101}
                       nameCharacteristicId={51102}/>
        </div>
    }
}
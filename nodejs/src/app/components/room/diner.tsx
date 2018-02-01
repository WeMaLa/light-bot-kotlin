import * as React from 'react';
import {Window} from "../accessory/window";
import {WebSocket} from "../../websocket/webSocket";
import {LightBulb} from "../accessory/lightBulb";
import {Heater} from "../accessory/heater";

export interface DinerProps {
    webSocket: WebSocket;
}

export interface DinerState {
}

export class Diner extends React.Component<DinerProps, DinerState> {

    render() {
        return <div className='room kitchen'>
            <Heater webSocket={this.props.webSocket}
                    offsetXInPercent={0.55}
                    offsetYInPercent={0.06}
                    accessoryId={21000}
                    serviceId={21100}
                    currentTemperatureCharacteristicId={21102}
                    targetTemperatureCharacteristicId={21101}
                    nameCharacteristicId={21103}/>
        </div>
    }
}
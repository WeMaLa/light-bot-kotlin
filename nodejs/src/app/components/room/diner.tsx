import * as React from 'react';
import {Window} from "../accessory/window";
import {WebSocket} from "../../websocket/webSocket";
import {LightBulb} from "../accessory/lightBulb";
import {Heater} from "../accessory/heater";
import {AccessoryWebSocketEvent} from "../../websocket/webSocketEvent";

export interface DinerProps {
    webSocket: WebSocket<AccessoryWebSocketEvent>;
}

export interface DinerState {
}

export class Diner extends React.Component<DinerProps, DinerState> {

    render() {
        return <div className='room diner'>
            <Heater webSocket={this.props.webSocket}
                    offsetXInPercent={0.55}
                    offsetYInPercent={0.06}
                    accessoryId={21000}
                    serviceId={21100}
                    currentTemperatureCharacteristicId={21102}
                    targetTemperatureCharacteristicId={21101}
                    nameCharacteristicId={21103}/>
            <Window webSocket={this.props.webSocket}
                    offsetXInPercent={0.62}
                    offsetYInPercent={0.02}
                    accessoryId={22000}
                    serviceId={22100}
                    currentPositionCharacteristicId={22102}
                    targetPositionCharacteristicId={22101}
                    nameCharacteristicId={22103}/>
            <LightBulb webSocket={this.props.webSocket}
                       offsetXInPercent={0.535}
                       offsetYInPercent={0.18}
                       accessoryId={23000}
                       serviceId={23100}
                       onCharacteristicId={23101}
                       nameCharacteristicId={23102}/>
            <LightBulb webSocket={this.props.webSocket}
                       offsetXInPercent={0.67}
                       offsetYInPercent={0.18}
                       accessoryId={24000}
                       serviceId={24100}
                       onCharacteristicId={24101}
                       nameCharacteristicId={24102}/>
        </div>
    }
}
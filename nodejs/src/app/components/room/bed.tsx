import * as React from 'react';
import {Window} from "../accessory/window";
import {WebSocket} from "../../websocket/webSocket";
import {LightBulb} from "../accessory/lightBulb";
import {Heater} from "../accessory/heater";

export interface BedProps {
    webSocket: WebSocket;
}

export interface BedState {
}

export class Bed extends React.Component<BedProps, BedState> {

    render() {
        return <div className='room bed'>
            <Heater webSocket={this.props.webSocket}
                    offsetXInPercent={0.055}
                    offsetYInPercent={0.5}
                    accessoryId={81000}
                    serviceId={81100}
                    currentTemperatureCharacteristicId={81102}
                    targetTemperatureCharacteristicId={81101}
                    nameCharacteristicId={81103}/>
            <Window webSocket={this.props.webSocket}
                    offsetXInPercent={0.025}
                    offsetYInPercent={0.6}
                    accessoryId={82000}
                    serviceId={82100}
                    currentPositionCharacteristicId={82102}
                    targetPositionCharacteristicId={82101}
                    nameCharacteristicId={82103}/>
            <LightBulb webSocket={this.props.webSocket}
                       offsetXInPercent={0.13}
                       offsetYInPercent={0.45}
                       accessoryId={83000}
                       serviceId={83100}
                       onCharacteristicId={83101}
                       nameCharacteristicId={83102}/>
            <LightBulb webSocket={this.props.webSocket}
                       offsetXInPercent={0.13}
                       offsetYInPercent={0.654}
                       accessoryId={84000}
                       serviceId={84100}
                       onCharacteristicId={84101}
                       nameCharacteristicId={84102}/>
        </div>
    }
}
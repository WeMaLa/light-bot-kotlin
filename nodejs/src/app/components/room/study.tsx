import * as React from 'react';
import {Window} from "../accessory/window";
import {WebSocket} from "../../websocket/webSocket";
import {LightBulb} from "../accessory/lightBulb";
import {Heater} from "../accessory/heater";

export interface StudyProps {
    webSocket: WebSocket;
}

export interface StudyState {
}

export class Study extends React.Component<StudyProps, StudyState> {

    render() {
        return <div className='room study'>
            // north
            <Heater webSocket={this.props.webSocket}
                    offsetXInPercent={0.245}
                    offsetYInPercent={0.06}
                    accessoryId={91000}
                    serviceId={91100}
                    currentTemperatureCharacteristicId={91102}
                    targetTemperatureCharacteristicId={91101}
                    nameCharacteristicId={91103}/>
            // west
            <Heater webSocket={this.props.webSocket}
                    offsetXInPercent={0.053}
                    offsetYInPercent={0.215}
                    accessoryId={92000}
                    serviceId={92100}
                    currentTemperatureCharacteristicId={92102}
                    targetTemperatureCharacteristicId={92101}
                    nameCharacteristicId={92103}/>
            // north
            <Window webSocket={this.props.webSocket}
                    offsetXInPercent={0.17}
                    offsetYInPercent={0.02}
                    accessoryId={93000}
                    serviceId={93100}
                    currentPositionCharacteristicId={93102}
                    targetPositionCharacteristicId={93101}
                    nameCharacteristicId={93103}/>
            // west
            <Window webSocket={this.props.webSocket}
                    offsetXInPercent={0.018}
                    offsetYInPercent={0.155}
                    accessoryId={94000}
                    serviceId={94100}
                    currentPositionCharacteristicId={94102}
                    targetPositionCharacteristicId={94101}
                    nameCharacteristicId={94103}/>
            <LightBulb webSocket={this.props.webSocket}
                       offsetXInPercent={0.13}
                       offsetYInPercent={0.18}
                       accessoryId={95000}
                       serviceId={95100}
                       onCharacteristicId={95101}
                       nameCharacteristicId={95102}/>
            <LightBulb webSocket={this.props.webSocket}
                       offsetXInPercent={0.265}
                       offsetYInPercent={0.18}
                       accessoryId={96000}
                       serviceId={96100}
                       onCharacteristicId={96101}
                       nameCharacteristicId={96102}/>
        </div>
    }
}
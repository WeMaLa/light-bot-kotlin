import * as React from 'react';
import {Window} from "../accessory/window";
import {WebSocket} from "../../websocket/webSocket";
import {LightBulb} from "../accessory/lightBulb";
import {Heater} from "../accessory/heater";

export interface LivingProps {
    webSocket: WebSocket;
}

export interface LivingState {
}

export class Living extends React.Component<LivingProps, LivingState> {

    render() {
        return <div className='room living'>
            <Heater webSocket={this.props.webSocket}
                    offsetXInPercent={0.85}
                    offsetYInPercent={0.63}
                    accessoryId={31000}
                    serviceId={31100}
                    currentTemperatureCharacteristicId={31102}
                    targetTemperatureCharacteristicId={31101}
                    nameCharacteristicId={31103}/>
            <Window webSocket={this.props.webSocket}
                    offsetXInPercent={0.9}
                    offsetYInPercent={0.47}
                    accessoryId={32000}
                    serviceId={32100}
                    currentPositionCharacteristicId={32102}
                    targetPositionCharacteristicId={32101}
                    nameCharacteristicId={32103}/>
            <LightBulb webSocket={this.props.webSocket}
                       offsetXInPercent={0.798}
                       offsetYInPercent={0.4}
                       accessoryId={33000}
                       serviceId={33100}
                       onCharacteristicId={33101}
                       nameCharacteristicId={33102}/>
            <LightBulb webSocket={this.props.webSocket}
                       offsetXInPercent={0.9}
                       offsetYInPercent={0.4}
                       accessoryId={34000}
                       serviceId={34100}
                       onCharacteristicId={34101}
                       nameCharacteristicId={34102}/>
            <LightBulb webSocket={this.props.webSocket}
                       offsetXInPercent={0.798}
                       offsetYInPercent={0.55}
                       accessoryId={35000}
                       serviceId={35100}
                       onCharacteristicId={35101}
                       nameCharacteristicId={35102}/>
            <LightBulb webSocket={this.props.webSocket}
                       offsetXInPercent={0.9}
                       offsetYInPercent={0.55}
                       accessoryId={36000}
                       serviceId={36100}
                       onCharacteristicId={36101}
                       nameCharacteristicId={36102}/>
        </div>
    }
}
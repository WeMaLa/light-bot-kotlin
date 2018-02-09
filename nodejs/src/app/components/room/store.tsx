import * as React from 'react';
import {Window} from "../accessory/window";
import {WebSocket} from "../../websocket/webSocket";
import {LightBulb} from "../accessory/lightBulb";
import {Heater} from "../accessory/heater";

export interface StoreProps {
    webSocket: WebSocket;
}

export interface StoreState {
}

export class Store extends React.Component<StoreProps, StoreState> {

    render() {
        return <div className='room store'>
            <LightBulb webSocket={this.props.webSocket}
                       offsetXInPercent={0.625}
                       offsetYInPercent={0.532}
                       accessoryId={41000}
                       serviceId={41100}
                       onCharacteristicId={41101}
                       nameCharacteristicId={41102}/>
        </div>
    }
}
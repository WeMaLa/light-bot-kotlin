import * as React from 'react';

import './accessory.scss'
import {WebSocket} from "../../websocket/webSocket";

export interface HeaterProps {
    accessoryId: number;
    serviceId: number;
    currentTemperatureCharacteristicId: number;
    targetTemperatureCharacteristicId: number;
    nameCharacteristicId: number;
    webSocket: WebSocket;
    offsetXInPercent: number;
    offsetYInPercent: number;
}

export interface HeaterState {
    name: string;
    currentTemperature: number;
    targetTemperature: number;
    loaded: boolean;
    positionTop: number;
    positionLeft: number;
}

export class Heater extends React.Component<HeaterProps, HeaterState> {

    constructor(props: HeaterProps, context: any) {
        super(props, context);

        this.state = {
            loaded: false,
            name: '',
            currentTemperature: 0,
            targetTemperature: 0,
            positionTop: 0,
            positionLeft: 0,
        };
    }

    componentWillMount(): void {
        this.props.webSocket.onEvent.subscribe("accessory", (sender, event) => {
            if (event.accessoryId === this.props.accessoryId) {
                console.log('Found accessory');

                if (event.characteristicId === this.props.currentTemperatureCharacteristicId) {
                    this.setState({
                        currentTemperature: +event.value
                    });
                } else if (event.characteristicId === this.props.targetTemperatureCharacteristicId) {
                    this.setState({
                        targetTemperature: +event.value
                    });
                }
            }
        });
    }

    componentWillUnmount(): void {
        this.props.webSocket.onEvent.unsubscribe("accessory");
    }

    componentDidMount(): void {
        let that = this;
        fetch('/api/accessories/' + this.props.accessoryId + '/services/' + this.props.serviceId + '/characteristics/' + this.props.nameCharacteristicId)
            .then(function (response) {
                return response.json();
            })
            .then(function (json) {
                that.setState({
                    loaded: true,
                    name: json.value,
                });
                that.updateDimensions();
            })
            .catch(function (ex) {
                console.log('parsing failed', ex)
            });
        fetch('/api/accessories/' + this.props.accessoryId + '/services/' + this.props.serviceId + '/characteristics/' + this.props.targetTemperatureCharacteristicId)
            .then(function (response) {
                return response.json();
            })
            .then(function (json) {
                that.setState({
                    loaded: true,
                    targetTemperature: json.value,
                });
                that.updateDimensions();
            })
            .catch(function (ex) {
                console.log('parsing failed', ex)
            });
        fetch('/api/accessories/' + this.props.accessoryId + '/services/' + this.props.serviceId + '/characteristics/' + this.props.currentTemperatureCharacteristicId)
            .then(function (response) {
                return response.json();
            })
            .then(function (json) {
                that.setState({
                    loaded: true,
                    currentTemperature: json.value,
                });
                that.updateDimensions();
            })
            .catch(function (ex) {
                console.log('parsing failed', ex)
            });

        window.addEventListener("resize", this.updateDimensions.bind(this));
    }

    updateDimensions() {
        // console.log('update dimensions');
        let image = document.querySelector('.ground-plot-image') as HTMLElement;
        let imageWidth = image.offsetWidth;
        let imageHeight = image.offsetHeight;
        let offset = this.offset(image);
        // console.log('Offset left: ' + offset.left);
        // console.log('Offset top: ' + offset.top);
        // console.log('Width: ' + imageWidth);
        // console.log('Height: ' + imageHeight);

        this.setState({
            positionTop: offset.top + (imageHeight * this.props.offsetYInPercent),
            positionLeft: offset.left + (imageWidth * this.props.offsetXInPercent),
        });
    }

    offset(el) {
        let rect = el.getBoundingClientRect(),
            scrollLeft = window.pageXOffset || document.documentElement.scrollLeft,
            scrollTop = window.pageYOffset || document.documentElement.scrollTop;
        return {top: rect.top + scrollTop, left: rect.left + scrollLeft}
    }

    render() {
        const divStyle = {
            top: this.state.positionTop,
            left: this.state.positionLeft
        };

        return <div className='accessory' style={divStyle}>
            {!this.state.loaded ?
                <div className='loading'>Loading</div> :
                <div className='window'>
                    <div className='name'>{this.state.name}</div>
                    <div className='target-temperature'>Target temperature: {this.state.targetTemperature}</div>
                    <div className='current-temperature'>Current temperature: {this.state.currentTemperature}</div>
                </div>
            }
        </div>
    }
}
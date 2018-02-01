import * as React from 'react';

import './accessory.scss'
import {WebSocket} from "../../websocket/webSocket";

export interface WindowProps {
    accessoryId: number;
    serviceId: number;
    currentPositionCharacteristicId: number;
    targetPositionCharacteristicId: number;
    nameCharacteristicId: number;
    webSocket: WebSocket;
    offsetXInPercent: number;
    offsetYInPercent: number;
}

export interface WindowState {
    name: string;
    currentPosition: number;
    targetPosition: number;
    loaded: boolean;
    positionTop: number;
    positionLeft: number;
}

export class Window extends React.Component<WindowProps, WindowState> {

    constructor(props: WindowProps, context: any) {
        super(props, context);

        this.state = {
            loaded: false,
            name: '',
            currentPosition: 0,
            targetPosition: 0,
            positionTop: 0,
            positionLeft: 0,
        };
    }

    componentWillMount(): void {
        this.props.webSocket.onEvent.subscribe("accessory", (sender, event) => {
            if (event.accessoryId === this.props.accessoryId) {
                console.log('Found accessory');

                if (event.characteristicId === this.props.currentPositionCharacteristicId) {
                    this.setState({
                        currentPosition: +event.value
                    });
                } else if (event.characteristicId === this.props.targetPositionCharacteristicId) {
                    this.setState({
                        targetPosition: +event.value
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
        fetch('/api/accessories/' + this.props.accessoryId + '/services/' + this.props.serviceId + '/characteristics/' + this.props.targetPositionCharacteristicId)
            .then(function (response) {
                return response.json();
            })
            .then(function (json) {
                that.setState({
                    loaded: true,
                    targetPosition: json.value,
                });
                that.updateDimensions();
            })
            .catch(function (ex) {
                console.log('parsing failed', ex)
            });
        fetch('/api/accessories/' + this.props.accessoryId + '/services/' + this.props.serviceId + '/characteristics/' + this.props.currentPositionCharacteristicId)
            .then(function (response) {
                return response.json();
            })
            .then(function (json) {
                that.setState({
                    loaded: true,
                    currentPosition: json.value,
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
                    <div className='target-position'>Target position: {this.state.targetPosition}</div>
                    <div className='current-position'>Current position: {this.state.currentPosition}</div>
                </div>
            }
        </div>
    }
}
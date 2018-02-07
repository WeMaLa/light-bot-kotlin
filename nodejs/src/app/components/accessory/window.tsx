import * as React from 'react';

import './accessory.scss'
import {WebSocket} from "../../websocket/webSocket";
import {Uuid} from "./uuid";

import FontAwesomeIcon from '@fortawesome/react-fontawesome';
import {
    faSquare as faSquareSolid,
    faThLarge as faSquareThreeQuarter,
    faTh as faSquareQuarter
} from '@fortawesome/fontawesome-free-solid';
import {faSquare as faSquareRegular} from '@fortawesome/fontawesome-free-regular';

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
    accessoryIcon: any;
}

export class Window extends React.Component<WindowProps, WindowState> {

    private _uuid: string;

    constructor(props: WindowProps, context: any) {
        super(props, context);

        this._uuid = Uuid.uuid();

        this.state = {
            loaded: false,
            name: '',
            currentPosition: 0,
            targetPosition: 0,
            positionTop: 0,
            positionLeft: 0,
            accessoryIcon: faSquareSolid
        };
    }

    componentWillMount(): void {
        this.props.webSocket.onEvent.subscribe("accessory_" + this._uuid, (sender, event) => {
            if (event.accessoryId === this.props.accessoryId) {
                console.log('Found accessory');

                if (event.characteristicId === this.props.currentPositionCharacteristicId) {
                    this.setState({
                        currentPosition: +event.value
                    });
                    this.updateAccessoryIcon();
                } else if (event.characteristicId === this.props.targetPositionCharacteristicId) {
                    this.setState({
                        targetPosition: +event.value
                    });
                }
            }
        });
    }

    componentWillUnmount(): void {
        this.props.webSocket.onEvent.unsubscribe("accessory_" + this._uuid);
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
                that.updateAccessoryIcon();
            })
            .catch(function (ex) {
                console.log('parsing failed', ex)
            });

        window.addEventListener("resize", this.updateDimensions.bind(this));
    }

    updateDimensions() {
        let image = document.querySelector('.ground-plot-image') as HTMLElement;
        let imageWidth = image.offsetWidth;
        let imageHeight = image.offsetHeight;
        let offset = this.offset(image);

        this.setState({
            positionTop: offset.top + (imageHeight * this.props.offsetYInPercent),
            positionLeft: offset.left + (imageWidth * this.props.offsetXInPercent),
        });
    }

    updateAccessoryIcon() {
        this.setState({
            accessoryIcon: this.state.currentPosition == 0 ? faSquareSolid
                : this.state.currentPosition == 100 ? faSquareRegular
                    : this.state.currentPosition > 50 ? faSquareQuarter
                        : faSquareThreeQuarter
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
                    <div className='icon'>
                        <FontAwesomeIcon icon={this.state.accessoryIcon} size='2x'/>
                    </div>
                    <div className='info'>
                        <div className='name'>{this.state.name}</div>
                        <div className='target-position'>Target position: {this.state.targetPosition}</div>
                        <div className='current-position'>Current position: {this.state.currentPosition}</div>
                        <div className='on'>Accessory: {this.props.accessoryId}</div>
                        <div className='on'>Service: {this.props.serviceId}</div>
                        <div className='on'>Characteristic: {this.props.targetPositionCharacteristicId}</div>
                    </div>
                </div>
            }
        </div>
    }
}
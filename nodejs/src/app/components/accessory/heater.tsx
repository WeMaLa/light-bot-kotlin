import * as React from "react";

import "./accessory.scss"
import "./heater.scss"
import {WebSocket} from "../../websocket/webSocket";
import {Uuid} from "./uuid";

import FontAwesomeIcon from "@fortawesome/react-fontawesome";
import {
    faThermometerEmpty,
    faThermometerQuarter,
    faThermometerHalf,
    faThermometerThreeQuarters,
    faThermometerFull
} from "@fortawesome/fontawesome-free-solid";
import {AccessoryWebSocketEvent} from "../../websocket/webSocketEvent";
import {GroundPlotInitializer} from "../groundPlot";

export interface HeaterProps {
    accessoryId: number;
    serviceId: number;
    currentTemperatureCharacteristicId: number;
    targetTemperatureCharacteristicId: number;
    nameCharacteristicId: number;
    webSocket: WebSocket<AccessoryWebSocketEvent>;
    initializer: GroundPlotInitializer;
    offsetXInPercent: number;
    offsetYInPercent: number;
}

export interface HeaterState {
    groundPlotInitialized: boolean;
    name: string;
    currentTemperature: number;
    targetTemperature: number;
    loaded: boolean;
    positionTop: number;
    positionLeft: number;
    accessoryIcon: any;
}

export class Heater extends React.Component<HeaterProps, HeaterState> {

    private _uuid: string;

    constructor(props: HeaterProps, context: any) {
        super(props, context);

        this._uuid = Uuid.uuid();

        this.state = {
            groundPlotInitialized: false,
            loaded: false,
            name: "",
            currentTemperature: 0,
            targetTemperature: 0,
            positionTop: 0,
            positionLeft: 0,
            accessoryIcon: faThermometerEmpty
        };
    }

    componentWillMount(): void {
        this.props.webSocket.onEvent.subscribe("accessory_" + this._uuid, (sender, event) => {
            if (event.accessoryId === this.props.accessoryId) {
                if (event.characteristicId === this.props.currentTemperatureCharacteristicId) {
                    this.setState({
                        currentTemperature: +event.value
                    });
                    this.updateAccessoryIcon();
                } else if (event.characteristicId === this.props.targetTemperatureCharacteristicId) {
                    this.setState({
                        targetTemperature: +event.value
                    });
                }
            }
        });
        this.props.initializer.onEvent.subscribe("accessory_" + this._uuid, (sender, event) => {
            if (event.imageLoaded) {
                this.setState({
                    groundPlotInitialized: true
                });
                this.updateDimensions();
            }
        });
    }

    componentWillUnmount(): void {
        this.props.webSocket.onEvent.unsubscribe("accessory_" + this._uuid);
        this.props.initializer.onEvent.unsubscribe("accessory_" + this._uuid);
    }

    componentDidMount(): void {
        let that = this;
        fetch("/api/accessories/" + this.props.accessoryId + "/services/" + this.props.serviceId + "/characteristics/" + this.props.nameCharacteristicId)
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
                console.log("parsing failed", ex)
            });
        fetch("/api/accessories/" + this.props.accessoryId + "/services/" + this.props.serviceId + "/characteristics/" + this.props.targetTemperatureCharacteristicId)
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
                console.log("parsing failed", ex)
            });
        fetch("/api/accessories/" + this.props.accessoryId + "/services/" + this.props.serviceId + "/characteristics/" + this.props.currentTemperatureCharacteristicId)
            .then(function (response) {
                return response.json();
            })
            .then(function (json) {
                that.setState({
                    loaded: true,
                    currentTemperature: json.value,
                });
                that.updateDimensions();
                that.updateAccessoryIcon();
            })
            .catch(function (ex) {
                console.log("parsing failed", ex)
            });

        window.addEventListener("resize", this.updateDimensions.bind(this));
    }

    updateDimensions() {
        let image = document.querySelector(".ground-plot > .image") as HTMLElement;
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
            accessoryIcon: this.state.currentTemperature <= 10 ? faThermometerEmpty
                : this.state.currentTemperature >= 38 ? faThermometerFull
                    : this.state.currentTemperature > 20 ? faThermometerThreeQuarters
                        : this.state.currentTemperature < 13 ? faThermometerQuarter
                            : faThermometerHalf
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

        return <div className="accessory" style={divStyle}>
            {this.state.loaded && this.state.groundPlotInitialized &&
                <div className="heater">
                    <div className="icon">
                        <FontAwesomeIcon icon={this.state.accessoryIcon} size="2x"/>
                    </div>
                    <div className="info">
                        <div>Beispielrequest</div>
                        <div className="code depth0">&#123;</div>
                        <div className="code depth1"> "characteristics":[</div>
                        <div className="code depth2">       &#123;</div>
                        <div className="code depth3"> "aid" : {this.props.accessoryId},</div>
                        <div className="code depth3"> "iid" : {this.props.targetTemperatureCharacteristicId},</div>
                        <div className="code depth3"> "value" : "{38 - this.state.targetTemperature + 10}"</div>
                        <div className="code depth2">       &#125;</div>
                        <div className="code depth1"> ]</div>
                        <div className="code depth0">&#125;</div>
                    </div>
                </div>
            }
        </div>
    }
}
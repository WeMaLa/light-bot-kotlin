import * as React from "react";

import "./accessory.scss"
import "./lightBulb.scss"
import {WebSocket} from "../../websocket/webSocket";
import {Uuid} from "./uuid";

import FontAwesomeIcon from "@fortawesome/react-fontawesome";
import {faLightbulb as faLightbulbSolid} from "@fortawesome/fontawesome-free-solid";
import {faLightbulb as faLightbulbRegular} from "@fortawesome/fontawesome-free-regular";
import {AccessoryWebSocketEvent} from "../../websocket/webSocketEvent";
import {GroundPlotInitializer} from "../groundPlot";

export interface LightBulbProps {
    accessoryId: number;
    serviceId: number;
    onCharacteristicId: number;
    nameCharacteristicId: number;
    webSocket: WebSocket<AccessoryWebSocketEvent>;
    initializer: GroundPlotInitializer;
    offsetXInPercent: number;
    offsetYInPercent: number;
}

export interface LightBulbState {
    groundPlotInitialized: boolean;
    name: string;
    on: boolean;
    loaded: boolean;
    positionTop: number;
    positionLeft: number;
    accessoryIcon: any;
}

export class LightBulb extends React.Component<LightBulbProps, LightBulbState> {

    private _uuid: string;

    constructor(props: LightBulbProps, context: any) {
        super(props, context);

        this._uuid = Uuid.uuid();

        this.state = {
            groundPlotInitialized: false,
            loaded: false,
            name: "",
            on: false,
            positionTop: 0,
            positionLeft: 0,
            accessoryIcon: faLightbulbRegular
        };
    }

    componentWillMount(): void {
        this.props.webSocket.onEvent.subscribe("accessory_" + this._uuid, (sender, event) => {
            if (event.accessoryId === this.props.accessoryId) {
                console.log("Found accessory");

                if (event.characteristicId === this.props.onCharacteristicId) {
                    this.setState({
                        on: event.value === "on" || event.value === "On" || event.value === "ON"
                    });
                    this.updateAccessoryIcon();
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
        fetch("/api/accessories/" + this.props.accessoryId + "/services/" + this.props.serviceId + "/characteristics/" + this.props.onCharacteristicId)
            .then(function (response) {
                return response.json();
            })
            .then(function (json) {
                that.setState({
                    loaded: true,
                    on: json.value === "on" || json.value === "On" || json.value === "ON"
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
            accessoryIcon: this.state.on ? faLightbulbSolid : faLightbulbRegular
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
                <div className="lightBulb" data-state={this.state.on ? "on" : "off"}>
                    <div className="icon">
                        <FontAwesomeIcon icon={this.state.accessoryIcon} size="2x"/>
                    </div>
                    <div className="info">
                        <div>Beispielrequest</div>
                        <div className="code depth0">&#123;</div>
                        <div className="code depth1"> "characteristics":[</div>
                        <div className="code depth2">       &#123;</div>
                        <div className="code depth3"> "aid" : {this.props.accessoryId},</div>
                        <div className="code depth3"> "iid" : {this.props.onCharacteristicId},</div>
                        <div className="code depth3"> "value" : "{this.state.on ? "off" : "on"}"</div>
                        <div className="code depth2">       &#125;</div>
                        <div className="code depth1"> ]</div>
                        <div className="code depth0">&#125;</div>
                    </div>
                </div>
            }
        </div>
    }
}
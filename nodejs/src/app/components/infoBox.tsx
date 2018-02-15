import * as React from "react";

import './infoBox.scss'
import {WebSocket} from "../websocket/webSocket";
import {AccessoryWebSocketEvent, StatusWebSocketEvent} from "../websocket/webSocketEvent";
import {GroundPlotInitializer} from "./groundPlot";

export interface InfoBoxProps {
    vHABStateWebSocket: WebSocket<StatusWebSocketEvent>;
    accessoryWebSocket: WebSocket<AccessoryWebSocketEvent>;
    initializer: GroundPlotInitializer;
}

export interface InfoBoxState {
    state: string;
    groundPlotInitialized: boolean;
}

export class InfoBox extends React.Component<InfoBoxProps, InfoBoxState> {

    private div: HTMLDivElement;

    constructor(props: any) {
        super(props);
        this.state = {
            state: "pending...",
            groundPlotInitialized: false
        };
    }

    componentWillMount(): void {
        this.props.initializer.onEvent.subscribe("info-box", (sender, event) => {
            this.setState({
                groundPlotInitialized: true
            });
            this.updateDimensions();
        });
    }

    componentWillUnmount(): void {
        this.props.initializer.onEvent.unsubscribe("info-box");
    }

    componentDidMount(): void {
        let that = this;
        fetch('/actuator/health')
            .then(function (response) {
                return response.json();
            })
            .then(function (json) {
                //console.log('parsed json', json);
                //console.log('parsed json', json.details.messageStatus.status);
                that.setState({
                    state: json.details.messageStatus.status
                });
                this.updateDimensions();
            })
            .catch(function (ex) {
                console.log('parsing failed', ex)
            });

        this.props.vHABStateWebSocket.onEvent.subscribe("infoBoxState", (sender, event) => {
            //console.log('Receive status event: ' + event.status);
            that.setState({
                state: event.status
            });
        });
        this.props.accessoryWebSocket.onEvent.subscribe("infoBoxAccessory", (sender, event) => {
            console.log(event);
        });

        window.addEventListener("resize", this.updateDimensions.bind(this));
    }

    updateDimensions() {
        const image = document.querySelector(".ground-plot > .image") as HTMLElement;
        const imageHeight = image.offsetHeight;
        this.div.setAttribute("style", "left: 20px; top: " + (imageHeight - 130) + "px;");
    }

    render() {
        return <div className="info info-box" ref={div => {this.div = div;}}>
            {this.state.groundPlotInitialized &&
            <div>
                STILL IN PROGRESS
                <div className="title">vHAB - virtual home automation bot</div>
                <div className="status">status: {this.state.state}</div>
                <div className="version">version: TODO</div>
                <div className="git revision">git revision: TODO</div>
            </div>
            }
        </div>
    }
}
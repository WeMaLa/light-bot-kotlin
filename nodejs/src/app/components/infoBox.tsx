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
    stage: string;
    version: string;
    groupId: string;
    artifactId: string;
    buildNumber: string;
    timestamp: string;
}

export class InfoBox extends React.Component<InfoBoxProps, InfoBoxState> {

    private div: HTMLDivElement;

    constructor(props: any) {
        super(props);
        this.state = {
            state: "pending...",
            stage: "pending...",
            version: "pending...",
            groupId: "pending...",
            artifactId: "pending...",
            buildNumber: "pending...",
            timestamp: "pending...",
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
        fetch('/actuator/info')
            .then(function (response) {
                return response.json();
            })
            .then(function (json) {
                //console.log('parsed json', json);
                //console.log('parsed json', json.details.messageStatus.status);
                that.setState({
                    stage: json.stage,
                    version: json.version,
                    groupId: json.groupId,
                    artifactId: json.artifactId,
                    buildNumber: json.buildNumber,
                    timestamp: that.convertToReadableTimestamp(json.timestamp),
                });
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

    private updateDimensions() {
        const image = document.querySelector(".ground-plot > .image") as HTMLElement;
        const imageHeight = image.offsetHeight;
        this.div.setAttribute("style", "left: 20px; top: " + (imageHeight - 150) + "px;");
    }

    private convertToReadableTimestamp(timestamp: number) {
        let newDate = new Date();
        newDate.setTime(timestamp);
        return newDate.toUTCString();
    }


    render() {
        return <div className="info info-box" ref={div => {
            this.div = div;
        }}>
            {this.state.groundPlotInitialized &&
            <div>
                <div className="title">vHAB - virtual home automation bot</div>
                <div className="system-information">
                    <div className={this.state.state === "OK" ? "status ok" : "status not-ok"}>
                        <label>Status</label>
                        <div className="value">{this.state.state}</div>
                    </div>
                    <div className="stage">
                        <label>Stage</label>
                        <div className="value">{this.state.stage}</div>
                    </div>
                    <div className="version">
                        <label>Version</label>
                        <div className="value">{this.state.version}</div>
                    </div>
                    <div className="groupId">
                        <label>GroupId</label>
                        <div className="value">{this.state.groupId}</div>
                    </div>
                    <div className="artifactId">
                        <label>ArtifactId</label>
                        <div className="value">{this.state.artifactId}</div>
                    </div>
                    <div className="buildNumber">
                        <label>BuildNumber</label>
                        <div className="value"><a href={"https://github.com/WeMaLa/light-bot-kotlin/commit/" + this.state.buildNumber} target="_blank">{this.state.buildNumber}</a></div>
                    </div>
                    <div className="timestamp">
                        <label>Timestamp</label>
                        <div className="value">{this.state.timestamp}</div>
                    </div>
                </div>
            </div>
            }
        </div>
    }
}
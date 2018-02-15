import * as React from "react";

import './eventList.scss'
import {GroundPlotInitializer} from "./groundPlot";
import {HeaterProps} from "./accessory/heater";
import {faThermometerEmpty} from "@fortawesome/fontawesome-free-solid";
import {Uuid} from "./accessory/uuid";

export interface EventListProps {
    initializer: GroundPlotInitializer;
}

export interface EventListState {
    groundPlotInitialized: boolean;
}

export class EventList extends React.Component<EventListProps, EventListState> {

    private div: HTMLDivElement;

    constructor(props: EventListProps, context: any) {
        super(props, context);

        this.state = {
            groundPlotInitialized: false
        };
    }

    componentWillMount(): void {
        this.props.initializer.onEvent.subscribe("event-list", (sender, event) => {
            this.setState({
                groundPlotInitialized: true
            });
            this.updateDimensions();
        });
    }

    componentWillUnmount(): void {
        this.props.initializer.onEvent.unsubscribe("event-list");
    }

    componentDidMount(): void {
        window.addEventListener("resize", this.updateDimensions.bind(this));
    }

    updateDimensions() {
        const image = document.querySelector(".ground-plot > .image") as HTMLElement;
        let imageWidth = image.offsetWidth;
        const imageHeight = image.offsetHeight;
        this.div.setAttribute("style", "left: " + (imageWidth - 340) + "px; top: " + (imageHeight - 130) + "px;");
    }

    render() {
        return <div className="info event-list" ref={div => {this.div = div;}}>
            {this.state.groundPlotInitialized &&
            <div>
                STILL IN PROGRES
                - last events -
            </div>
            }
        </div>
    }
}
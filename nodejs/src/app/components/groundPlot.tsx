import * as React from "react";

import './groundPlot.scss'

import FontAwesomeIcon from "@fortawesome/react-fontawesome";
import {faCog, faThumbsDown} from "@fortawesome/fontawesome-free-solid";
import {EventList} from "../event/EventList";
import {IEvent} from "../event/IEvent";

export interface GroundPlotProps {
    initializer: GroundPlotInitializer;
}

export interface GroundPlotState {
    imageLoaded: boolean;
    imageError: boolean;
}

export class GroundPlot extends React.Component<GroundPlotProps, GroundPlotState> {

    constructor(props) {
        super(props);
        this.state = {
            imageLoaded: false,
            imageError: false
        };
    }

    handleImageLoaded() {
        let that = this;
        setTimeout(function () {
            that.setState({imageLoaded: true});
            that.props.initializer.sendImageLoadedEvent();
        }, 500);
    }

    handleImageErrored() {
        this.setState({imageError: true});
    }

    render() {
        return <div className="ground-plot">
            {!this.state.imageLoaded && !this.state.imageError &&
            <div className="spinner">
                <FontAwesomeIcon className="icon"
                                 icon={faCog}
                                 size="8x"
                                 spin/>
                <div className="text">Lade vHAB (virtual Home Automation Bot)</div>
            </div>
            }
            {this.state.imageError &&
            <div className="spinner">
                <FontAwesomeIcon className="icon"
                                 icon={faThumbsDown}
                                 size="8x"/>
                <div className="text">vHAB (virtual Home Automation Bot) konnte nicht geladen werden</div>
            </div>
            }
            <img className={this.state.imageLoaded ? "image" : "image hidden"}
                 src="../images/groundplot.png"
                 onLoad={this.handleImageLoaded.bind(this)}
                 onError={this.handleImageErrored.bind(this)}/>
        </div>
    }
}

export class GroundPlotInitializer {
    private static _events: EventList<GroundPlotInitializer, GroundPlotInitializeEvent> = new EventList<GroundPlotInitializer, GroundPlotInitializeEvent>();

    get onEvent(): IEvent<GroundPlotInitializer, GroundPlotInitializeEvent> {
        return GroundPlotInitializer._events.get("onEvent");
    }

    sendImageLoadedEvent() {
        GroundPlotInitializer._events.get("onEvent").dispatch(this, new GroundPlotInitializeEvent(true));
    }
}

export class GroundPlotInitializeEvent {
    private _imageLoaded: boolean;

    constructor(imageLoaded: boolean) {
        this._imageLoaded = imageLoaded;
    }

    get imageLoaded(): boolean {
        return this._imageLoaded;
    }
}
import * as React from "react";

import './groundPlot.scss'

import FontAwesomeIcon from "@fortawesome/react-fontawesome";
import {faCog} from "@fortawesome/fontawesome-free-solid";

export interface GroundPlotProps {
}

export interface GroundPlotState {
    imageLoaded: boolean;
}

export class GroundPlot extends React.Component<GroundPlotProps, GroundPlotState> {

    constructor(props) {
        super(props);
        this.state = {imageLoaded: false};
    }

    handleImageLoaded() {
        this.setState({imageLoaded: true});
    }

    handleImageErrored() {
        this.setState({imageLoaded: false});
    }

    render() {
        return <div className="ground-plot">
            <img className={this.state.imageLoaded ? "image" : "image hidden"}
                 src="../images/groundplot.png"
                 onLoad={this.handleImageLoaded.bind(this)}
                 onError={this.handleImageErrored.bind(this)}/>
            <div className={this.state.imageLoaded ? "spinner hidden" : "spinner"}>
                <FontAwesomeIcon className="icon"
                                 icon={faCog}
                                 size="8x"
                                 spin/>
                <div className="text">Lade vHAB (virtual Home Automation Bot)</div>
            </div>
        </div>
    }
}
import * as React from "react";

import './groundPlot.scss'

export interface GroundPlotProps {
}

export interface GroundPlotState {
}

export class GroundPlot extends React.Component<GroundPlotProps, GroundPlotState> {

    render() {
        return <img className="ground-plot-image" src="../images/groundplot.png" />
    }
}
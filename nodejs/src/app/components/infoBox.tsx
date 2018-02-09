import * as React from 'react';

import './infoBox.scss'

export interface InfoBoxProps {
}

export interface InfoBoxState {
}

export class InfoBox extends React.Component<InfoBoxProps, InfoBoxState> {

    div: HTMLDivElement;

    componentDidMount(): void {
        this.updateDimensions();
        window.addEventListener("resize", this.updateDimensions.bind(this));
    }

    updateDimensions() {
        const image = document.querySelector('.ground-plot-image') as HTMLElement;
        const imageHeight = image.offsetHeight;
        this.div.setAttribute("style", "left: 20px; top: " + (imageHeight - 130) + "px;");
    }

    render() {
        return <div className="info info-box" ref={div => {this.div = div;}}>
            STILL IN PROGRESS
            <div className="title">vHAB - virtual home automation bot</div>
            <div className="status">status: TODO</div>
            <div className="version">version: TODO</div>
            <div className="git revision">git revision: TODO</div>
        </div>
    }
}
import * as React from 'react';

import './eventList.scss'

export interface EventListProps {
}

export interface EventListState {
}

export class EventList extends React.Component<EventListProps, EventListState> {

    div: HTMLDivElement;

    componentDidMount(): void {
        this.updateDimensions();
        window.addEventListener("resize", this.updateDimensions.bind(this));
    }

    updateDimensions() {
        const image = document.querySelector('.ground-plot-image') as HTMLElement;
        let imageWidth = image.offsetWidth;
        const imageHeight = image.offsetHeight;
        this.div.setAttribute("style", "left: " + (imageWidth - 350) + "px; top: " + (imageHeight - 130) + "px;");
    }

    render() {
        return <div className="info event-list" ref={div => {this.div = div;}}>
            STILL IN PROGRES
            - last events -
        </div>
    }
}
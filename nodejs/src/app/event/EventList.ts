import {EventDispatcher} from "./EventDispatcher";

export class EventList<TSender, TArgs> {

    private _events: { [name: string]: EventDispatcher<TSender, TArgs>; } = {};

    get(name: string): EventDispatcher<TSender, TArgs> {

        let event = this._events[name];

        if (event) {
            return event;
        }

        event = new EventDispatcher<TSender, TArgs>();
        this._events[name] = event;
        return event;
    }

    remove(name: string): void {
        this._events[name] = null;
    }
}
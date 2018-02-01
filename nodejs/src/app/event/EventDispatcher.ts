import {IEvent} from "./IEvent";

export class EventDispatcher<TSender, TArgs> implements IEvent<TSender, TArgs> {

    private _subscriptions: Map<string, (sender: TSender, args: TArgs) => void> = new Map();

    subscribe(component: string, fn: (sender: TSender, args: TArgs) => void): void {
        if (fn) {
            console.log("[EventDispatcher] Subscribe component '" + component + "'");
            this._subscriptions.set(component, fn);
        }
    }

    unsubscribe(component: string): void {
        console.log("[EventDispatcher] Unsubscribe component '" + component + "'");
        this._subscriptions.delete(component);
    }

    dispatch(sender: TSender, args: TArgs): void {
        this._subscriptions.forEach(function(handler, component) {
            handler(sender, args);
        });
    }
}
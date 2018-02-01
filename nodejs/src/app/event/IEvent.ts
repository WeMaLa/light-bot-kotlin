export interface IEvent<TSender, TArgs> {

    subscribe(component: string, fn: (sender: TSender, args: TArgs) => void): void;

    unsubscribe(component: string): void;

}
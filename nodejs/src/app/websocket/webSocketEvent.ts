export class WebSocketEvent {

    private _accessoryId: number;
    private _characteristicId: number;
    private _value: string;

    constructor(accessoryId: number, characteristicId: number, value: string) {
        this._accessoryId = accessoryId;
        this._characteristicId = characteristicId;
        this._value = value;
    }

    get accessoryId(): number {
        return this._accessoryId;
    }

    get characteristicId(): number {
        return this._characteristicId;
    }

    get value(): string {
        return this._value;
    }
}
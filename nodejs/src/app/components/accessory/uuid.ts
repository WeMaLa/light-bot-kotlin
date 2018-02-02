export class Uuid {

    /**
     * Erzeugt eine UUID nach RFC 4122
     */
    public static uuid(): string {
        return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, (char) => {
            let random = Math.random() * 16 | 0; // Nachkommastellen abschneiden
            let value = char === "x" ? random : (random % 4 + 8); // Bei x Random 0-15 (0-F), bei y Random 0-3 + 8 = 8-11 (8-b) gemäss RFC 4122
            return value.toString(16); // Hexadezimales Zeichen zurückgeben
        });
    }
}